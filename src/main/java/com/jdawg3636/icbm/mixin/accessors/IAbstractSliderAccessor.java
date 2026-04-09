package com.jdawg3636.icbm.mixin.accessors;

import net.minecraft.client.gui.widget.AbstractSlider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractSlider.class)
public interface IAbstractSliderAccessor {
    @Invoker("setValue")
    void invokeSetValue(double value);
}
