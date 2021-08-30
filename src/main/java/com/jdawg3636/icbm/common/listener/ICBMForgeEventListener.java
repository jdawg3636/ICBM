package com.jdawg3636.icbm.common.listener;

import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.jdawg3636.icbm.ICBMReference.proxy;

/**
 * Handles Events from MinecraftForge.EVENT_BUS
 */
public class ICBMForgeEventListener {

    @SubscribeEvent
    public static void onAttachCapabilitiesEventWorld(final AttachCapabilitiesEvent<World> event) {
        proxy.onAttachCapabilitiesEventWorld(event);
    }

    @SubscribeEvent
    public static void onBiomeLoadingEvent(final BiomeLoadingEvent event) {
        proxy.onBiomeLoadingEvent(event);
    }

    @SubscribeEvent
    public static void onTickEvent(final TickEvent.WorldTickEvent event) {
        proxy.onTickEvent(event);
    }

}
