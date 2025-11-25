package com.jdawg3636.icbm.common.network;

import com.jdawg3636.icbm.common.item.ItemSignalDisruptor;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketDirection;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * This is used for Client -> Server communication only.
 */
public class CPacketUpdateSignalDisruptor {

    public final int slot;
    public final int radioFrequency;

    public CPacketUpdateSignalDisruptor(int slot, int radioFrequency) {
        this.slot = slot;
        this.radioFrequency = radioFrequency;
    }

    public static void write(CPacketUpdateSignalDisruptor packet, PacketBuffer buffer) {
        buffer.writeInt(packet.slot);
        buffer.writeInt(packet.radioFrequency);
    }

    public static CPacketUpdateSignalDisruptor read(PacketBuffer buffer) {
        return new CPacketUpdateSignalDisruptor(
                buffer.readInt(),
                buffer.readInt()
        );
    }

    public static void handle(CPacketUpdateSignalDisruptor packet, Supplier<NetworkEvent.Context> contextSupplier) {

        contextSupplier.get().enqueueWork(() -> {

            if(contextSupplier.get().getDirection() != NetworkDirection.PLAY_TO_SERVER || contextSupplier.get().getNetworkManager().getDirection() != PacketDirection.SERVERBOUND) return;

            ServerPlayerEntity sender = contextSupplier.get().getSender();

            if(sender != null) {
                if(packet.slot < 0) return;
                ItemStack itemStack = sender.inventory.getItem(packet.slot);
                // Setter will validate the frequency for us - no need to check here
                ItemSignalDisruptor.setFrequencyBeingDisrupted(itemStack, packet.radioFrequency);
            }

        });

        contextSupplier.get().setPacketHandled(true);

    }

}
