package com.jdawg3636.icbm.common.block.launcher_control_panel;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.machine.IScreenMachine;
import com.jdawg3636.icbm.common.network.CPacketUpdateLauncherControlPanel;
import com.jdawg3636.icbm.common.network.ICBMNetworking;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ScreenLauncherControlPanel extends Screen implements IScreenMachine {

    // TileEntity
    private final TileLauncherControlPanel tileEntity;

    // Sizing Copied from ContainerScreen
    public final int imageWidth = 176;
    public final int imageHeight = 166;
    public final int widthTextField = (imageWidth / 2) - 14;

    // GUI
    public static final ResourceLocation TEXTURE = new ResourceLocation(ICBMReference.MODID, "textures/gui/gui_empty.png");
    public static final ITextComponent LABEL_TITLE              = new TranslationTextComponent("gui." + ICBMReference.MODID + ".launcher_control_panel");
    public static final ITextComponent LABEL_TARGET_X           = new TranslationTextComponent("gui." + ICBMReference.MODID + ".launcher_control_panel.target_x");
    public static final ITextComponent LABEL_TARGET_Z           = new TranslationTextComponent("gui." + ICBMReference.MODID + ".launcher_control_panel.target_z");
    public static final ITextComponent LABEL_TARGET_Y           = new TranslationTextComponent("gui." + ICBMReference.MODID + ".launcher_control_panel.target_y");
    public static final ITextComponent LABEL_RADIO_FREQUENCY    = new TranslationTextComponent("gui." + ICBMReference.MODID + ".launcher_control_panel.radio_frequency");
    public TextFieldWidget textFieldTargetX;
    public TextFieldWidget textFieldTargetZ;
    public TextFieldWidget textFieldTargetY;
    public TextFieldWidget textFieldRadioFrequency;

    public ScreenLauncherControlPanel(TileLauncherControlPanel tileEntity) {
        this(new TranslationTextComponent("gui." + ICBMReference.MODID + ".launcher_control_panel"), tileEntity);
    }

    public ScreenLauncherControlPanel(ITextComponent name, TileLauncherControlPanel tileEntity) {
        super(name);
        this.tileEntity = tileEntity;
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

    public static boolean stringIsNumeric(String in) {
        if(in.isEmpty()) return true;
        if(in.equals("-")) return true;
        try { Double.parseDouble(in); } catch (Exception e) { return false; }
        return true;
    }

    public static boolean stringIsPositiveInteger(String in) {
        if(in.isEmpty()) return true;
        try {
            int val = Integer.parseInt(in);
            if(val < 0) return false;
        } catch (Exception e) { return false; }
        return true;
    }

    @SuppressWarnings("UnusedAssignment")
    @Override
    protected void init() {

        assert this.minecraft != null;
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);

        int verticalIncrement = 1;

        // Widget::new(x, y, width, height, message)
        this.textFieldTargetX        = new TextFieldWidget(this.font, this.width / 2 + 7, ((height - imageHeight) / 2) + 20 + (15 * verticalIncrement++), widthTextField, 12, new TranslationTextComponent("gui." + ICBMReference.MODID + ".launcher_control_panel.target_x"));
        this.textFieldTargetZ        = new TextFieldWidget(this.font, this.width / 2 + 7, ((height - imageHeight) / 2) + 20 + (15 * verticalIncrement++), widthTextField, 12, new TranslationTextComponent("gui." + ICBMReference.MODID + ".launcher_control_panel.target_z"));
        verticalIncrement++;
        this.textFieldTargetY        = new TextFieldWidget(this.font, this.width / 2 + 7, ((height - imageHeight) / 2) + 20 + (15 * verticalIncrement++), widthTextField, 12, new TranslationTextComponent("gui." + ICBMReference.MODID + ".launcher_control_panel.target_y"));
        verticalIncrement++;
        this.textFieldRadioFrequency = new TextFieldWidget(this.font, this.width / 2 + 7, ((height - imageHeight) / 2) + 20 + (15 * verticalIncrement++), widthTextField, 12, new TranslationTextComponent("gui." + ICBMReference.MODID + ".launcher_control_panel.radio_frequency"));

        textFieldTargetX.setFilter(ScreenLauncherControlPanel::stringIsNumeric);
        textFieldTargetZ.setFilter(ScreenLauncherControlPanel::stringIsNumeric);
        textFieldTargetY.setFilter(ScreenLauncherControlPanel::stringIsNumeric);
        textFieldRadioFrequency.setFilter(ScreenLauncherControlPanel::stringIsPositiveInteger);

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

        if(!(tileEntity instanceof TileLauncherControlPanelT2)) {
            textFieldTargetY.setEditable(false);
        }
        if(!(tileEntity instanceof TileLauncherControlPanelT3)) {
            textFieldRadioFrequency.setEditable(false);
        }

        this.children.add(this.textFieldTargetX);
        this.children.add(this.textFieldTargetZ);
        this.children.add(this.textFieldTargetY);
        this.children.add(this.textFieldRadioFrequency);

    }

    @Override
    public void updateGui(BlockPos posOfTileEntity) {
        if(tileEntity.getBlockPos().equals(posOfTileEntity)) {
            this.textFieldTargetX.setValue(String.valueOf(tileEntity.getTargetX()));
            this.textFieldTargetZ.setValue(String.valueOf(tileEntity.getTargetZ()));
            this.textFieldTargetY.setValue(String.valueOf(tileEntity.getTargetY()));
            this.textFieldRadioFrequency.setValue(String.valueOf(tileEntity.getRadioFrequency()));
            this.textFieldTargetX.moveCursorToStart();
            this.textFieldTargetZ.moveCursorToStart();
            this.textFieldTargetY.moveCursorToStart();
            this.textFieldRadioFrequency.moveCursorToStart();
        }
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        String s = this.textFieldTargetX.getValue();
        this.init(minecraft, width, height);
        this.textFieldTargetX.setValue(s);
    }

    @Override
    public void onClose() {
        sendUpdatePacketToServer();
        super.onClose();
    }

    @Override
    public void removed() {
        assert this.minecraft != null;
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

    @SuppressWarnings("UnusedAssignment")
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        renderGUITexture(matrixStack, mouseX, mouseY, partialTicks);
        drawCenteredStringNoShadow(matrixStack, this.font, LABEL_TITLE, 0, 5, 0x8f8f8f);
        int verticalIncrement = 1;
        drawRightAlignStringNoShadow(matrixStack, this.font, LABEL_TARGET_X,        3, 22 + (15 * verticalIncrement++), 0x404040);
        drawRightAlignStringNoShadow(matrixStack, this.font, LABEL_TARGET_Z,        3, 22 + (15 * verticalIncrement++), 0x404040);
        verticalIncrement++;
        drawRightAlignStringNoShadow(matrixStack, this.font, LABEL_TARGET_Y,        3, 22 + (15 * verticalIncrement++), 0x404040);
        verticalIncrement++;
        drawRightAlignStringNoShadow(matrixStack, this.font, LABEL_RADIO_FREQUENCY, 3, 22 + (15 * verticalIncrement++), 0x404040);
        this.textFieldTargetX.render(matrixStack, mouseX, mouseY, partialTicks);
        this.textFieldTargetZ.render(matrixStack, mouseX, mouseY, partialTicks);
        this.textFieldTargetY.render(matrixStack, mouseX, mouseY, partialTicks);
        this.textFieldRadioFrequency.render(matrixStack, mouseX, mouseY, partialTicks);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @SuppressWarnings("unused")
    public void renderGUITexture(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
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
    public Minecraft deobf$getMinecraft() {
        return super.getMinecraft();
    }

    @Override
    public void deobf$renderTooltip(MatrixStack matrixStack, ITextComponent text, int mouseX, int mouseY) {
        super.renderTooltip(matrixStack, text, mouseX, mouseY);
    }

}
