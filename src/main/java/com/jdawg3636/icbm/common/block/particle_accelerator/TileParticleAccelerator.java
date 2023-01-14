package com.jdawg3636.icbm.common.block.particle_accelerator;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.capability.energystorage.ICBMEnergyStorage;
import com.jdawg3636.icbm.common.entity.EntityAcceleratingParticle;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class TileParticleAccelerator extends TileEntity implements ITickableTileEntity {
    public final ItemStackHandler itemHandler = new ItemStackHandler(3);
    private final ICBMEnergyStorage energyStorage = new ICBMEnergyStorage().setCapacity(1_000_000_000, true).setMaxExtract(0).setCallbackOnChanged(this::setChanged);
    public final LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.of(() -> itemHandler);
    public final LazyOptional<IEnergyStorage> energyStorageLazyOptional = LazyOptional.of(() -> energyStorage);
    public UUID particleEntityID = null;
    public boolean acceleratorIsActive = false;

    public TileParticleAccelerator(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    @Override
    public void tick() {
        if(level != null && !level.isClientSide()) {
            tickEnergy();
            // Most tick logic is handled within the entity itself, we just need to create it if one should exist but doesn't.
            EntityAcceleratingParticle particleEntity = (EntityAcceleratingParticle) (((ServerWorld) level).getEntity(particleEntityID));
            if(acceleratorIsActive && particleEntity == null) {
                particleEntity = EntityAcceleratingParticle.getNewInstanceForAccelerator(this);
                if(particleEntity != null) {
                    this.particleEntityID = particleEntity.getUUID();
                    level.addFreshEntity(particleEntity);
                }
                else {
                    ICBMReference.logger().warn("Failed to instantiate particle entity for particle accelerator!");
                }
            }
        }
    }

    public void tickEnergy() {

        // Assumes that side has already been checked - should only ever be called on the logical server
        assert level != null && !level.isClientSide();

        // If a redstone signal is present, try to consume Forge Energy to activate the accelerator.
        boolean redstoneSignalPresent = level.hasNeighborSignal(getBlockPos());
        if(redstoneSignalPresent) {
            int energyToIngest = ICBMReference.COMMON_CONFIG.getParticleAcceleratorEnergyUsagePerTick();
            int energyIngested = ingestEnergy(energyToIngest, Direction.values());
            acceleratorIsActive = energyIngested == energyToIngest;
        } else {
            acceleratorIsActive = false;
        }

    }

    public int ingestEnergy(int maxAmount, Direction[] directions) {
        if(level == null) return 0;
        int totalRequesting = maxAmount;
        for(int i = 0; totalRequesting > 0 && i < directions.length; ++i) {
            Direction direction = directions[i];
            TileEntity blockEntity = level.getBlockEntity(getBlockPos().relative(direction));
            if(blockEntity != null) {
                LazyOptional<IEnergyStorage> neighborCapOptional = blockEntity.getCapability(ICBMReference.FORGE_ENERGY_CAPABILITY, direction.getOpposite());
                if(neighborCapOptional.isPresent()) {
                    IEnergyStorage neighborCap = neighborCapOptional.orElse(null);
                    totalRequesting -= neighborCap.extractEnergy(totalRequesting, false);
                }
            }
        }
        return maxAmount - totalRequesting;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) return itemHandlerLazyOptional.cast();
        if(cap.equals(CapabilityEnergy.ENERGY)) return energyStorageLazyOptional.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        if(tag.contains("inv"))         itemHandler.deserializeNBT(tag.getCompound("inv"));
        if(tag.contains("energy"))      energyStorage.deserializeNBT(tag.getCompound("energy"));
        if(tag.contains("particle_id")) particleEntityID = tag.getUUID("particle_id");
        if(tag.contains("active"))      acceleratorIsActive = tag.getBoolean("active");
        super.load(state, tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.put("inv", itemHandler.serializeNBT());
        tag.put("energy", energyStorage.serializeNBT());
        tag.putUUID("particle_id", particleEntityID);
        tag.putBoolean("active", acceleratorIsActive);
        return super.save(tag);
    }

}