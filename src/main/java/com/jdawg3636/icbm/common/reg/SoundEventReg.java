package com.jdawg3636.icbm.common.reg;

import com.jdawg3636.icbm.ICBMReference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundEventReg {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ICBMReference.MODID);

    public static final RegistryObject<SoundEvent> EFFECT_AIRSTRIKE            = getSoundEventRegistryObject("effect_airstrike");
    public static final RegistryObject<SoundEvent> EFFECT_ALARM                = getSoundEventRegistryObject("effect_alarm");
    public static final RegistryObject<SoundEvent> EFFECT_BEAM_CHARGE          = getSoundEventRegistryObject("effect_beam_charge");
    public static final RegistryObject<SoundEvent> EFFECT_BEAM_DISCHARGE       = getSoundEventRegistryObject("effect_beam_discharge");
    public static final RegistryObject<SoundEvent> EFFECT_BLOCK_CRUMBLE        = getSoundEventRegistryObject("effect_block_crumble");
    public static final RegistryObject<SoundEvent> EFFECT_GAS_LEAK             = getSoundEventRegistryObject("effect_gas_leak");
    public static final RegistryObject<SoundEvent> EFFECT_MISSILE_FLIGHT       = getSoundEventRegistryObject("effect_missile_flight");
    public static final RegistryObject<SoundEvent> EFFECT_MISSILE_LAUNCH       = getSoundEventRegistryObject("effect_missile_launch");
    public static final RegistryObject<SoundEvent> EFFECT_REDMATTER            = getSoundEventRegistryObject("effect_redmatter");

    public static final RegistryObject<SoundEvent> EXPLOSION_ANTIGRAVITATIONAL = getSoundEventRegistryObject("explosion_antigravitational");
    public static final RegistryObject<SoundEvent> EXPLOSION_ANTIMATTER        = getSoundEventRegistryObject("explosion_antimatter");
    public static final RegistryObject<SoundEvent> EXPLOSION_DEBILITATION      = getSoundEventRegistryObject("explosion_debilitation");
    public static final RegistryObject<SoundEvent> EXPLOSION_EMP               = getSoundEventRegistryObject("explosion_emp");
    public static final RegistryObject<SoundEvent> EXPLOSION_GENERIC           = getSoundEventRegistryObject("explosion_generic");
    public static final RegistryObject<SoundEvent> EXPLOSION_HYPERSONIC        = getSoundEventRegistryObject("explosion_hypersonic");
    public static final RegistryObject<SoundEvent> EXPLOSION_INCENDIARY        = getSoundEventRegistryObject("explosion_incendiary");
    public static final RegistryObject<SoundEvent> EXPLOSION_SONIC             = getSoundEventRegistryObject("explosion_sonic");

    public static final RegistryObject<SoundEvent> GUI_BEEP                    = getSoundEventRegistryObject("gui_beep");
    public static final RegistryObject<SoundEvent> TILE_MACHINE_HUM            = getSoundEventRegistryObject("tile_machine_hum");

    public static RegistryObject<SoundEvent> getSoundEventRegistryObject(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(ICBMReference.MODID, name)));
    }

}
