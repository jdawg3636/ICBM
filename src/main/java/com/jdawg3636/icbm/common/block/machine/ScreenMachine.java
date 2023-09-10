package com.jdawg3636.icbm.common.block.machine;

import com.jdawg3636.icbm.ICBMReference;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ScreenMachine <T extends AbstractContainerMachine> extends ContainerScreen<T> {

    public static final ResourceLocation DEFAULT_CONTAINER_BACKGROUND_TEXTURE = new ResourceLocation(ICBMReference.MODID, "textures/gui/gui_container.png");

    public final ResourceLocation TEXTURE;

    public ScreenMachine(T container, PlayerInventory inventory, ITextComponent name) {
        this(container, inventory, name, DEFAULT_CONTAINER_BACKGROUND_TEXTURE, 352/2, 434/2);
    }

    /**
     * Note that slot positions are set by the container, not the screen.
     * @see AbstractContainerMachine#addPlayerInventorySlots
     * */
    public ScreenMachine(T container, PlayerInventory inventory, ITextComponent name, ResourceLocation backgroundTexture, int imageWidth, int imageHeight) {
        super(container, inventory, name);
        this.TEXTURE = backgroundTexture;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        drawHorizontallyCenteredStringNoShadow(matrixStack, this.font, this.title, 1, 5, ICBMReference.ICBMTextColors.LIGHT_GRAY.code);
        this.font.draw(matrixStack, this.inventory.getDisplayName(), (float)this.inventoryLabelX, (float)this.inventoryLabelY, ICBMReference.ICBMTextColors.DARK_GRAY.code);
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

    public void drawHorizontallyCenteredStringNoShadow(MatrixStack matrixStack, FontRenderer fontRenderer, ITextComponent text, int offsetX, int posY, int color) {
        IReorderingProcessor ireorderingprocessor = text.getVisualOrderText();
        int centerX = this.imageWidth / 2;
        fontRenderer.draw(matrixStack, ireorderingprocessor, (float)(centerX - fontRenderer.width(ireorderingprocessor) / 2) + offsetX, (float)posY, color);
    }

}
