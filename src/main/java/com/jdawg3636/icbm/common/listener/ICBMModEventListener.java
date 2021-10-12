package com.jdawg3636.icbm.common.listener;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static com.jdawg3636.icbm.ICBMReference.proxy;

/**
 * Handles Events from FMLJavaModLoadingContext.get().getModEventBus()
 */
public class ICBMModEventListener {

    @SubscribeEvent
    public static void onClientSetupEvent(final FMLClientSetupEvent event) {
        proxy.onClientSetupEvent(event);
    }

    @SubscribeEvent
    public static void onModelRegistryEvent(final ModelRegistryEvent event) {
        proxy.onModelRegistryEvent(event);
    }

    @SubscribeEvent
    public static void onParticleFactoryRegisterEvent(ParticleFactoryRegisterEvent event) {
        proxy.onParticleFactoryRegisterEvent(event);
    }

    @SubscribeEvent
    public static void onCommonSetupEvent(final FMLCommonSetupEvent event) {
        proxy.onCommonSetupEvent(event);
    }

}
