package com.jdawg3636.icbm.common.block.coal_generator;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.machine.ScreenMachine;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ScreenCoalGenerator extends ScreenMachine<ContainerCoalGenerator> {

    public static final ResourceLocation COAL_GENERATOR_BACKGROUND_TEXTURE = new ResourceLocation(ICBMReference.MODID, "textures/gui/gui_coal_generator.png");

    public ScreenCoalGenerator(ContainerCoalGenerator container, PlayerInventory inventory, ITextComponent name) {
        super(container, inventory, name, COAL_GENERATOR_BACKGROUND_TEXTURE, 352 / 2, 332 / 2);
    }

    public ScreenCoalGenerator(ContainerCoalGenerator container, PlayerInventory inventory, ITextComponent name, ResourceLocation backgroundTexture, int imageWidth, int imageHeight) {
        super(container, inventory, name, backgroundTexture, imageWidth, imageHeight);
    }

    @Override
    public void renderSlotBackgrounds(MatrixStack matrixStack, int relX, int relY) {
        super.renderSlotBackgrounds(matrixStack, relX, relY);
        // Render flame progress bar
        blit(matrixStack, relX + 80 - 1, relY + 34 - 1, 100, 18, 18*3, 18, 18, 256, 256);
    }

}
