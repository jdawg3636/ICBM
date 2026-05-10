package com.jdawg3636.icbm.common.block.machine;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ICBMItemStackHandler extends ItemStackHandler {

    public static enum SlotTag {

        BATTERY(true, 18*1, 18*6),
        FLUID_ITEM(true, 18*1, 18*7),
        INVENTORY_MACHINE(true),
        MISSILE(false),
        INVENTORY_PLAYER(true);

        public final int overlayU;
        public final int overlayV;
        public final boolean shouldRenderBorder;

        SlotTag(boolean shouldRenderBorder, int overlayU, int overlayV) {
            this.shouldRenderBorder = shouldRenderBorder;
            this.overlayU = overlayU;
            this.overlayV = overlayV;
        }

        SlotTag(boolean shouldRenderBorder) {
            this(shouldRenderBorder, 0, 0);
        }

    }

    public static class SlotMeta {

        public final int xPos;
        public final int yPos;
        public final SlotTag slotTag;

        private SlotMeta(int xPos, int yPos, SlotTag slotTag) {
            this.xPos = xPos;
            this.yPos = yPos;
            this.slotTag = slotTag;
        }

    }

    private boolean doCheckItemValidity = true;
    private boolean isBeingDestroyed = false;
    BiConsumer<Integer, Boolean> onInventorySlotChanged = (slot, isBeingDestroyed) -> {};
    BiFunction<Integer, ItemStack, Boolean> isInventoryItemValid = (slot, stack) -> true;

    private final ArrayList<SlotMeta> slotMetas = new ArrayList<>();

    public ICBMItemStackHandler(BiConsumer<Integer, Boolean> onInventorySlotChanged, BiFunction<Integer, ItemStack, Boolean> isInventoryItemValid) {
        super(0);
        this.onInventorySlotChanged = onInventorySlotChanged;
        this.isInventoryItemValid = isInventoryItemValid;
    }

    public void addSlot(int xPos, int yPos, SlotTag slotTag) {
        slotMetas.add(new SlotMeta(xPos, yPos, slotTag));
        this.setSize(slotMetas.size());
    }

    public ArrayList<SlotMeta> getSlotMetas() {
        return slotMetas;
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
