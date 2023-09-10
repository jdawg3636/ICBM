package com.jdawg3636.icbm.common.block.emp_tower;

import com.jdawg3636.icbm.common.block.machine.AbstractContainerMachine;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ContainerEMPTower extends AbstractContainerMachine {

    public static final int batterySlotX = 151;
    public static final int batterySlotY = 23;

    public ContainerEMPTower(@Nullable ContainerType<?> containerType, int windowId, World level, BlockPos blockPos, PlayerInventory playerInventory) {
        super(containerType, windowId, level, blockPos, playerInventory);
        addSlot(batterySlotX, batterySlotY);
        addPlayerInventorySlots(8, 135);
    }

}
