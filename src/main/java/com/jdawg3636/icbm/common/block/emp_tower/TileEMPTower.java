package com.jdawg3636.icbm.common.block.emp_tower;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.machine.TileMachine;
import com.jdawg3636.icbm.common.capability.energystorage.ICBMEnergyStorage;
import com.jdawg3636.icbm.common.capability.missiledirector.LogicalMissile;
import com.jdawg3636.icbm.common.entity.EntityMissile;
import com.jdawg3636.icbm.common.reg.ContainerReg;
import com.jdawg3636.icbm.common.reg.SoundEventReg;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.energy.CapabilityEnergy;

import java.util.List;

public class TileEMPTower extends TileMachine implements ITickableTileEntity {

    public static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("gui.icbm.emp_tower");

    double animationPercent;
    double empRadius;

    public TileEMPTower(TileEntityType<?> tileEntityType) {
        this(tileEntityType, DEFAULT_NAME);
    }

    public TileEMPTower(TileEntityType<?> tileEntityType, ITextComponent name) {
        super(tileEntityType, ContainerReg.EMP_TOWER::get, 1, 1_000_000, 5_000, 0, name);
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

    public void setEMPRadius(double empRadius) {
        empRadius = Math.min(Math.max(empRadius, ICBMReference.COMMON_CONFIG.getEMPTowerRangeMinimum()), ICBMReference.COMMON_CONFIG.getEMPTowerRangeMaximum());
        this.empRadius = empRadius;
    }

    public double getEMPRadius() {
        return empRadius;
    }

    // TODO: move this into the missile director capability so that it can work on LogicalMissiles
    public void triggerEMPBlast() {
        if(level == null || level.isClientSide) return;
        List<EntityMissile> missilesToZap = level.getEntitiesOfClass(EntityMissile.class, new AxisAlignedBB(getBlockPos().above()).inflate(empRadius, 1000.0, empRadius));
        for(EntityMissile missile : missilesToZap) {
            missile.getLogicalMissile().ifPresent(LogicalMissile::strikeWithEMP);
        }
        this.level.playSound((PlayerEntity) null, getBlockPos().getX() + 0.5, getBlockPos().getY() + 1.5, getBlockPos().getZ() + 0.5, SoundEventReg.EFFECT_BEAM_DISCHARGE.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    public void tickSpinnyThing() {
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
    public void tick() {
        if(level == null) return;
        tickSpinnyThing();
        if(!level.isClientSide() && redstoneSignalPresent() && tryConsumeEnergy(ICBMReference.COMMON_CONFIG.empTowerEnergyUsePerBlast())) {
            triggerEMPBlast();
        }
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        this.setEMPRadius(tag.getDouble("radius"));
        super.load(state, tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.putDouble("radius", empRadius);
        return super.save(tag);
    }

    public String getRadiusSliderText() {
        return "gui.icbm.emp_tower.radius";
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox().expandTowards(0, 1, 0).inflate(1D/16D, 0, 1D/16D);
    }

}
