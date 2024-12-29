package com.jdawg3636.icbm.common.block.oil_refinery;

import com.jdawg3636.icbm.common.block.machine.ScreenMachine;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ScreenOilRefinery extends ScreenMachine<ContainerOilRefinery> {

    public final TileOilRefinery TILE_ENTITY;

    public ScreenOilRefinery(ContainerOilRefinery container, PlayerInventory inventory, ITextComponent name) {
        super(container, inventory, name);
        TILE_ENTITY = (TileOilRefinery)super.TILE_ENTITY;
    }

    public ScreenOilRefinery(ContainerOilRefinery container, PlayerInventory inventory, ITextComponent name, ResourceLocation backgroundTexture, int imageWidth, int imageHeight) {
        super(container, inventory, name, backgroundTexture, imageWidth, imageHeight);
        TILE_ENTITY = (TileOilRefinery)super.TILE_ENTITY;
    }

    @Override
    public void renderSlotBackgrounds(MatrixStack matrixStack, int relX, int relY) {
        super.renderSlotBackgrounds(matrixStack, relX, relY);
        // Render flame progress bar
        blit(matrixStack, relX + 80 - 1, relY + 34 - 1, 100, 18, 18*3, 18, 18, 256, 256);
        final int flameHeight = 3 + (int)Math.ceil(13 * TILE_ENTITY.getPercentageFuelLeft());
        final int negFlameHeight = 18 - flameHeight;
        blit(
                matrixStack, // matrixStack
                relX + 80 - 1, // x
                relY + 34 - 1 + negFlameHeight, // y
                100, // blitOffset
                18, // uOffset
                18*2 + negFlameHeight, // vOffset
                18, // uWidth
                flameHeight, // vHeight
                256, // textureHeight
                256 // textureWidth
        );
    }

}
