package com.jdawg3636.icbm.common.block.machine;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class AbstractContainerMachine extends Container {

    public static enum SlotTag {
        BATTERY(true, 18*1, 18*6),
        FLUID_ITEM(true, 18*1, 18*7),
        INVENTORY_MACHINE,
        MISSILE(false),
        UNDECLARED;

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

        SlotTag() {
            this(true);
        }

    }

    public static final Map<Integer, SlotTag> slotTags = new HashMap<>();

    private final TileMachine tileEntity;
    private final IItemHandler playerInventoryWrapper;
    private int nextSlotIndex = 0;
    boolean playerInventoryShown = false;

    @FunctionalInterface
    public static interface IConstructor {
        AbstractContainerMachine construct(@Nullable ContainerType<?> containerType, int windowId, World level, BlockPos blockPos, PlayerInventory playerInventory);
    }

    public AbstractContainerMachine(@Nullable ContainerType<?> containerType, int windowId, World level, BlockPos blockPos, PlayerInventory playerInventory) {
        super(containerType, windowId);
        this.tileEntity = (TileMachine) level.getBlockEntity(blockPos);
        this.playerInventoryWrapper = new InvWrapper(playerInventory);
    }

    public void addSlot(int xPos, int yPos) {
        addSlot(xPos, yPos, SlotTag.UNDECLARED);
    }

    public void addSlot(int xPos, int yPos, SlotTag slotTag) {
        if(tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(
                (IItemHandler itemHandler) -> {
                    slotTags.put(nextSlotIndex, slotTag);
                    addSlot(new SlotItemHandler(itemHandler, nextSlotIndex++, xPos, yPos));
                }
            );
        }
    }

    public int getSlotCount() {
        return tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).map(IItemHandler::getSlots).orElse(-1);
    }

    public SlotTag getSlotTag(int i) {
        return slotTags.getOrDefault(i, SlotTag.UNDECLARED);
    }

    public TileMachine getBlockEntity() {
        return tileEntity;
    }

    @Override
    public boolean stillValid(PlayerEntity playerEntity) {
        if(tileEntity == null) return false;
        if(tileEntity.getLevel() == null) return false;
        return stillValid(IWorldPosCallable.create(tileEntity.getLevel(), tileEntity.getBlockPos()), playerEntity, tileEntity.getBlockState().getBlock());
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();
            // Moving Items From Inside
            if (index < getSlotCount()) {
                if (!this.moveItemStackTo(stack, 1, 36 + nextSlotIndex, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, itemstack);
            }
            // Moving Items From Outside
            else if (playerInventoryShown) {
                // Try to move into machine
                if (!this.moveItemStackTo(stack, 0, nextSlotIndex, false)) {
                    return ItemStack.EMPTY;
                }
                // Try to move from inventory to hotbar
                else if (index < 27 + nextSlotIndex) {
                    if (!this.moveItemStackTo(stack, 27 + nextSlotIndex, 36 + nextSlotIndex, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                // Try to move from hotbar to inventory
                else if (index < 36 + nextSlotIndex && !this.moveItemStackTo(stack, nextSlotIndex, 27 + nextSlotIndex, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stack);
        }

        return itemstack;
    }

    public int addSlotRange(IItemHandler handler, int index, int x, int y, int count, int dx) {
        for (int i = 0 ; i < count ; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    public void addSlotBox(IItemHandler handler, int index, int x, int y, int countHorizontal, int dx, int countVertical, int dy) {
        for (int j = 0 ; j < countVertical ; j++) {
            index = addSlotRange(handler, index, x, y, countHorizontal, dx);
            y += dy;
        }
    }

    public void addPlayerInventorySlots(int leftCol, int topRow) {
        // Set Flag
        playerInventoryShown = true;
        // Add Player inventory
        addSlotBox(playerInventoryWrapper, 9, leftCol, topRow, 9, 18, 3, 18);
        // Add Player Hotbar
        topRow += 58;
        addSlotRange(playerInventoryWrapper, 0, leftCol, topRow, 9, 18);
    }

}
