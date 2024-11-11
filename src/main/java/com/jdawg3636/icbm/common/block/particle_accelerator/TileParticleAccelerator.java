package com.jdawg3636.icbm.common.block.particle_accelerator;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.machine.ICBMItemStackHandler;
import com.jdawg3636.icbm.common.block.machine.TileMachine;
import com.jdawg3636.icbm.common.entity.EntityAcceleratingParticle;
import com.jdawg3636.icbm.common.reg.ContainerReg;
import com.jdawg3636.icbm.common.reg.ItemReg;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.UUID;

public class TileParticleAccelerator extends TileMachine implements ITickableTileEntity {

    public static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("gui.icbm.particle_accelerator");
    public UUID particleEntityID = null;
    public boolean acceleratorIsActive = false;

    public TileParticleAccelerator(TileEntityType<?> tileEntityType) {
        super(tileEntityType, ContainerReg.PARTICLE_ACCELERATOR::get, ContainerParticleAccelerator::new, 3, 1_000_000_000, 1_000_000_000, 0, DEFAULT_NAME);
    }

    public static enum SlotIDs {
        PARTICLES,
        EMPTY_CELLS,
        RESULT_CELLS
    }

    @Override
    public boolean isInventoryItemValid(int slot, @Nonnull ItemStack stack) {
        if(slot == SlotIDs.PARTICLES.ordinal()) {
            return stack.getItem() != ItemReg.EMPTY_CELL.get();
        }
        if(slot == SlotIDs.EMPTY_CELLS.ordinal()) {
            return stack.getItem() == ItemReg.EMPTY_CELL.get();
        }
        return slot != SlotIDs.RESULT_CELLS.ordinal();
    }

    @Override
    public void tick() {
        tickMachine();
        if(level != null && !level.isClientSide()) {
            // If a redstone signal is present, try to consume Forge Energy to activate the accelerator.
            if(redstoneSignalPresent()) {
                acceleratorIsActive = tryConsumeEnergy(ICBMReference.COMMON_CONFIG.getParticleAcceleratorEnergyUsagePerTick());
            } else {
                acceleratorIsActive = false;
            }
            // Most tick logic is handled within the entity itself, we just need to create a particle entity if one should exist but doesn't.
            EntityAcceleratingParticle particleEntity = (EntityAcceleratingParticle) (((ServerWorld) level).getEntity(particleEntityID));
            assert itemHandlerLazyOptional.isPresent();
            IItemHandler itemHandler = itemHandlerLazyOptional.orElse(null);
            if(acceleratorIsActive && particleEntity == null && itemHandler.extractItem(SlotIDs.PARTICLES.ordinal(), 1, true).getCount() != 0) {
                itemHandler.extractItem(SlotIDs.PARTICLES.ordinal(), 1, false);
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

    public boolean tryProduceResult(ItemStack result) {
        assert itemHandlerLazyOptional.isPresent();
        IItemHandler itemHandlerUncast = itemHandlerLazyOptional.orElse(null);
        assert itemHandlerUncast instanceof ICBMItemStackHandler;
        ICBMItemStackHandler itemHandler = (ICBMItemStackHandler)itemHandlerUncast;
        if(itemHandler.extractItem(SlotIDs.EMPTY_CELLS.ordinal(), 1, true).getItem() == ItemReg.EMPTY_CELL.get()) {
            itemHandler.extractItem(SlotIDs.EMPTY_CELLS.ordinal(), 1, false);
            return itemHandler.insertItemUnchecked(SlotIDs.RESULT_CELLS.ordinal(), result, false).getCount() == 0;
        }
        return false;
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        if(tag.contains("particle_id")) particleEntityID = tag.getUUID("particle_id");
        if(tag.contains("active"))      acceleratorIsActive = tag.getBoolean("active");
        super.load(state, tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        if(particleEntityID != null) tag.putUUID("particle_id", particleEntityID);
        tag.putBoolean("active", acceleratorIsActive);
        return super.save(tag);
    }

}