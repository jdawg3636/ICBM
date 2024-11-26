package com.jdawg3636.icbm.common.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.Util;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectEngineeredPathogenImmunity extends Effect {

    public EffectEngineeredPathogenImmunity() {
        this(EffectType.BENEFICIAL, 0xCEBCCF);
    }

    public EffectEngineeredPathogenImmunity(EffectType category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        for(EffectInstance effect : livingEntity.getActiveEffects()) {
            if(effect.getEffect().getCategory() == EffectType.HARMFUL) {
                livingEntity.removeEffect(effect.getEffect());
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public String getOrCreateDescriptionId() {
        return Util.makeDescriptionId("effect", ForgeRegistries.POTIONS.getKey(this));
    }

}
