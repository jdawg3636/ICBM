package com.jdawg3636.icbm.mixin.accessors;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface ILivingEntityAccessor {
    @Invoker("getDeathSound")
    SoundEvent invokeGetDeathSound();
    @Invoker("getSoundVolume")
    float invokeGetSoundVolume();
    @Invoker("getVoicePitch")
    float invokeGetVoicePitch();
}
