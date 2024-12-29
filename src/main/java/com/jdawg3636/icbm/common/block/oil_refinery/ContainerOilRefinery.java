package com.jdawg3636.icbm.common.block.oil_refinery;

import com.jdawg3636.icbm.common.block.machine.AbstractContainerMachine;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ContainerOilRefinery extends AbstractContainerMachine {

    public ContainerOilRefinery(@Nullable ContainerType<?> containerType, int windowId, World level, BlockPos blockPos, PlayerInventory playerInventory) {
        super(containerType, windowId, level, blockPos, playerInventory);
        addSlot(80 - (2 * 18), 43 - 9, SlotTag.FLUID_ITEM);
        addSlot(80 - (1 * 18), 43 - 9, SlotTag.FLUID_ITEM);
        addSlot(80 + (2 * 18), 43 - 9, SlotTag.BATTERY);
        addPlayerInventorySlots(8, 135);
    }

}
