package com.jdawg3636.icbm.common.block.machine;

import com.jdawg3636.icbm.ICBMReference;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenMachine <T extends AbstractContainerMachine> extends ContainerScreen<T> implements IScreenMachine {

    public static final ResourceLocation DEFAULT_COMPONENTS_TEXTURE = new ResourceLocation(ICBMReference.MODID, "textures/gui/gui_components.png");
    public static final ResourceLocation DEFAULT_CONTAINER_BACKGROUND_TEXTURE = new ResourceLocation(ICBMReference.MODID, "textures/gui/gui_container.png");

    public final TileMachine TILE_ENTITY;
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
        this.TILE_ENTITY = container.getBlockEntity();
        this.TEXTURE = backgroundTexture;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getImageWidth() {
        return this.imageWidth;
    }

    @Override
    public int getImageHeight() {
        return this.imageHeight;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack); // This is the blur/gradient behind the GUI, not the background texture. See "renderBg" for that.
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    public void renderSlotBackgrounds(MatrixStack matrixStack, int relX, int relY) {
        assert this.minecraft != null;
        this.minecraft.getTextureManager().bind(DEFAULT_COMPONENTS_TEXTURE);
        AbstractContainerMachine container = this.menu;
        for(int i = 0; i < container.slots.size(); ++i) {
            Slot slot = container.slots.get(i);
            AbstractContainerMachine.SlotTag slotTag = container.getSlotTag(i);
            if (slot.isActive() && slotTag.shouldRenderBorder) {
                renderSlotBackground(matrixStack, slot, slotTag, relX, relY);
            }
        }
    }

    public void renderSlotBackground(MatrixStack matrixStack, Slot slot, AbstractContainerMachine.SlotTag slotTag, int relX, int relY) {
        blit(matrixStack, relX + slot.x - 1, relY + slot.y - 1, 100, 0, 0, 18, 18, 256, 256);
        blit(matrixStack, relX + slot.x - 1, relY + slot.y - 1, 100, slotTag.overlayU, slotTag.overlayV, 18, 18, 256, 256);
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
        this.renderSlotBackgrounds(matrixStack, relX, relY);
    }

    public void drawHorizontallyCenteredStringNoShadow(MatrixStack matrixStack, FontRenderer fontRenderer, ITextComponent text, int offsetX, int posY, int color) {
        IReorderingProcessor ireorderingprocessor = text.getVisualOrderText();
        int centerX = this.imageWidth / 2;
        fontRenderer.draw(matrixStack, ireorderingprocessor, (float)(centerX - fontRenderer.width(ireorderingprocessor) / 2) + offsetX, (float)posY, color);
    }

    public static void blitColored(MatrixStack pMatrixStack, int pX, int pY, int pBlitOffset, float pUOffset, float pVOffset, int pUWidth, int pVHeight, int pTextureHeight, int pTextureWidth, float red, float green, float blue, float alpha) {
        innerBlitColored(pMatrixStack, pX, pX + pUWidth, pY, pY + pVHeight, pBlitOffset, pUWidth, pVHeight, pUOffset, pVOffset, pTextureWidth, pTextureHeight, red, green, blue, alpha);
    }

    private static void innerBlitColored(MatrixStack pMatrixStack, int pX1, int pX2, int pY1, int pY2, int pBlitOffset, int pUWidth, int pVHeight, float pUOffset, float pVOffset, int pTextureWidth, int pTextureHeight, float red, float green, float blue, float alpha) {
        innerBlitColoredTwo(pMatrixStack.last().pose(), pX1, pX2, pY1, pY2, pBlitOffset, (pUOffset + 0.0F) / (float)pTextureWidth, (pUOffset + (float)pUWidth) / (float)pTextureWidth, (pVOffset + 0.0F) / (float)pTextureHeight, (pVOffset + (float)pVHeight) / (float)pTextureHeight, red, green, blue, alpha);
    }

    private static void innerBlitColoredTwo(Matrix4f pMatrix, int pX1, int pX2, int pY1, int pY2, int pBlitOffset, float pMinU, float pMaxU, float pMinV, float pMaxV, float red, float green, float blue, float alpha) {
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX);
        bufferbuilder.vertex(pMatrix, (float)pX1, (float)pY2, (float)pBlitOffset).color(red, green, blue, alpha).uv(pMinU, pMaxV).overlayCoords(OverlayTexture.NO_OVERLAY).endVertex();
        bufferbuilder.vertex(pMatrix, (float)pX2, (float)pY2, (float)pBlitOffset).color(red, green, blue, alpha).uv(pMaxU, pMaxV).overlayCoords(OverlayTexture.NO_OVERLAY).endVertex();
        bufferbuilder.vertex(pMatrix, (float)pX2, (float)pY1, (float)pBlitOffset).color(red, green, blue, alpha).uv(pMaxU, pMinV).overlayCoords(OverlayTexture.NO_OVERLAY).endVertex();
        bufferbuilder.vertex(pMatrix, (float)pX1, (float)pY1, (float)pBlitOffset).color(red, green, blue, alpha).uv(pMinU, pMinV).overlayCoords(OverlayTexture.NO_OVERLAY).endVertex();
        bufferbuilder.end();
        RenderSystem.enableAlphaTest();
        WorldVertexBufferUploader.end(bufferbuilder);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        // The joys of client-side code weeeeeeeee
        assert this.minecraft != null;
        assert this.minecraft.player != null;
        // Call the superclass (AbstractContainerScreen) code
        super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        // Return value for superclass call isn't helpful (always returns true) - manually checking if it should have done anything
        if(this.clickedSlot == null && this.minecraft.player.inventory.getCarried().isEmpty()) {
            // If not then try to use normal behavior from grandparent class (should make other draggable widgets such as sliders function properly)
            return this.getFocused() != null && this.isDragging() && button == 0 && this.getFocused().mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
        // Superclass always returns true, so we'll do the same.
        return true;
    }

    @Override
    protected void init() {
        super.init();
        assert this.minecraft != null;
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
    }

    @Override
    public void removed() {
        assert this.minecraft != null;
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
        super.removed();
    }

    @Override
    public void onClose() {
        sendUpdatePacketToServer();
        super.onClose();
    }

    public void updateGui() {}

    @Override
    public void updateGui(BlockPos posOfTileEntity) {
        if(TILE_ENTITY.getPosOfTileEntity().equals(posOfTileEntity)) updateGui();
    }

    @Override
    public Minecraft deobf$getMinecraft() {
        return super.getMinecraft();
    }

    @Override
    public void deobf$renderTooltip(MatrixStack matrixStack, ITextComponent text, int mouseX, int mouseY) {
        super.renderTooltip(matrixStack, text, mouseX, mouseY);
    }

}
