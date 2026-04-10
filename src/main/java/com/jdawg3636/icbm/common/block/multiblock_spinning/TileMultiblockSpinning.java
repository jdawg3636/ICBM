package com.jdawg3636.icbm.common.block.multiblock_spinning;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.machine.AbstractContainerMachine;
import com.jdawg3636.icbm.common.block.machine.TileMachine;
import com.jdawg3636.icbm.common.capability.ICBMCapabilities;
import com.jdawg3636.icbm.common.capability.energystorage.ICBMEnergyStorage;
import com.jdawg3636.icbm.common.capability.missiledirector.LogicalMissile;
import com.jdawg3636.icbm.common.capability.missiledirector.MissileLaunchPhase;
import com.jdawg3636.icbm.common.entity.EntityLightningVisual;
import com.jdawg3636.icbm.common.reg.EntityReg;
import com.jdawg3636.icbm.common.reg.SoundEventReg;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.energy.CapabilityEnergy;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TileMultiblockSpinning extends TileMachine implements ITickableTileEntity {

    double animationPercent;
    double machineAffectedRadius;

    public TileMultiblockSpinning(TileEntityType<?> tileEntityType, Supplier<ContainerType<? extends AbstractContainerMachine>> containerSupplier, ITextComponent name) {
        super(tileEntityType, containerSupplier, ContainerMultiblockSpinning::new, 1, 1_000_000, 5_000, 0, new ArrayList<>(), name);
    }

    public void addAnimationPercent(double increment) {
        animationPercent += increment;
        while(animationPercent > 100) animationPercent -= 100D;
    }

    public float getAnimationRadians(float partialTicks) {
        double effectiveAnimationPercent = this.animationPercent;
        effectiveAnimationPercent += partialTicks * getAnimationSpeed();
        return (float)(effectiveAnimationPercent * 0.01 * 2 * Math.PI);
    }

    public double getAnimationSpeed() {
        return 6D * this.energyStorageLazyOptional.map((energyStorage) -> ((ICBMEnergyStorage)energyStorage).getEnergyPercentage()).orElse(0d);
    }

    public void setMachineAffectedRadius(double empRadius) {
        empRadius = Math.min(Math.max(empRadius, ICBMReference.COMMON_CONFIG.getEMPTowerRangeMinimum()), ICBMReference.COMMON_CONFIG.getEMPTowerRangeMaximum());
        this.machineAffectedRadius = empRadius;
    }

    public double getMachineAffectedRadius() {
        return machineAffectedRadius;
    }

    public void triggerEMPBlast() {
        // Server-side only
        if(level == null || level.isClientSide) return;
        // Get a reference to the Missile Director
        level.getCapability(ICBMCapabilities.MISSILE_DIRECTOR_CAPABILITY).ifPresent(missileDirector -> {
            // Find all launched missiles in the radius
            List<LogicalMissile> missilesToZap = missileDirector.getLogicalMissiles().values().stream()
                    .filter(logicalMissile -> logicalMissile.missileLaunchPhase == MissileLaunchPhase.LAUNCHED)
                    .filter(logicalMissile -> getBlockPos().distSqr(logicalMissile.x, logicalMissile.y, logicalMissile.z, true) < machineAffectedRadius * machineAffectedRadius)
                    .collect(Collectors.toList()); // must collect into list instead of using original stream - otherwise causes ConcurrentModificationException
            // Destroy targeted missiles in radius
            missilesToZap.forEach(logicalMissile -> {
                // Spawn visual lightning
                EntityLightningVisual entityLightningVisual = EntityReg.LIGHTNING_VISUAL.get().create(level);
                if(entityLightningVisual != null) {
                    BlockPos visualLightningPosition = getBlockPos().above(2);
                    entityLightningVisual.setPos(visualLightningPosition.getX() + 0.5, visualLightningPosition.getY(), visualLightningPosition.getZ() + 0.5);
                    entityLightningVisual.updateRotation(logicalMissile.x, logicalMissile.y, logicalMissile.z);
                    level.addFreshEntity(entityLightningVisual);
                }
                // Destroy missile
                logicalMissile.strikeWithEMP();
                // Play sound
                this.level.playSound((PlayerEntity)null, logicalMissile.x, logicalMissile.y, logicalMissile.z, SoundEvents.LIGHTNING_BOLT_IMPACT, SoundCategory.BLOCKS, 10.0F, 0.6F);
            });
            // Play sound
            this.level.playSound((PlayerEntity) null, getBlockPos().getX() + 0.5, getBlockPos().getY() + 1.5, getBlockPos().getZ() + 0.5, SoundEventReg.EFFECT_BEAM_DISCHARGE.get(), SoundCategory.BLOCKS, 100F, 1.0F);
            this.level.playSound((PlayerEntity) null, getBlockPos().getX() + 0.5, getBlockPos().getY() + 1.5, getBlockPos().getZ() + 0.5, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundCategory.BLOCKS, 300F, 1.0F);
        });
    }

    public void tick() {
        assert level != null;
        if(level.isClientSide()) {
            addAnimationPercent(getAnimationSpeed());
        } else {
            itemHandlerLazyOptional.ifPresent(itemHandler -> {
                itemHandler.getStackInSlot(0).getCapability(CapabilityEnergy.ENERGY).ifPresent(itemEnergyStorage -> {
                    tryReceiveEnergy(itemEnergyStorage, 10_000);
                });
            });
        }
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        this.setMachineAffectedRadius(tag.getDouble("radius"));
        super.load(state, tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.putDouble("radius", machineAffectedRadius);
        return super.save(tag);
    }

    public String getRadiusSliderText() {
        return "gui.icbm.emp_tower.radius";
    }

}
