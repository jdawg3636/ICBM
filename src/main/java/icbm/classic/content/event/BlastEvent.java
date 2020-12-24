package icbm.classic.content.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class BlastEvent extends Event {

    private final BlockPos blastPosition;
    private final World blastWorld;

    public BlastEvent(BlockPos blastPosition, World blastWorld) {
        this.blastPosition = blastPosition;
        this.blastWorld = blastWorld;
    }

    public BlockPos getBlastPosition() {
        return blastPosition;
    }

    public World getBlastWorld() {
        return blastWorld;
    }

    public static class Incendiary extends BlastEvent {
        public Incendiary(BlockPos blastPosition, World blastWorld) {
            super(blastPosition, blastWorld);
        }
    }

}
