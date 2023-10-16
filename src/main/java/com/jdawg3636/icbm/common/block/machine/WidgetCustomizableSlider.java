package com.jdawg3636.icbm.common.block.machine;

import net.minecraft.client.gui.widget.AbstractSlider;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class WidgetCustomizableSlider extends AbstractSlider {

    public IFormattableTextComponent defaultMessage;
    public Consumer<WidgetCustomizableSlider> applyValueCallback;
    public Consumer<WidgetCustomizableSlider> updateMessageCallback;

    /**
     * Slider widget used in the vanilla UI (ex. render distance, volume, etc.), now with 100% more lambdas.
     * @param posX X-Position of the widget on screen
     * @param posY Y-Position of the widget on screen
     * @param width width of the widget on screen
     * @param height height of the widget on screen
     * @param message text displayed on the slider
     * @param value default value of the slider (range 0.0D to 1.0D)
     */
    public WidgetCustomizableSlider(int posX, int posY, int width, int height, IFormattableTextComponent message, double value, Consumer<WidgetCustomizableSlider> applyValueCallback, Consumer<WidgetCustomizableSlider> updateMessageCallback) {
        super(posX, posY, width, height, message, value);
        this.defaultMessage = message;
        this.updateMessageCallback = updateMessageCallback;
        this.applyValueCallback = applyValueCallback;
        updateMessage();
    }

    public WidgetCustomizableSlider(int posX, int posY, int width, int height, IFormattableTextComponent message, double value, Consumer<WidgetCustomizableSlider> applyValueCallback) {
        this(posX, posY, width, height, message, value, applyValueCallback, null);
    }

    public WidgetCustomizableSlider(int posX, int posY, int width, int height, IFormattableTextComponent message, double value) {
        this(posX, posY, width, height, message, value, null);
    }

    @Override
    public void applyValue() {
        if(applyValueCallback != null) {
            applyValueCallback.accept(this);
        }
    }

    @Override
    public void updateMessage() {
        if(updateMessageCallback == null) {
            ITextComponent sliderValueAsText = new StringTextComponent((int) (this.value * 100.0D) + "%");
            this.setMessage(defaultMessage.copy().append(": ").append(sliderValueAsText));
        } else {
            updateMessageCallback.accept(this);
        }
    }

    public double getValue() {
        return this.value;
    }

}
