package com.jdawg3636.icbm.mixin.accessors;

import net.minecraft.client.particle.RisingParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RisingParticle.class)
public interface IRisingParticleAccessor {
    @Accessor("fallSpeed")
    void setFallSpeed(double value);
}
