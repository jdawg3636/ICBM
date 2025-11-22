package com.jdawg3636.icbm.common.block.proximity_detector;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.machine.TileMachine;
import com.jdawg3636.icbm.common.reg.ContainerReg;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
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

public class TileProximityDetector extends TileMachine implements ITickableTileEntity {

    public static enum SlotIDs {
        BATTERY,
    }

    public static enum DetectionModes {
        PLAYERS, MOBS, BOTH
    }

    // User-controlled parameters
    private double detectionRadius = ICBMReference.COMMON_CONFIG.getProximityDetectorRadiusMaximum();
    private int exclusionFrequency = 0;
    public DetectionModes detectionMode = DetectionModes.BOTH;

    public int currentSignal = 0;

    public TileProximityDetector(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn, ContainerReg.PROXIMITY_DETECTOR::get, ContainerProximityDetector::new, 1, 10_000, 50, 0, new ArrayList<>(), new TranslationTextComponent("gui." + ICBMReference.MODID + ".proximity_detector"));
    }

    @Override
    public void tick() {
        assert this.level != null;
        // Ingest energy from battery
        this.getEnergyStorageOfItemInSlot(SlotIDs.BATTERY.ordinal()).ifPresent(batteryEnergyStorage -> {
            tryReceiveEnergy(batteryEnergyStorage, batteryEnergyStorage.getEnergyStored());
        });
        // TODO Only output signal if sufficient power provided
        // TODO original mod had max radius of 30 blocks. Should probably make that a config.
        // TODO implement signal disruptor support
        // Calculate Redstone Signal
        final BlockPos blockPos = getBlockPos();
        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, new AxisAlignedBB(blockPos).inflate(detectionRadius), entity -> true);
        int newSignal = targets.stream()
                .map(target -> getBlockPos().distSqr(target.position(), true)) // Map target to square of distance
                .min(Double::compare) // Find minimum distance (closest target). Returns Optional<Double> (no longer a Stream).
                .map(Math::sqrt) // Take square root
                .filter(distance -> distance <= detectionRadius) // Ensure target is within radius (spherical/Euclidean, as opposed to bounding box check).
                .map(distance -> 15 * (1 - (distance / detectionRadius))) // Convert distance into a 0-15 signal
                .map(floatSignal -> (int)Math.ceil(floatSignal)) // Round up and cast to int
                .orElse(0); // If no targets exist, signal strength is zero.
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

}
