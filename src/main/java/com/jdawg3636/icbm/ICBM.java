package com.jdawg3636.icbm;

import com.jdawg3636.icbm.common.listener.ICBMForgeEventListener;
import com.jdawg3636.icbm.common.listener.ICBMModEventListener;
import com.jdawg3636.icbm.common.reg.*;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryBuilder;

@Mod(ICBMReference.MODID)
public final class ICBM {

    public ICBM() {

        // Register Configs
        //registerConfigFile(ModConfig.Type.CLIENT, ICBMReference.CLIENT_CONFIG.spec);
        registerConfigFile(ModConfig.Type.COMMON, ICBMReference.COMMON_CONFIG.spec);
        //registerConfigFile(ModConfig.Type.SERVER, ICBMReference.SERVER_CONFIG.spec);

        // Register Event Listeners
        MinecraftForge.EVENT_BUS.register(ICBMForgeEventListener.class);
        FMLJavaModLoadingContext.get().getModEventBus().register(ICBMModEventListener.class);

        // Make Custom Registries
        BlastEventReg.BLAST_EVENTS.makeRegistry("blast_events", RegistryBuilder::new);
        BlastManagerThreadReg.BLAST_MANAGER_THREADS.makeRegistry("blast_manager_threads", RegistryBuilder::new);

        // Load Tag Reference Classes
        ICBMTags.makeSureThisClassIsLoaded();

        // Subscribe ICBM DeferredRegisters to RegistryEvent.NewRegistry events
        for(DeferredRegister<?> deferredRegister : getICBMDeferredRegisters()) {
            deferredRegister.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

    }

    public DeferredRegister<?>[] getICBMDeferredRegisters() {
        return new DeferredRegister[] {
                BlastEventReg.BLAST_EVENTS,
                BlastManagerThreadReg.BLAST_MANAGER_THREADS,
                BlockReg.BLOCKS,
                ContainerReg.CONTAINERS,
                EffectReg.EFFECTS,
                EntityReg.ENTITIES,
                FluidReg.FLUIDS,
                ItemReg.ITEMS,
                ParticleTypeReg.PARTICLES,
                RecipeSerializerReg.RECIPE_SERIALIZERS,
                SoundEventReg.SOUND_EVENTS,
                TileReg.TILES
        };
    }

    public static void registerConfigFile(ModConfig.Type type, ForgeConfigSpec spec) {
        ModLoadingContext.get().registerConfig(type, spec, String.format("%s-%s.toml", ICBMReference.CONFIG_FILE_PREFIX, type.extension()));
    }

}
