package com.jdawg3636.icbm.common.item;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.launcher_control_panel.ScreenLauncherControlPanel;
import com.jdawg3636.icbm.common.network.CPacketUpdateItemFrequency;
import com.jdawg3636.icbm.common.network.ICBMNetworking;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenFrequencyItem extends Screen {

    public static final ITextComponent LABEL_RADIO_FREQUENCY = new TranslationTextComponent("gui." + ICBMReference.MODID + ".frequency_item.radio_frequency");
    public static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(ICBMReference.MODID, "textures/gui/mc_base_empty.png");
    public static final int IMAGE_WIDTH = 176;
    public static final int IMAGE_HEIGHT = 166;

    private final int slot;

    public TextFieldWidget textFieldRadioFrequency;

    public ScreenFrequencyItem(ITextComponent name, int slot) {
        super(name);
        this.slot = slot;
    }

    @Override
    protected void init() {

        assert this.minecraft != null;
        assert this.minecraft.player != null;
        ItemStack itemStack = this.minecraft.player.inventory.getItem(this.slot);
        int frequency = ItemFrequency.getFrequency(itemStack);

        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.textFieldRadioFrequency = new TextFieldWidget(this.font, this.width / 2 - 37, ((height - IMAGE_HEIGHT) / 2) + 76, 74, 12, new TranslationTextComponent("gui." + ICBMReference.MODID + ".signal_disruptor.radio_frequency"));
        this.textFieldRadioFrequency.setFilter(ScreenLauncherControlPanel::stringIsPositiveInteger);
        this.textFieldRadioFrequency.setMaxLength(32500);
        this.textFieldRadioFrequency.setValue(String.valueOf(frequency));
        this.textFieldRadioFrequency.moveCursorToStart();
        this.children.add(this.textFieldRadioFrequency);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        renderGUITexture(matrixStack);
        drawCenteredStringNoShadow(matrixStack, this.font, this.title, 0, 5, 0x404040);
        drawCenteredStringNoShadow(matrixStack, this.font, LABEL_RADIO_FREQUENCY, 0, 62, 0x404040);
        this.textFieldRadioFrequency.render(matrixStack, mouseX, mouseY, partialTicks);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    public void renderGUITexture(MatrixStack matrixStack) {
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
        this.minecraft.getTextureManager().bind(BACKGROUND_TEXTURE);
        int relX = (this.width - IMAGE_WIDTH) / 2;
        int relY = (this.height - IMAGE_HEIGHT) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    public void drawCenteredStringNoShadow(MatrixStack matrixStack, FontRenderer fontRenderer, ITextComponent text, int posX, int posY, int color) {
        IReorderingProcessor ireorderingprocessor = text.getVisualOrderText();
        int relX = this.width / 2;
        int relY = (this.height - IMAGE_HEIGHT) / 2;
        fontRenderer.draw(matrixStack, ireorderingprocessor, (float)(posX - fontRenderer.width(ireorderingprocessor) / 2) + relX, (float)posY + relY, color);
    }

    public void drawRightAlignStringNoShadow(MatrixStack matrixStack, FontRenderer fontRenderer, ITextComponent text, int posX, int posY, int color) {
        IReorderingProcessor ireorderingprocessor = text.getVisualOrderText();
        int relX = this.width / 2;
        int relY = (this.height - IMAGE_HEIGHT) / 2;
        fontRenderer.draw(matrixStack, ireorderingprocessor, (float)(posX - fontRenderer.width(ireorderingprocessor)) + relX, (float)posY + relY, color);
    }

    @Override
    public void onClose() {
        sendUpdatePacketToServer();
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void sendUpdatePacketToServer() {
        try {
            int frequency = Integer.parseInt(textFieldRadioFrequency.getValue());
            ICBMNetworking.INSTANCE.sendToServer(new CPacketUpdateItemFrequency(this.slot, frequency));
        } catch (NumberFormatException ignored) {}
    }

}
