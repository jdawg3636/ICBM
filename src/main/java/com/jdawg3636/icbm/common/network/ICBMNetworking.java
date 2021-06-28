package com.jdawg3636.icbm.common.network;

import com.jdawg3636.icbm.ICBMReference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * https://mcforge.readthedocs.io/en/latest/networking/simpleimpl/
 */
public class ICBMNetworking {

    public static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE;

    public static void init() {

        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(ICBMReference.MODID, "main"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals
        );

        int packetID = 0;
        INSTANCE.messageBuilder(
                CPacketUpdateLauncherControlPanel.class, packetID++)
                .encoder(CPacketUpdateLauncherControlPanel::write)
                .decoder(CPacketUpdateLauncherControlPanel::read)
                .consumer(CPacketUpdateLauncherControlPanel::handle)
                .add();

    }

}
