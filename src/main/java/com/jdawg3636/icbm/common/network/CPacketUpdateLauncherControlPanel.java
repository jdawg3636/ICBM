package com.jdawg3636.icbm.common.network;

import com.jdawg3636.icbm.common.block.launcher_control_panel.TileLauncherControlPanel;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

// Used for Client -> Server Communication
// Server -> Client is sent using a vanilla SUpdateTileEntityPacket handled in TileLauncherControlPanel::onDataPacket
public class CPacketUpdateLauncherControlPanel {

    public BlockPos pos;
    public int shouldUpdate;
    public double targetX;
    public double targetZ;
    public double targetY;
    public int radioFrequency;

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

            if(contextSupplier.get().getDirection() != NetworkDirection.PLAY_TO_SERVER) return;

            ServerPlayerEntity sender = contextSupplier.get().getSender();

            try {
                ServerWorld senderWorld = sender.getLevel();
                TileEntity tileEntity = senderWorld.getBlockEntity(packet.pos);
                if(tileEntity instanceof TileLauncherControlPanel && sender.position().distanceToSqr(packet.pos.getX(), packet.pos.getY(), packet.pos.getZ()) <=  36) {
                    if((packet.shouldUpdate & 0b0001) != 0) ((TileLauncherControlPanel) tileEntity).setTargetX(packet.targetX);
                    if((packet.shouldUpdate & 0b0010) != 0) ((TileLauncherControlPanel) tileEntity).setTargetZ(packet.targetZ);
                    if((packet.shouldUpdate & 0b0100) != 0) ((TileLauncherControlPanel) tileEntity).setTargetY(packet.targetY);
                    if((packet.shouldUpdate & 0b1000) != 0) ((TileLauncherControlPanel) tileEntity).setRadioFrequency(packet.radioFrequency);
                }
            } catch (NullPointerException ignored) {
                ignored.printStackTrace();
            }

        });

        contextSupplier.get().setPacketHandled(true);

    }

}
