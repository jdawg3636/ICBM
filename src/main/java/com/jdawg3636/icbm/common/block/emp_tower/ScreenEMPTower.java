package com.jdawg3636.icbm.common.block.emp_tower;

import com.jdawg3636.icbm.common.block.machine.ScreenMachine;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenEMPTower extends ScreenMachine<ContainerEMPTower> {

    public ScreenEMPTower(ContainerEMPTower container, PlayerInventory inventory, ITextComponent name) {
        super(container, inventory, name);
    }

}
