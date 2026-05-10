package com.jdawg3636.icbm.common.block.particle_accelerator;

import com.jdawg3636.icbm.common.block.machine.ContainerMachine;
import com.jdawg3636.icbm.common.block.machine.ScreenMachine;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ScreenParticleAccelerator extends ScreenMachine<ContainerMachine> {

    public ScreenParticleAccelerator(ContainerMachine container, PlayerInventory inventory, ITextComponent name) {
        super(container, inventory, name);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        super.renderLabels(matrixStack, mouseX, mouseY);
        String stringSpeedPercentage = String.format("%.2f", ((TileParticleAccelerator)TILE_ENTITY).displayedSpeedPercentage * 100);
        this.font.draw(matrixStack, new TranslationTextComponent("gui.icbm.particle_accelerator.current_speed", stringSpeedPercentage), 8, 62+51, 0x363636);
    }

}
