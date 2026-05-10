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
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;
import java.util.Optional;

public class ContainerMachine extends Container {

    private final Optional<TileMachine> tileMachineOptional;
    private final IItemHandler playerInventoryWrapper;
    private int nextSlotIndex = 0;
    boolean playerInventoryShown = false;

    @FunctionalInterface
    public static interface IConstructor {
        ContainerMachine construct(@Nullable ContainerType<?> containerType, int windowId, World level, BlockPos blockPos, PlayerInventory playerInventory);
    }

    public ContainerMachine(@Nullable ContainerType<?> containerType, int windowId, World level, BlockPos blockPos, PlayerInventory playerInventory, int playerInventorySlotsLeftCol, int playerInventorySlotsTopRow) {
        super(containerType, windowId);
        this.tileMachineOptional = Optional.ofNullable(level.getBlockEntity(blockPos)).filter(TileMachine.class::isInstance).map(TileMachine.class::cast);
        this.playerInventoryWrapper = new InvWrapper(playerInventory);
        this.tileMachineOptional.flatMap(TileMachine::getItemHandler).ifPresent(itemHandler -> {
            for (ICBMItemStackHandler.SlotMeta slotMeta : itemHandler.getSlotMetas()) {
                addSlot(new SlotItemHandler(itemHandler, nextSlotIndex++, slotMeta.xPos, slotMeta.yPos));
            }
        });
        this.addPlayerInventorySlots(playerInventorySlotsLeftCol, playerInventorySlotsTopRow);
    }

    public int getSlotCount() {
        return tileMachineOptional.flatMap(TileMachine::getItemHandler).map(itemHandler -> itemHandler.getSlotMetas().size()).orElse(0);
    }

    public ICBMItemStackHandler.SlotTag getSlotTag(int i) {
        return tileMachineOptional
                .flatMap(TileMachine::getItemHandler)
                .map(ICBMItemStackHandler::getSlotMetas)
                .filter(list -> list.size() > i)
                .map(list -> list.get(i).slotTag)
                .orElse(ICBMItemStackHandler.SlotTag.INVENTORY_PLAYER);
    }

    public TileMachine getBlockEntity() {
        return tileMachineOptional.orElse(null);
    }

    @Override
    public boolean stillValid(PlayerEntity playerEntity) {
        return tileMachineOptional.map(tileMachine -> {
            if(tileMachine.getLevel() == null) return false;
            return stillValid(IWorldPosCallable.create(tileMachine.getLevel(), tileMachine.getBlockPos()), playerEntity, tileMachine.getBlockState().getBlock());
        }).orElse(false);
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
