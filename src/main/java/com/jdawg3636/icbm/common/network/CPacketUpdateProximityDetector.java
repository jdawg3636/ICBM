package com.jdawg3636.icbm.common.network;

import com.jdawg3636.icbm.common.block.proximity_detector.TileProximityDetector;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketDirection;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * This is used for Client -> Server communication only.
 * Server -> Client communication uses vanilla {@link net.minecraft.network.play.server.SUpdateTileEntityPacket}s
 * handled in {@link com.jdawg3636.icbm.common.block.machine.TileMachine#onDataPacket}
 * via a server-side call from the block when it is interacted with.
 */
public class CPacketUpdateProximityDetector {

    public final BlockPos pos;
    public final int detectionMode;
    public final double detectionRadius;
    public final int exclusionFrequency;

    public CPacketUpdateProximityDetector(BlockPos pos, int detectionMode, double detectionRadius, int exclusionFrequency) {
        this.pos = pos;
        this.detectionMode = detectionMode;
        this.detectionRadius = detectionRadius;
        this.exclusionFrequency = exclusionFrequency;
    }

    public static void write(CPacketUpdateProximityDetector packet, PacketBuffer buffer) {
        buffer.writeBlockPos(packet.pos);
        buffer.writeInt(packet.detectionMode);
        buffer.writeDouble(packet.detectionRadius);
        buffer.writeInt(packet.exclusionFrequency);
    }

    public static CPacketUpdateProximityDetector read(PacketBuffer buffer) {
        return new CPacketUpdateProximityDetector(
            buffer.readBlockPos(),
            buffer.readInt(),
            buffer.readDouble(),
            buffer.readInt()
        );
    }

    public static void handle(CPacketUpdateProximityDetector packet, Supplier<NetworkEvent.Context> contextSupplier) {

        contextSupplier.get().enqueueWork(() -> {

            // Direction Check
            if(contextSupplier.get().getDirection() != NetworkDirection.PLAY_TO_SERVER || contextSupplier.get().getNetworkManager().getDirection() != PacketDirection.SERVERBOUND) return;

            // Get Sender and Check
            ServerPlayerEntity sender = contextSupplier.get().getSender();
            if(sender == null) return;
            if(sender.position().distanceToSqr(packet.pos.getX(), packet.pos.getY(), packet.pos.getZ()) > 36) return;

            // Get TileEntity and Check
            ServerWorld senderWorld = sender.getLevel();
            TileEntity tileEntity = senderWorld.getBlockEntity(packet.pos);
            if(!(tileEntity instanceof TileProximityDetector)) return;

            // If all checks passed, perform update
            // Note: setters may have their own checks
            ((TileProximityDetector) tileEntity).setDetectionMode(packet.detectionMode);
            ((TileProximityDetector) tileEntity).setDetectionRadius(packet.detectionRadius);
            ((TileProximityDetector) tileEntity).setExclusionFrequency(packet.exclusionFrequency);

        });

        contextSupplier.get().setPacketHandled(true);

    }

}
