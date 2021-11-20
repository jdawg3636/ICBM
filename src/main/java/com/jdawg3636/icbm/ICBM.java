package com.jdawg3636.icbm;

import com.jdawg3636.icbm.common.listener.ICBMForgeEventListener;
import com.jdawg3636.icbm.common.listener.ICBMModEventListener;
import com.jdawg3636.icbm.common.reg.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryBuilder;

@Mod(ICBMReference.MODID)
public final class ICBM {

    public ICBM() {

        // Register Event Listeners
        MinecraftForge.EVENT_BUS.register(ICBMForgeEventListener.class);
        FMLJavaModLoadingContext.get().getModEventBus().register(ICBMModEventListener.class);

        // Make Custom Registries
        BlastManagerThreadReg.BLAST_MANAGER_THREADS.makeRegistry("blast_manager_threads", RegistryBuilder::new);

        // Subscribe ICBM DeferredRegisters to RegistryEvent.NewRegistry events
        for(DeferredRegister<?> deferredRegister : getICBMDeferredRegisters()) {
            deferredRegister.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

    }

    public DeferredRegister<?>[] getICBMDeferredRegisters() {
        return new DeferredRegister[] {
                BlastManagerThreadReg.BLAST_MANAGER_THREADS,
                BlockReg.BLOCKS,
                ContainerReg.CONTAINERS,
                EntityReg.ENTITIES,
                ItemReg.ITEMS,
                ParticleTypeReg.PARTICLES,
                SoundEventReg.SOUND_EVENTS,
                TileReg.TILES
        };
    }

}
