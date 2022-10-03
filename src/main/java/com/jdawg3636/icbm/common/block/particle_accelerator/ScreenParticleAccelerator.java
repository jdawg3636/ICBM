package com.jdawg3636.icbm.common.block.particle_accelerator;

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

public class ScreenParticleAccelerator extends ContainerScreen<ContainerParticleAccelerator> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(ICBMReference.MODID, "textures/gui/gui_particle_accelerator.png");

    public ScreenParticleAccelerator(ContainerParticleAccelerator container, PlayerInventory inventory, ITextComponent name) {
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
        // TODO: Get the title from the container instead of hardcoding - should be renamable using an anvil (super niche feature but no reason to not support it)
        fontRenderer.draw(matrixStack, new TranslationTextComponent("gui.icbm.particle_accelerator"), 46, 5, 0x8f8f8f);
        //fontRenderer.draw(matrixStack, new TranslationTextComponent("gui.icbm.launcher_platform.place"), 62, 32, 0x363636);
        fontRenderer.draw(matrixStack, new TranslationTextComponent("container.inventory"), 8, 72, 0x363636);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        /*
        Using RenderSystem::color4f despite deprecation
        From sciwhiz12 on April 17, 2021 in #modder-support-116 in The Forge Project discord (https://discord.com/channels/313125603924639766/725850371834118214/832995781510299688):
        "as best as we can know, [RenderSystem::color4f is] deprecated by Mojang because people shouldn't be calling the GL methods directly, and instead use the batched rendering
        system (the IRenderTypeBuffers, RenderType, etc) tho the one place where you can call them directly is during GUI rendering (because that's not batched), and is usually called
        as the first thing in their rendering methods to make sure the GL color state is clear (like if a previous GUI changed the state but didn't revert it)"
         */
        //noinspection deprecation
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        assert this.minecraft != null;
        this.minecraft.getTextureManager().bind(TEXTURE);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }

}
