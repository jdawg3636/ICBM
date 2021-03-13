package com.jdawg3636.icbm.common.gui;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.container.ContainerLauncherPlatform;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ScreenLauncherPlatform extends ContainerScreen<ContainerLauncherPlatform> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(ICBMReference.MODID, "textures/gui/gui_launcher.png");

    public ScreenLauncherPlatform(ContainerLauncherPlatform container, PlayerInventory inventory, ITextComponent name) {
        super(container, inventory, name);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        // TODO make text dynamically centered for better localization support, fix alignment of center slot both here and in texture file
        // Using fontRenderer.func_243248_b() instead of ContainerScreen.drawString() - draws localized text without a shadow
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        fontRenderer.func_243248_b(matrixStack, new TranslationTextComponent("gui.launcherBase"), 46, 5, 0x8f8f8f);
        fontRenderer.func_243248_b(matrixStack, new TranslationTextComponent("gui.launcherBase.place"), 62, 32, 0x363636);
        fontRenderer.func_243248_b(matrixStack, new TranslationTextComponent("container.inventory"), 8, 72, 0x363636);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize);
    }

}
