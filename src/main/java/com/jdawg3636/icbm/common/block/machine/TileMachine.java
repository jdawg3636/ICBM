package com.jdawg3636.icbm.common.block.machine;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.capability.energystorage.ICBMEnergyStorage;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileMachine extends TileEntity {

    private final ItemStackHandler itemHandler;
    public final LazyOptional<IItemHandler> itemHandlerLazyOptional;
    private final ICBMEnergyStorage energyStorage;
    public final LazyOptional<IEnergyStorage> energyStorageLazyOptional;

    public TileMachine(TileEntityType<?> tileEntityTypeIn) {
        this(tileEntityTypeIn, 0, 0, 0, 0);
    }

    public TileMachine(TileEntityType<?> tileEntityTypeIn, int inventorySize, int forgeEnergyCapacity, int forgeEnergyPerTickIn, int forgeEnergyPerTickOut) {
        super(tileEntityTypeIn);
        if(inventorySize > 0) {
            itemHandler = createHandler(inventorySize);
            itemHandlerLazyOptional = LazyOptional.of(() -> itemHandler);
        }
        else {
            itemHandler = null;
            itemHandlerLazyOptional = LazyOptional.empty();
        }
        this.energyStorage = new ICBMEnergyStorage().setCapacity(forgeEnergyCapacity, false).setMaxReceive(forgeEnergyPerTickIn).setMaxExtract(forgeEnergyPerTickOut).setCallbackOnChanged(this::setChanged);
        this.energyStorageLazyOptional = LazyOptional.of(() -> energyStorage);
    }

    /*
    * Note! This must be called by the block. This is done by default in AbstractBlockMachineTile.
    * */
    public void onBlockDestroyed() {
        if(level != null) {
            for(int i = 0; i < itemHandler.getSlots(); ++i) {
                ItemStack contents = itemHandler.getStackInSlot(0);
                itemHandler.setStackInSlot(0, ItemStack.EMPTY);
                InventoryHelper.dropItemStack(level, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), contents);
            }
        }
    }

    protected void onInventorySlotChanged(int slot) {
        setChanged();
    }

    public boolean isInventoryItemValid(int slot, ItemStack stack) {
        return true;
    }

    private ItemStackHandler createHandler(int inventorySize) {
        return new ItemStackHandler(inventorySize) {

            @Override
            protected void onContentsChanged(int slot) {
                onInventorySlotChanged(slot);
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return isInventoryItemValid(slot, stack);
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if(!isItemValid(slot, stack)) return stack;
                return super.insertItem(slot, stack, simulate);
            }

        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(itemHandlerLazyOptional.isPresent() && cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) return itemHandlerLazyOptional.cast();
        if(energyStorageLazyOptional.isPresent() && cap.equals(CapabilityEnergy.ENERGY)) return energyStorageLazyOptional.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        if(tag.contains("inv") && itemHandlerLazyOptional.isPresent()) itemHandler.deserializeNBT(tag.getCompound("inv"));
        if(tag.contains("energy") && energyStorageLazyOptional.isPresent()) energyStorage.deserializeNBT(tag.getCompound("energy"));
        super.load(state, tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        if(itemHandlerLazyOptional.isPresent()) tag.put("inv", itemHandler.serializeNBT());
        if(energyStorageLazyOptional.isPresent()) tag.put("energy", energyStorage.serializeNBT());
        return super.save(tag);
    }

    @Override
    public double getViewDistance() {
        return ICBMReference.distProxy().getTileEntityUpdateDistance();
    }

}
