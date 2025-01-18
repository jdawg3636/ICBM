package com.jdawg3636.icbm.common.block.machine;

import com.jdawg3636.icbm.ICBMReference;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.function.Supplier;

public class WidgetFluidTank extends Widget {

    IScreenMachine screen;
    Supplier<ITextComponent> tooltipSupplier;
    LazyOptional<FluidTank> fluidTankLazyOptional;

    ResourceLocation tankOverlayTexture = ScreenMachine.DEFAULT_COMPONENTS_TEXTURE;
    int tankOverlayU = 58;
    int tankOverlayV = 0;

    public WidgetFluidTank(int posX, int posY, int width, int height, IScreenMachine screen, Supplier<ITextComponent> tooltipSupplier, LazyOptional<FluidTank> fluidTankLazyOptional) {
        super(posX, posY, width, height, new StringTextComponent(ICBMReference.MODID + " fluid tank widget"));
        this.screen = screen;
        this.tooltipSupplier = tooltipSupplier;
        this.fluidTankLazyOptional = fluidTankLazyOptional;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

        // Render Fluid Texture
        this.screen.deobf$getMinecraft().getTextureManager().bind(AtlasTexture.LOCATION_BLOCKS);
        fluidTankLazyOptional.ifPresent(fluidTank -> {
            FluidStack fluidStack = fluidTank.getFluid();
            if (!fluidStack.isEmpty()) {

                // Extract fluid texture coordinates and color
                TextureAtlasSprite textureAtlasSprite = Minecraft.getInstance().getTextureAtlas(AtlasTexture.LOCATION_BLOCKS).apply(fluidStack.getFluid().getAttributes().getStillTexture());
                int colorPacked = fluidStack.getFluid().getAttributes().getColor(fluidStack);
                float colorAlpha = 1.0f; // Always opaque
                float colorRed   = (colorPacked >> 16 & 0xFF) / 255.0f;
                float colorGreen = (colorPacked >>  8 & 0xFF) / 255.0f;
                float colorBlue  = (colorPacked >>  0 & 0xFF) / 255.0f;

                // Calculate Fluid Area
                final double fractionFilled = Math.min(1.0D, fluidStack.getAmount() / (double)fluidTank.getCapacity());
                final int fluidHeightPixels = (int) Math.ceil((this.height - 4) * fractionFilled);
                int yBottom = this.y + this.height - 2;

                // Draw fluid texture (looping to draw in slices so the texture tiles instead of stretching/cropping)
                for (int xTilingOffset = 0; xTilingOffset < width; xTilingOffset += 16) {
                    final int sliceWidth = Math.min(16, width - xTilingOffset);
                    final float u0 = textureAtlasSprite.getU0();
                    float u1 = textureAtlasSprite.getU1();
                    u1 = u0 + ((u1 - u0) * (sliceWidth / 16F));
                    for (int yTilingOffset = 0; yTilingOffset < fluidHeightPixels; yTilingOffset += 16) {
                        final int sliceHeight = Math.min(16, fluidHeightPixels - yTilingOffset);
                        float v0 = textureAtlasSprite.getV0();
                        final float v1 = textureAtlasSprite.getV1();
                        v0 = v0 + ((v1 - v0) * (1F - (sliceHeight / 16F)));
                        ScreenMachine.innerBlitColored(matrixStack.last().pose(), this.x + xTilingOffset, this.x + xTilingOffset + sliceWidth, yBottom - yTilingOffset - sliceHeight, yBottom - yTilingOffset, this.getBlitOffset(), u0, u1, v0, v1, colorRed, colorGreen, colorBlue, colorAlpha);
                    }
                }

            }
        });

        // Render Tank Overlay
        this.screen.deobf$getMinecraft().getTextureManager().bind(tankOverlayTexture);
        blit(matrixStack, this.x, this.y, getBlitOffset(), tankOverlayU,  tankOverlayV, this.width, this.height, 256, 256);

        // Render Tooltip
        if(this.isHovered()) {
            this.renderToolTip(matrixStack, mouseX, mouseY);
        }

    }

    @Override
    public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
        screen.deobf$renderTooltip(matrixStack, tooltipSupplier.get(), mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        return false;
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        return false;
    }

}
