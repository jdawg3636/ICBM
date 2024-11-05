package com.jdawg3636.icbm.common.network;

import com.jdawg3636.icbm.ICBMReference;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
        registerPacket(INSTANCE, CPacketUpdateLauncherControlPanel.class, packetID++);
        //noinspection UnusedAssignment
        registerPacket(INSTANCE, CPacketUpdateEMPTower.class, packetID++);

    }

    @SuppressWarnings("unchecked")
    public static <T> void registerPacket(SimpleChannel channel, Class<T> packetClass, int packetID) {
        try {
            final Method writeMethod = packetClass.getDeclaredMethod("write", packetClass, PacketBuffer.class);
            final Method readMethod = packetClass.getDeclaredMethod("read", PacketBuffer.class);
            final Method handleMethod = packetClass.getDeclaredMethod("handle", packetClass, Supplier.class);
            final BiConsumer<T, PacketBuffer> writeInvoker = (packet, buffer) -> { try { writeMethod.invoke(null, packet, buffer); } catch (Throwable t) { throw new RuntimeException(t); } };
            final Function<PacketBuffer, T> readInvoker = (packet) -> { try { return (T)readMethod.invoke(null, packet); } catch (Throwable t) { throw new RuntimeException(t); } };
            final BiConsumer<T, Supplier<NetworkEvent.Context>> handleInvoker = (packet, contextSupplier) -> { try { handleMethod.invoke(null, packet, contextSupplier); } catch (Throwable t) { throw new RuntimeException(t); } };
            channel.messageBuilder(packetClass, packetID).encoder(writeInvoker).decoder(readInvoker).consumer(handleInvoker).add();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

}
