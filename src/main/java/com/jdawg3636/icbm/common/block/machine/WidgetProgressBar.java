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

    private static final int FULL_BAR_TEXTURE_HEIGHT = 65;

    IScreenMachine screen;
    Supplier<Vector3f> colorSupplier;
    Supplier<Float> valueSupplier;
    Supplier<ITextComponent> tooltipSupplier;

    public WidgetProgressBar(int posX, int posY, int width, int height, IScreenMachine screen, Supplier<Vector3f> colorSupplier, Supplier<Float> valueSupplier, Supplier<ITextComponent> tooltipSupplier) {
        super(posX, posY, width, height, new StringTextComponent(ICBMReference.MODID + " progress bar widget"));
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
        blit(matrixStack, this.x - 1, this.y - 1, 100, 40,  0 * FULL_BAR_TEXTURE_HEIGHT, this.width, this.height, 256, 256);
        // Render Bar Contents
        Vector3f color = colorSupplier.get();
        int missingPixels = this.height - (int)(this.valueSupplier.get() * this.height);
        ScreenMachine.blitColored(matrixStack, this.x - 1, this.y - 1 + missingPixels, 100, 40, (1 * FULL_BAR_TEXTURE_HEIGHT) + missingPixels, this.width, this.height - missingPixels, 256, 256, color.x(), color.y(), color.z(), 1.0F);
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
