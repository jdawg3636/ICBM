package com.jdawg3636.icbm.common.network;

import com.jdawg3636.icbm.common.block.launcher_control_panel.ITileLaunchControlPanel;
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
 * handled in {@link com.jdawg3636.icbm.common.block.launcher_control_panel.TileLauncherControlPanel#onDataPacket} (or
 * handled in {@link com.jdawg3636.icbm.common.block.machine.TileMachine#onDataPacket}, in the case of Cruise Launchers)
 * via a server-side call from the block when it is interacted with (in this case {@link com.jdawg3636.icbm.common.block.launcher_control_panel.BlockLauncherControlPanel#use}
 * or{@link com.jdawg3636.icbm.common.block.machine.AbstractBlockMachineTile#use})
 */
public class CPacketUpdateLauncherControlPanel {

    public final BlockPos pos;
    public final int shouldUpdate;
    public final double targetX;
    public final double targetZ;
    public final double targetY;
    public final int radioFrequency;

    public CPacketUpdateLauncherControlPanel(BlockPos pos, int shouldUpdate, double targetX, double targetZ, double targetY, int radioFrequency) {
        this.pos = pos;
        this.shouldUpdate = shouldUpdate;
        this.targetX = targetX;
        this.targetZ = targetZ;
        this.targetY = targetY;
        this.radioFrequency = radioFrequency;
    }

    public static void write(CPacketUpdateLauncherControlPanel packet, PacketBuffer buffer) {
        buffer.writeBlockPos(packet.pos);
        buffer.writeInt(packet.shouldUpdate);
        buffer.writeDouble(packet.targetX);
        buffer.writeDouble(packet.targetZ);
        buffer.writeDouble(packet.targetY);
        buffer.writeInt(packet.radioFrequency);
    }

    public static CPacketUpdateLauncherControlPanel read(PacketBuffer buffer) {
        return new CPacketUpdateLauncherControlPanel(
                buffer.readBlockPos(),
                buffer.readInt(),
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readInt()
        );
    }

    public static void handle(CPacketUpdateLauncherControlPanel packet, Supplier<NetworkEvent.Context> contextSupplier) {

        contextSupplier.get().enqueueWork(() -> {

            if(contextSupplier.get().getDirection() != NetworkDirection.PLAY_TO_SERVER || contextSupplier.get().getNetworkManager().getDirection() != PacketDirection.SERVERBOUND) return;

            ServerPlayerEntity sender = contextSupplier.get().getSender();

            if(sender != null) {
                ServerWorld senderWorld = sender.getLevel();
                TileEntity tileEntity = senderWorld.getBlockEntity(packet.pos);
                if(tileEntity instanceof ITileLaunchControlPanel && sender.position().distanceToSqr(packet.pos.getX(), packet.pos.getY(), packet.pos.getZ()) <=  36) {
                    if((packet.shouldUpdate & 0b0001) != 0) ((ITileLaunchControlPanel) tileEntity).setTargetX(packet.targetX);
                    if((packet.shouldUpdate & 0b0010) != 0) ((ITileLaunchControlPanel) tileEntity).setTargetZ(packet.targetZ);
                    if((packet.shouldUpdate & 0b0100) != 0) ((ITileLaunchControlPanel) tileEntity).setTargetY(packet.targetY);
                    if((packet.shouldUpdate & 0b1000) != 0) ((ITileLaunchControlPanel) tileEntity).setRadioFrequency(packet.radioFrequency);
                }
            }

        });

        contextSupplier.get().setPacketHandled(true);

    }

}
