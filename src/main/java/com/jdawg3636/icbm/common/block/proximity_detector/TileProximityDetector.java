package com.jdawg3636.icbm.common.block.proximity_detector;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.ITileHasRadioFrequency;
import com.jdawg3636.icbm.common.block.machine.TileMachine;
import com.jdawg3636.icbm.common.item.ItemSignalDisruptor;
import com.jdawg3636.icbm.common.reg.ContainerReg;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class TileProximityDetector extends TileMachine implements ITickableTileEntity, ITileHasRadioFrequency {

    public static enum SlotIDs {
        BATTERY,
    }

    public static enum DetectionModes {
        PLAYERS("players"), MOBS("mobs"), BOTH("players_and_mobs");
        final TranslationTextComponent buttonLabel;
        DetectionModes(String buttonLabel) {
            this.buttonLabel = new TranslationTextComponent("gui." + ICBMReference.MODID + ".proximity_detector.detection_mode." + buttonLabel);
        }
    }

    // User-controlled parameters
    private double detectionRadius = ICBMReference.COMMON_CONFIG.getProximityDetectorRadiusMaximum();
    private int exclusionFrequency = 0;
    private DetectionModes detectionMode = DetectionModes.BOTH;

    public int currentSignal = 0;

    public TileProximityDetector(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn, ContainerReg.PROXIMITY_DETECTOR::get, ContainerProximityDetector::new, 1, 10_000, 50, 0, new ArrayList<>(), new TranslationTextComponent("gui." + ICBMReference.MODID + ".proximity_detector"));
    }

    @Override
    public void tick() {
        assert this.level != null;
        final BlockPos blockPos = getBlockPos();
        // Ingest energy from battery
        this.getEnergyStorageOfItemInSlot(SlotIDs.BATTERY.ordinal()).ifPresent(batteryEnergyStorage -> {
            tryReceiveEnergy(batteryEnergyStorage, batteryEnergyStorage.getEnergyStored());
        });
        // Calculate redstone signal
        int newSignal = 0;
        if(this.tryConsumeEnergy(5)) { // TODO: Make power usage configurable
            // Find potential targets based on detection mode and coarse radius
            List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, new AxisAlignedBB(blockPos).inflate(detectionRadius), entity -> {
                boolean targetIsValidForMode = !entity.isSpectator() && (
                        this.detectionMode == DetectionModes.BOTH ||
                        (this.detectionMode == DetectionModes.PLAYERS && entity instanceof PlayerEntity) ||
                        (this.detectionMode == DetectionModes.MOBS && !(entity instanceof PlayerEntity))
                );
                boolean signalIsNotBlocked = (!(entity instanceof PlayerEntity)) || !ItemSignalDisruptor.getFrequenciesDisruptedByPlayer((PlayerEntity)entity).contains(this.exclusionFrequency);
                return targetIsValidForMode && signalIsNotBlocked;
            });
            newSignal = targets.stream()
                    .map(target -> getBlockPos().distSqr(target.position(), true)) // Map target to square of distance
                    .min(Double::compare) // Find minimum distance (closest target). Returns Optional<Double> (no longer a Stream).
                    .map(Math::sqrt) // Take square root
                    .map(distance -> Math.max(0, distance - 0.86602540378)) // Reduce distance by sqrt(0.75) to account for the size of the block itself
                    .filter(distance -> distance <= detectionRadius) // Ensure target is within radius (spherical/Euclidean, as opposed to bounding box check).
                    .map(distance -> 15 * (1 - (distance / detectionRadius))) // Convert distance into a 0-15 signal
                    .map(floatSignal -> (int)Math.ceil(floatSignal)) // Round up and cast to int. Has the side effect of converting NaN to zero.
                    .orElse(0); // If no targets exist, signal strength is zero.
        }
        // Update Redstone Signal (if necessary)
        if(newSignal != currentSignal) {
            this.currentSignal = newSignal;
            level.blockUpdated(blockPos, level.getBlockState(blockPos).getBlock());
        }
    }

    @Override
    public boolean isInventoryItemValid(int slot, @Nonnull ItemStack stack) {
        if(slot == SlotIDs.BATTERY.ordinal()) {
            return stack.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::canExtract).orElse(false);
        }
        return false;
    }

    public int getSignal(BlockState blockState, IBlockReader level, BlockPos blockPos, Direction p_180656_4_) {
        return currentSignal;
    }

    @Override
    public void setRadioFrequency(int radioFrequency) {
        if(radioFrequency > 0) {
            this.exclusionFrequency = radioFrequency;
        }
    }

    @Override
    public int getRadioFrequency() {
        return this.exclusionFrequency;
    }

    public String getRadiusSliderText() {
        return "gui.icbm.proximity_detector.radius";
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        if(tag.contains("detection_radius")) {
            this.detectionRadius = tag.getDouble("detection_radius");
        }
        if(tag.contains("exclusion_frequency")) {
            this.exclusionFrequency = tag.getInt("exclusion_frequency");
        }
        if(tag.contains("detection_mode")) {
            final DetectionModes[] detectionModes = DetectionModes.values();
            this.detectionMode = detectionModes[tag.getInt("detection_mode") % detectionModes.length];
        }
        if(tag.contains("current_signal")) {
            this.currentSignal = tag.getInt("current_signal");
        }
        super.load(state, tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.putDouble("detection_radius", this.detectionRadius);
        tag.putInt("exclusion_frequency", this.exclusionFrequency);
        tag.putInt("detection_mode", this.detectionMode.ordinal());
        tag.putInt("current_signal", this.currentSignal);
        return super.save(tag);
    }

    public double getDetectionRadius() {
        return detectionRadius;
    }

    public void setDetectionRadius(double detectionRadius) {
        this.detectionRadius = MathHelper.clamp(detectionRadius, ICBMReference.COMMON_CONFIG.getProximityDetectorRadiusMinimum(), ICBMReference.COMMON_CONFIG.getProximityDetectorRadiusMaximum());
    }

    public int getExclusionFrequency() {
        return exclusionFrequency;
    }

    public void setExclusionFrequency(int exclusionFrequency) {
        this.exclusionFrequency = Math.max(0, exclusionFrequency);
    }

    public DetectionModes getDetectionMode() {
        return detectionMode;
    }

    public void setDetectionMode(int detectionMode) {
        DetectionModes[] possibleValues = DetectionModes.values();
        if(detectionMode < possibleValues.length) {
            this.detectionMode = possibleValues[detectionMode];
        }
    }

}
