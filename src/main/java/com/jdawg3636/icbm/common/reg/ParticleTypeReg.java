package com.jdawg3636.icbm.common.reg;

import com.jdawg3636.icbm.ICBMReference;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ParticleTypeReg {

    /*
     * Particle Registration is Weird and Terrible.
     * See com.jdawg3636.icbm.common.listener.ClientProxy::onParticleFactoryRegisterEvent for client-side registration that is far more informative.
     */

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ICBMReference.MODID);

    public static final RegistryObject<ParticleType<BasicParticleType>> ENDER_EFFECT = PARTICLES.register("ender", () -> new BasicParticleType(true));

    public static final RegistryObject<ParticleType<BasicParticleType>> SMOKE_DEBILITATION_A = PARTICLES.register("smoke_debilitation_a", () -> new BasicParticleType(true));
    public static final RegistryObject<ParticleType<BasicParticleType>> SMOKE_DEBILITATION_B = PARTICLES.register("smoke_debilitation_b", () -> new BasicParticleType(false));
    public static final RegistryObject<ParticleType<BasicParticleType>> SMOKE_DEBILITATION_C = PARTICLES.register("smoke_debilitation_c", () -> new BasicParticleType(true));
    public static final RegistryObject<ParticleType<BasicParticleType>> SMOKE_DEBILITATION_D = PARTICLES.register("smoke_debilitation_d", () -> new BasicParticleType(false));
    public static final RegistryObject<ParticleType<BasicParticleType>> SMOKE_DEBILITATION_E = PARTICLES.register("smoke_debilitation_e", () -> new BasicParticleType(true));

    public static final RegistryObject<ParticleType<BasicParticleType>> SMOKE_CHEMICAL_A = PARTICLES.register("smoke_chemical_a", () -> new BasicParticleType(true));
    public static final RegistryObject<ParticleType<BasicParticleType>> SMOKE_CHEMICAL_B = PARTICLES.register("smoke_chemical_b", () -> new BasicParticleType(false));
    public static final RegistryObject<ParticleType<BasicParticleType>> SMOKE_CHEMICAL_C = PARTICLES.register("smoke_chemical_c", () -> new BasicParticleType(true));
    public static final RegistryObject<ParticleType<BasicParticleType>> SMOKE_CHEMICAL_D = PARTICLES.register("smoke_chemical_d", () -> new BasicParticleType(false));
    public static final RegistryObject<ParticleType<BasicParticleType>> SMOKE_CHEMICAL_E = PARTICLES.register("smoke_chemical_e", () -> new BasicParticleType(true));

    public static final RegistryObject<ParticleType<BasicParticleType>> SMOKE_CONTAGION_A = PARTICLES.register("smoke_contagion_a", () -> new BasicParticleType(true));
    public static final RegistryObject<ParticleType<BasicParticleType>> SMOKE_CONTAGION_B = PARTICLES.register("smoke_contagion_b", () -> new BasicParticleType(false));
    public static final RegistryObject<ParticleType<BasicParticleType>> SMOKE_CONTAGION_C = PARTICLES.register("smoke_contagion_c", () -> new BasicParticleType(true));
    public static final RegistryObject<ParticleType<BasicParticleType>> SMOKE_CONTAGION_D = PARTICLES.register("smoke_contagion_d", () -> new BasicParticleType(false));
    public static final RegistryObject<ParticleType<BasicParticleType>> SMOKE_CONTAGION_E = PARTICLES.register("smoke_contagion_e", () -> new BasicParticleType(true));

    public static final RegistryObject<ParticleType<BasicParticleType>> RADIOACTIVE_EFFECT = PARTICLES.register("radioactive", () -> new BasicParticleType(false));

}
