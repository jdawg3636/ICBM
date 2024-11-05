package com.jdawg3636.icbm.common.block.launcher_platform;

import com.jdawg3636.icbm.common.block.machine.AbstractContainerMachine;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ContainerLauncherPlatform extends AbstractContainerMachine {

    public ContainerLauncherPlatform(@Nullable ContainerType<?> containerType, int windowId, World level, BlockPos blockPos, PlayerInventory playerInventory) {
        super(containerType, windowId, level, blockPos, playerInventory);
        addSlot(getMissileSlotX(), getMissileSlotY(), SlotTag.MISSILE);
        addPlayerInventorySlots(8, 84);
    }

    public int getMissileSlotX() {
        // TODO Fix horizontal alignment of center slot, both here and in texture file
        return 84;
    }

    public int getMissileSlotY() {
        return 47;
    }

}
