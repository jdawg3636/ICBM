package icbm.common.network;

import icbm.common.event.BlastEvent;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketBlastEvent {

    private final BlockPos blastPosition;
    private final World blastWorld;

    public PacketBlastEvent(BlockPos blastPosition, World blastWorld) {
        this.blastPosition = blastPosition;
        this.blastWorld = blastWorld;
    }

    // Read
    public PacketBlastEvent(PacketBuffer buffer) {
        //TODO Figure out how to best encode dimension/world data
        blastPosition = buffer.readBlockPos();
        blastWorld = null;//World.CODEC.encode().orElse(RegistryKey.getOrCreateKey(Registry.WORLD_KEY, buffer.readResourceLocation()));
    }

    // Write
    public void toBytes(PacketBuffer buffer) {
        buffer.writeBlockPos(blastPosition);
        buffer.writeResourceLocation(blastWorld.getDimensionKey().getRegistryName());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> MinecraftForge.EVENT_BUS.post(new BlastEvent(blastPosition, blastWorld)));
        ctx.get().setPacketHandled(true);
    }

}
