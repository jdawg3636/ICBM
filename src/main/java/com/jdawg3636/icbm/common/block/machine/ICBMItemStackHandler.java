package com.jdawg3636.icbm.common.block.machine;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ICBMItemStackHandler extends ItemStackHandler {

    private boolean doCheckItemValidity = true;
    private boolean isBeingDestroyed = false;
    BiConsumer<Integer, Boolean> onInventorySlotChanged = (slot, isBeingDestroyed) -> {};
    BiFunction<Integer, ItemStack, Boolean> isInventoryItemValid = (slot, stack) -> true;

    public ICBMItemStackHandler(int inventorySize) {
        super(inventorySize);
    }

    public ICBMItemStackHandler(int inventorySize, BiConsumer<Integer, Boolean> onInventorySlotChanged, BiFunction<Integer, ItemStack, Boolean> isInventoryItemValid) {
        this(inventorySize);
        this.onInventorySlotChanged = onInventorySlotChanged;
        this.isInventoryItemValid = isInventoryItemValid;
    }

    @Override
    protected void onContentsChanged(int slot) {
        onInventorySlotChanged.accept(slot, this.isBeingDestroyed);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return !doCheckItemValidity || isInventoryItemValid.apply(slot, stack);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return super.insertItem(slot, stack, simulate);
    }

    public ItemStack insertItemUnchecked(int slot, @Nonnull ItemStack stack, boolean simulate) {
        this.doCheckItemValidity = false;
        ItemStack result = this.insertItem(slot, stack, simulate);
        this.doCheckItemValidity = true;
        return result;
    }

    public void setBeingDestroyed() {
        this.isBeingDestroyed = true;
    }

}
