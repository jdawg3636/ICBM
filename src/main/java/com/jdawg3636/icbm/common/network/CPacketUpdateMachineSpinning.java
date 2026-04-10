package com.jdawg3636.icbm.common.network;

import com.jdawg3636.icbm.common.block.multiblock_spinning.TileMultiblockSpinning;
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
 * Shared packet used by both the EMP Tower and the Radar Station
 * This is used for Client -> Server communication only.
 * Server -> Client communication uses vanilla {@link net.minecraft.network.play.server.SUpdateTileEntityPacket}s
 * handled in {@link com.jdawg3636.icbm.common.block.machine.TileMachine#onDataPacket}
 * via a server-side call from the block when it is interacted with (in this case {@link com.jdawg3636.icbm.common.block.multiblock.AbstractBlockMultiTile#onUseMultiblock})
 */
public class CPacketUpdateMachineSpinning {

    public final BlockPos pos;
    public final double machineAffectedRadius;

    public CPacketUpdateMachineSpinning(BlockPos pos, double machineAffectedRadius) {
        this.pos = pos;
        this.machineAffectedRadius = machineAffectedRadius;
    }

    public static void write(CPacketUpdateMachineSpinning packet, PacketBuffer buffer) {
        buffer.writeBlockPos(packet.pos);
        buffer.writeDouble(packet.machineAffectedRadius);
    }

    public static CPacketUpdateMachineSpinning read(PacketBuffer buffer) {
        return new CPacketUpdateMachineSpinning(
            buffer.readBlockPos(),
            buffer.readDouble()
        );
    }

    public static void handle(CPacketUpdateMachineSpinning packet, Supplier<NetworkEvent.Context> contextSupplier) {

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
            if(!(tileEntity instanceof TileMultiblockSpinning)) return;

            // If all checks passed, perform update
            // Note: setter may have its own checks
            ((TileMultiblockSpinning) tileEntity).setMachineAffectedRadius(packet.machineAffectedRadius);

        });

        contextSupplier.get().setPacketHandled(true);

    }

}
