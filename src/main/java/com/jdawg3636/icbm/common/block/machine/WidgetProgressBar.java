package com.jdawg3636.icbm.common.block.machine;

import com.jdawg3636.icbm.ICBMReference;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class WidgetProgressBar extends Widget {

    boolean horizontal;
    IScreenMachine screen;
    Supplier<Vector3f> colorSupplier;
    Supplier<Float> valueSupplier;
    Supplier<ITextComponent> tooltipSupplier;

    int emptyBarU = 40;
    int emptyBarV = 0;
    int fullBarU = 40;
    int fullBarV = 65;

    public WidgetProgressBar(int posX, int posY, int width, int height, boolean horizontal, IScreenMachine screen, Supplier<Vector3f> colorSupplier, Supplier<Float> valueSupplier, Supplier<ITextComponent> tooltipSupplier) {
        super(posX, posY, width, height, new StringTextComponent(ICBMReference.MODID + " progress bar widget"));
        this.horizontal = horizontal;
        this.screen = screen;
        this.colorSupplier = colorSupplier;
        this.valueSupplier = valueSupplier;
        this.tooltipSupplier = tooltipSupplier;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        // Pulling from the gui_components.png texture
        this.screen.deobf$getMinecraft().getTextureManager().bind(ScreenMachine.DEFAULT_COMPONENTS_TEXTURE);
        // Render Empty Bar
        blit(matrixStack, this.x, this.y, getBlitOffset(), emptyBarU,  emptyBarV, this.width, this.height, 256, 256);
        // Render Bar Contents
        Vector3f color = colorSupplier.get();
        int missingPixels = this.height - (int)(this.valueSupplier.get() * this.height);
        ScreenMachine.blitColored(matrixStack, this.x, this.y + missingPixels, getBlitOffset(), fullBarU, fullBarV + missingPixels, this.width, this.height - missingPixels, 256, 256, color.x(), color.y(), color.z(), 1.0F);
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
