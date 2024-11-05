package com.jdawg3636.icbm.common.listener;

import com.jdawg3636.icbm.ICBMReference;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Handles Events from FMLJavaModLoadingContext.get().getModEventBus()
 */
public class ICBMModEventListener {

    @SubscribeEvent
    public static void onClientSetupEvent(final FMLClientSetupEvent event) {
        ICBMReference.distProxy().onClientSetupEvent(event);
    }

    @SubscribeEvent
    public static void onModelRegistryEvent(final ModelRegistryEvent event) {
        ICBMReference.distProxy().onModelRegistryEvent(event);
    }

    @SubscribeEvent
    public static void onParticleFactoryRegisterEvent(ParticleFactoryRegisterEvent event) {
        ICBMReference.distProxy().onParticleFactoryRegisterEvent(event);
    }

    @SubscribeEvent
    public static void onCommonSetupEvent(final FMLCommonSetupEvent event) {
        ICBMReference.distProxy().onCommonSetupEvent(event);
    }

}
