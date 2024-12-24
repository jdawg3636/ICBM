package com.jdawg3636.icbm.common.listener;

import com.jdawg3636.icbm.ICBMReference;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Handles Events from MinecraftForge.EVENT_BUS
 */
public class ICBMForgeEventListener {

    @SubscribeEvent
    public static void onAttachCapabilitiesEventWorld(final AttachCapabilitiesEvent<World> event) {
        ICBMReference.distProxy().onAttachCapabilitiesEventWorld(event);
    }

    @SubscribeEvent
    public static void onBiomeLoadingEvent(final BiomeLoadingEvent event) {
        ICBMReference.distProxy().onBiomeLoadingEvent(event);
    }

    @SubscribeEvent
    public static void onLivingDeathEvent(final LivingDeathEvent event) {
        ICBMReference.distProxy().onLivingDeathEvent(event);
    }

    @SubscribeEvent
    public static void onPlayerInteractEvent(final PlayerInteractEvent.EntityInteractSpecific event) {
        ICBMReference.distProxy().onPlayerInteractEvent(event);
    }

    @SubscribeEvent
    public static void onRegisterCommandsEvent(final RegisterCommandsEvent event) {
        ICBMReference.distProxy().onRegisterCommandsEvent(event);
    }

    @SubscribeEvent
    public static void onTickEvent(final TickEvent.WorldTickEvent event) {
        ICBMReference.distProxy().onTickEvent(event);
    }

}
