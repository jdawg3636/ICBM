package com.jdawg3636.icbm.common.block.launcher_platform;

import com.jdawg3636.icbm.ICBMReference;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenLauncherPlatform extends ContainerScreen<ContainerLauncherPlatform> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(ICBMReference.MODID, "textures/gui/gui_launcher.png");

    public ScreenLauncherPlatform(ContainerLauncherPlatform container, PlayerInventory inventory, ITextComponent name) {
        super(container, inventory, name);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        // TODO make text dynamically centered for better localization support, fix alignment of center slot both here and in texture file
        // Using fontRenderer.draw() instead of ContainerScreen.drawString() - draws localized text without a shadow
        FontRenderer fontRenderer = Minecraft.getInstance().font;
        fontRenderer.draw(matrixStack, new TranslationTextComponent("gui.icbm.launcher_platform"), 46, 5, 0x8f8f8f);
        fontRenderer.draw(matrixStack, new TranslationTextComponent("gui.icbm.launcher_platform.place"), 62, 32, 0x363636);
        fontRenderer.draw(matrixStack, new TranslationTextComponent("container.inventory"), 8, 72, 0x363636);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(TEXTURE);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }

}
