package com.jdawg3636.icbm.common.reg;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.effect.EffectEngineeredPathogen;
import com.jdawg3636.icbm.common.effect.EffectEngineeredPathogenImmunity;
import com.jdawg3636.icbm.common.effect.EffectRadiation;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectReg {

    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, ICBMReference.MODID);

    public static final RegistryObject<Effect> ENGINEERED_PATHOGEN = EFFECTS.register("engineered_pathogen", EffectEngineeredPathogen::new);
    public static final RegistryObject<Effect> ENGINEERED_PATHOGEN_IMMUNITY = EFFECTS.register("engineered_pathogen_immunity", EffectEngineeredPathogenImmunity::new);
    public static final RegistryObject<Effect> RADIATION = EFFECTS.register("radiation", EffectRadiation::new);

}
