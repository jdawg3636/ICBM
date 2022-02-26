package com.jdawg3636.icbm.common.block.cruise_launcher;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.launcher_control_panel.IScreenLaunchControlPanel;
import com.jdawg3636.icbm.common.network.CPacketUpdateLauncherControlPanel;
import com.jdawg3636.icbm.common.network.ICBMNetworking;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ScreenCruiseLauncher extends ContainerScreen<ContainerCruiseLauncher> implements IScreenLaunchControlPanel {

    // TileEntity
    private final TileCruiseLauncher tileEntity;

    // Sizing Copied from ContainerScreen
    public final int imageWidth = 176;
    public final int imageHeight = 166;
    public final int widthTextField = (imageWidth / 2) - 14;

    // GUI
    public static final ResourceLocation TEXTURE                = new ResourceLocation(ICBMReference.MODID, "textures/gui/gui_cruise_launcher.png");
    public static final ITextComponent LABEL_TITLE              = new TranslationTextComponent("gui." + ICBMReference.MODID + ".cruise_launcher");
    public static final ITextComponent LABEL_TARGET_X           = new TranslationTextComponent("gui." + ICBMReference.MODID + ".cruise_launcher.target_x");
    public static final ITextComponent LABEL_TARGET_Z           = new TranslationTextComponent("gui." + ICBMReference.MODID + ".cruise_launcher.target_z");
    public static final ITextComponent LABEL_TARGET_Y           = new TranslationTextComponent("gui." + ICBMReference.MODID + ".cruise_launcher.target_y");
    public static final ITextComponent LABEL_RADIO_FREQUENCY    = new TranslationTextComponent("gui." + ICBMReference.MODID + ".cruise_launcher.radio_frequency");
    public TextFieldWidget textFieldTargetX;
    public TextFieldWidget textFieldTargetZ;
    public TextFieldWidget textFieldTargetY;
    public TextFieldWidget textFieldRadioFrequency;

    public ScreenCruiseLauncher(ContainerCruiseLauncher container, PlayerInventory inventory, ITextComponent name) {
        this(container, inventory, name, (TileCruiseLauncher)container.getBlockEntity());
    }

    public ScreenCruiseLauncher(ContainerCruiseLauncher container, PlayerInventory inventory, ITextComponent name, TileCruiseLauncher tileEntity) {
        super(container, inventory, name);
        this.tileEntity = tileEntity;
    }

    public static boolean stringIsNumeric(String in) {
        if(in.equals("")) return true;
        try { Double.parseDouble(in); } catch (Exception e) { return false; }
        return true;
    }

    @Override
    protected void init() {

        super.init();

        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);

        int verticalIncrement = 1;

        // Widget::new(x, y, width, height, message)
        this.textFieldTargetX        = new TextFieldWidget(this.font, this.width / 2 - 20, ((height - imageHeight) / 2) + 6 + (15 * verticalIncrement++), widthTextField, 12, new TranslationTextComponent("gui." + ICBMReference.MODID + ".cruise_launcher.target_x"));
        this.textFieldTargetZ        = new TextFieldWidget(this.font, this.width / 2 - 20, ((height - imageHeight) / 2) + 6 + (15 * verticalIncrement++), widthTextField, 12, new TranslationTextComponent("gui." + ICBMReference.MODID + ".cruise_launcher.target_z"));
        this.textFieldTargetY        = new TextFieldWidget(this.font, this.width / 2 - 20, ((height - imageHeight) / 2) + 6 + (15 * verticalIncrement++), widthTextField, 12, new TranslationTextComponent("gui." + ICBMReference.MODID + ".cruise_launcher.target_y"));
        this.textFieldRadioFrequency = new TextFieldWidget(this.font, this.width / 2 - 20, ((height - imageHeight) / 2) + 6 + (15 * verticalIncrement++), widthTextField, 12, new TranslationTextComponent("gui." + ICBMReference.MODID + ".cruise_launcher.radio_frequency"));

        textFieldTargetX.setFilter(ScreenCruiseLauncher::stringIsNumeric);
        textFieldTargetZ.setFilter(ScreenCruiseLauncher::stringIsNumeric);
        textFieldTargetY.setFilter(ScreenCruiseLauncher::stringIsNumeric);
        textFieldRadioFrequency.setFilter(ScreenCruiseLauncher::stringIsNumeric);

        this.textFieldTargetX.setMaxLength(32500);
        this.textFieldTargetX.setMaxLength(32500);
        this.textFieldTargetZ.setMaxLength(32500);
        this.textFieldTargetY.setMaxLength(32500);
        this.textFieldRadioFrequency.setMaxLength(32500);

        this.textFieldTargetX.setValue(String.valueOf(tileEntity.getTargetX()));
        this.textFieldTargetZ.setValue(String.valueOf(tileEntity.getTargetZ()));
        this.textFieldTargetY.setValue(String.valueOf(tileEntity.getTargetY()));
        this.textFieldRadioFrequency.setValue(String.valueOf(tileEntity.getRadioFrequency()));

        this.textFieldTargetX.moveCursorToStart();
        this.textFieldTargetZ.moveCursorToStart();
        this.textFieldTargetY.moveCursorToStart();
        this.textFieldRadioFrequency.moveCursorToStart();

        this.children.add(this.textFieldTargetX);
        this.children.add(this.textFieldTargetZ);
        this.children.add(this.textFieldTargetY);
        this.children.add(this.textFieldRadioFrequency);

    }

    public void updateGui() {
        this.textFieldTargetX.setValue(String.valueOf(tileEntity.getTargetX()));
        this.textFieldTargetZ.setValue(String.valueOf(tileEntity.getTargetZ()));
        this.textFieldTargetY.setValue(String.valueOf(tileEntity.getTargetY()));
        this.textFieldRadioFrequency.setValue(String.valueOf(tileEntity.getRadioFrequency()));
        this.textFieldTargetX.moveCursorToStart();
        this.textFieldTargetZ.moveCursorToStart();
        this.textFieldTargetY.moveCursorToStart();
        this.textFieldRadioFrequency.moveCursorToStart();
    }

    @Override
    public void resize(Minecraft p_231152_1_, int p_231152_2_, int p_231152_3_) {
        String s = this.textFieldTargetX.getValue();
        this.init(p_231152_1_, p_231152_2_, p_231152_3_);
        this.textFieldTargetX.setValue(s);
    }

    @Override
    public void onClose() {
        sendUpdatePacketToServer();
        super.onClose();
    }

    @Override
    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }

    @Override
    public void tick() {
        this.textFieldTargetX.tick();
    }

    public void sendUpdatePacketToServer() {

        BlockPos pos;
        int packetShouldUpdate = 0b1111;
        double packetTargetX = 0D;
        double packetTargetZ = 0D;
        double packetTargetY = 0D;
        int packetRadioFrequency = 0;

        if(tileEntity != null) pos = tileEntity.getBlockPos();
        else return;

        try { packetTargetX        = Double.parseDouble(textFieldTargetX.getValue()); } catch (Exception e) { packetShouldUpdate &= 0b1110; }
        try { packetTargetZ        = Double.parseDouble(textFieldTargetZ.getValue()); } catch (Exception e) { packetShouldUpdate &= 0b1101; }
        try { packetTargetY        = Double.parseDouble(textFieldTargetY.getValue()); } catch (Exception e) { packetShouldUpdate &= 0b1011; }
        try { packetRadioFrequency = Integer.parseInt(textFieldRadioFrequency.getValue()); } catch (Exception e) { packetShouldUpdate &= 0b0111; }

        ICBMNetworking.INSTANCE.sendToServer(new CPacketUpdateLauncherControlPanel(pos, packetShouldUpdate, packetTargetX, packetTargetZ, packetTargetY, packetRadioFrequency));

    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        renderGUITexture(matrixStack, mouseX, mouseY, partialTicks);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        drawCenteredStringNoShadow(matrixStack, this.font, LABEL_TITLE, 0, 5, 0x8f8f8f);
        int verticalIncrement = 1;
        drawRightAlignStringNoShadow(matrixStack, this.font, LABEL_TARGET_X,        -24, 8 + (15 * verticalIncrement++), 0x404040);
        drawRightAlignStringNoShadow(matrixStack, this.font, LABEL_TARGET_Z,        -24, 8 + (15 * verticalIncrement++), 0x404040);
        drawRightAlignStringNoShadow(matrixStack, this.font, LABEL_TARGET_Y,        -24, 8 + (15 * verticalIncrement++), 0x404040);
        drawRightAlignStringNoShadow(matrixStack, this.font, LABEL_RADIO_FREQUENCY, -24, 8 + (15 * verticalIncrement++), 0x404040);
        this.textFieldTargetX.render(matrixStack, mouseX, mouseY, partialTicks);
        this.textFieldTargetZ.render(matrixStack, mouseX, mouseY, partialTicks);
        this.textFieldTargetY.render(matrixStack, mouseX, mouseY, partialTicks);
        this.textFieldRadioFrequency.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    public void renderGUITexture(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(TEXTURE);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }

    public void drawCenteredStringNoShadow(MatrixStack matrixStack, FontRenderer fontRenderer, ITextComponent text, int posX, int posY, int color) {
        IReorderingProcessor ireorderingprocessor = text.getVisualOrderText();
        int relX = this.width / 2;
        int relY = (this.height - this.imageHeight) / 2;
        fontRenderer.draw(matrixStack, ireorderingprocessor, (float)(posX - fontRenderer.width(ireorderingprocessor) / 2) + relX, (float)posY + relY, color);
    }

    public void drawRightAlignStringNoShadow(MatrixStack matrixStack, FontRenderer fontRenderer, ITextComponent text, int posX, int posY, int color) {
        IReorderingProcessor ireorderingprocessor = text.getVisualOrderText();
        int relX = this.width / 2;
        int relY = (this.height - this.imageHeight) / 2;
        fontRenderer.draw(matrixStack, ireorderingprocessor, (float)(posX - fontRenderer.width(ireorderingprocessor)) + relX, (float)posY + relY, color);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        // TODO make text dynamically centered for better localization support, fix alignment of center slot both here and in texture file
        // Using fontRenderer.draw() instead of ContainerScreen.drawString() - draws localized text without a shadow
        FontRenderer fontRenderer = Minecraft.getInstance().font;
        fontRenderer.draw(matrixStack, new TranslationTextComponent("gui.icbm.cruise_launcher"), 46, 5, 0x8f8f8f);
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
