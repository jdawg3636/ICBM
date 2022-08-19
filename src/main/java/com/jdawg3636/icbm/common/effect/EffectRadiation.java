package com.jdawg3636.icbm.common.effect;

import com.jdawg3636.icbm.ICBMReference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectRadiation extends Effect {

    public static final float RADIATION_DAMAGE_PER_TICK = 1F;
    public static final DamageSource RADIATION_DAMAGE_SOURCE = (new DamageSource("icbm.radiation")).bypassArmor();
    public static final ResourceLocation RADIATION_PROTECTIVE_TAG = new ResourceLocation(ICBMReference.MODID, "radiation_protective");

    public EffectRadiation() {
        this(EffectType.HARMFUL, 0x9da629);
    }

    public EffectRadiation(EffectType category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        livingEntity.hurt(RADIATION_DAMAGE_SOURCE, RADIATION_DAMAGE_PER_TICK);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int j = 100 >> amplifier;
        if (j > 0) {
            return duration % j == 0;
        } else {
            return true;
        }
    }

    @Override
    public String getOrCreateDescriptionId() {
        return Util.makeDescriptionId("effect", ForgeRegistries.POTIONS.getKey(this));
    }

}
