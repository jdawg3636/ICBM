package com.jdawg3636.icbm.common.block.cruise_launcher;

import com.jdawg3636.icbm.common.block.launcher_platform.ContainerLauncherPlatform;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ContainerCruiseLauncher extends ContainerLauncherPlatform {

    public ContainerCruiseLauncher(@Nullable ContainerType<?> containerType, int windowId, World level, BlockPos blockPos, PlayerInventory playerInventory) {
        super(containerType, windowId, level, blockPos, playerInventory);
    }

    @Override
    public int getMissileSlotX() {
        return 151;
    }

    @Override
    public int getMissileSlotY() {
        return 23;
    }

}
