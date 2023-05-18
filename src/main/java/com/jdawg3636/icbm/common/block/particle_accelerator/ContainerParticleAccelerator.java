package com.jdawg3636.icbm.common.block.particle_accelerator;

import com.jdawg3636.icbm.common.block.machine.AbstractContainerMachine;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ContainerParticleAccelerator extends AbstractContainerMachine {

    public ContainerParticleAccelerator(@Nullable ContainerType<?> containerType, int windowId, World level, BlockPos blockPos, PlayerInventory playerInventory) {
        super(containerType, windowId, level, blockPos, playerInventory);
        addSlot(132, 24); // Input Matter
        addSlot(132, 51); // Empty Cells
        addSlot(152, 51); // Output Cells
        addPlayerInventorySlots(8, 84);
    }

}
