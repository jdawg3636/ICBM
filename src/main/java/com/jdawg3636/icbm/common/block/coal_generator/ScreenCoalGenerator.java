package com.jdawg3636.icbm.common.block.coal_generator;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.machine.ScreenMachine;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ScreenCoalGenerator extends ScreenMachine<ContainerCoalGenerator> {

    public static final ResourceLocation COAL_GENERATOR_BACKGROUND_TEXTURE = new ResourceLocation(ICBMReference.MODID, "textures/gui/gui_coal_generator.png");

    public final TileCoalGenerator TILE_ENTITY;

    public ScreenCoalGenerator(ContainerCoalGenerator container, PlayerInventory inventory, ITextComponent name) {
        this(container, inventory, name, COAL_GENERATOR_BACKGROUND_TEXTURE, 352 / 2, 332 / 2);
    }

    public ScreenCoalGenerator(ContainerCoalGenerator container, PlayerInventory inventory, ITextComponent name, ResourceLocation backgroundTexture, int imageWidth, int imageHeight) {
        super(container, inventory, name, backgroundTexture, imageWidth, imageHeight);
        TILE_ENTITY = (TileCoalGenerator)super.TILE_ENTITY;
    }

    @Override
    public void renderSlotBackgrounds(MatrixStack matrixStack, int relX, int relY) {
        super.renderSlotBackgrounds(matrixStack, relX, relY);
        // Render flame progress bar
//        int spriteY = TILE_ENTITY.remainingBurnTicks > 0 ? 2 : 3;
//        blit(matrixStack, relX + 80 - 1, relY + 34 - 1, 100, 18, 18*spriteY, 18, 18, 256, 256);
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
