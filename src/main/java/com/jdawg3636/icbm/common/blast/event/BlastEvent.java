package com.jdawg3636.icbm.common.blast.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * Fired whenever an ICBM explosive is detonated
 * Server-Side Only
 * Handled in com.jdawg3636.icbm.common.event.ICBMEvents
 */
@Cancelable
public class BlastEvent extends Event {

    public enum Type {
        EXPLOSIVES,
        EXPLOSIVES_MINECART,
        PLATFORM_MISSILE,
        CRUISE_MISSILE,
        HANDHELD_ROCKET,
        GRENADE
    }

    private final BlockPos blastPosition;
    private final ServerWorld blastWorld;
    private final BlastEvent.Type blastType;

    public BlastEvent(BlockPos blastPosition, ServerWorld blastWorld, BlastEvent.Type blastType) {
        this.blastPosition = blastPosition;
        this.blastWorld = blastWorld;
        this.blastType = blastType;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    public BlockPos getBlastPosition() {
        return blastPosition;
    }

    public ServerWorld getBlastWorld() {
        return blastWorld;
    }

    public BlastEvent.Type getBlastType() {
        return blastType;
    }

    public interface BlastEventProvider {
        BlastEvent getBlastEvent(BlockPos blastPosition, ServerWorld blastWorld, BlastEvent.Type blastType);
    }

    public static class Condensed extends BlastEvent {
        public Condensed(BlockPos blastPosition, ServerWorld blastWorld, BlastEvent.Type blastType) {
            super(blastPosition, blastWorld, blastType);
        }
    }
    public static class Shrapnel extends BlastEvent {
        public Shrapnel(BlockPos blastPosition, ServerWorld blastWorld, BlastEvent.Type blastType) {
            super(blastPosition, blastWorld, blastType);
        }
    }
    public static class ShrapnelImpact extends BlastEvent {
        public ShrapnelImpact(BlockPos blastPosition, ServerWorld blastWorld, BlastEvent.Type blastType) {
            super(blastPosition, blastWorld, blastType);
        }
    }
    public static class Incendiary extends BlastEvent {
        public Incendiary(BlockPos blastPosition, ServerWorld blastWorld, BlastEvent.Type blastType) {
            super(blastPosition, blastWorld, blastType);
        }
    }
    public static class Debilitation extends BlastEvent {
        public Debilitation(BlockPos blastPosition, ServerWorld blastWorld, BlastEvent.Type blastType) {
            super(blastPosition, blastWorld, blastType);
        }
    }
    public static class Chemical extends BlastEvent {
        public Chemical(BlockPos blastPosition, ServerWorld blastWorld, BlastEvent.Type blastType) {
            super(blastPosition, blastWorld, blastType);
        }
    }
    public static class Anvil extends BlastEvent {
        public Anvil(BlockPos blastPosition, ServerWorld blastWorld, BlastEvent.Type blastType) {
            super(blastPosition, blastWorld, blastType);
        }
    }
    public static class Repulsive extends BlastEvent {
        public Repulsive(BlockPos blastPosition, ServerWorld blastWorld, BlastEvent.Type blastType) {
            super(blastPosition, blastWorld, blastType);
        }
    }
    public static class Attractive extends BlastEvent {
        public Attractive(BlockPos blastPosition, ServerWorld blastWorld, BlastEvent.Type blastType) {
            super(blastPosition, blastWorld, blastType);
        }
    }
    public static class Nightmare extends BlastEvent {
        public Nightmare(BlockPos blastPosition, ServerWorld blastWorld, BlastEvent.Type blastType) {
            super(blastPosition, blastWorld, blastType);
        }
    }
    public static class Fragmentation extends BlastEvent {
        public Fragmentation(BlockPos blastPosition, ServerWorld blastWorld, BlastEvent.Type blastType) {
            super(blastPosition, blastWorld, blastType);
        }
    }
    public static class Contagious extends BlastEvent {
        public Contagious(BlockPos blastPosition, ServerWorld blastWorld, BlastEvent.Type blastType) {
            super(blastPosition, blastWorld, blastType);
        }
    }
    public static class Sonic extends BlastEvent {
        public Sonic(BlockPos blastPosition, ServerWorld blastWorld, BlastEvent.Type blastType) {
            super(blastPosition, blastWorld, blastType);
        }
    }
    public static class Breaching extends BlastEvent {
        public Breaching(BlockPos blastPosition, ServerWorld blastWorld, BlastEvent.Type blastType) {
            super(blastPosition, blastWorld, blastType);
        }
    }
    public static class Rejuvenation extends BlastEvent {
        public Rejuvenation(BlockPos blastPosition, ServerWorld blastWorld, BlastEvent.Type blastType) {
            super(blastPosition, blastWorld, blastType);
        }
    }
    public static class Thermobaric extends BlastEvent {
        public Thermobaric(BlockPos blastPosition, ServerWorld blastWorld, BlastEvent.Type blastType) {
            super(blastPosition, blastWorld, blastType);
        }
    }
    public static class Nuclear extends BlastEvent {
        public Nuclear(BlockPos blastPosition, ServerWorld blastWorld, BlastEvent.Type blastType) {
            super(blastPosition, blastWorld, blastType);
        }
    }
    public static class Emp extends BlastEvent {
        public Emp(BlockPos blastPosition, ServerWorld blastWorld, BlastEvent.Type blastType) {
            super(blastPosition, blastWorld, blastType);
        }
    }
    public static class Exothermic extends BlastEvent {
        public Exothermic(BlockPos blastPosition, ServerWorld blastWorld, BlastEvent.Type blastType) {
            super(blastPosition, blastWorld, blastType);
        }
    }
    public static class Endothermic extends BlastEvent {
        public Endothermic(BlockPos blastPosition, ServerWorld blastWorld, BlastEvent.Type blastType) {
            super(blastPosition, blastWorld, blastType);
        }
    }
    public static class Antigravitational extends BlastEvent {
        public Antigravitational(BlockPos blastPosition, ServerWorld blastWorld, BlastEvent.Type blastType) {
            super(blastPosition, blastWorld, blastType);
        }
    }
    public static class Ender extends BlastEvent {
        public Ender(BlockPos blastPosition, ServerWorld blastWorld, BlastEvent.Type blastType) {
            super(blastPosition, blastWorld, blastType);
        }
    }
    public static class Hypersonic extends BlastEvent {
        public Hypersonic(BlockPos blastPosition, ServerWorld blastWorld, BlastEvent.Type blastType) {
            super(blastPosition, blastWorld, blastType);
        }
    }
    public static class Antimatter extends BlastEvent {
        public Antimatter(BlockPos blastPosition, ServerWorld blastWorld, BlastEvent.Type blastType) {
            super(blastPosition, blastWorld, blastType);
        }
    }
    public static class Redmatter extends BlastEvent {
        public Redmatter(BlockPos blastPosition, ServerWorld blastWorld, BlastEvent.Type blastType) {
            super(blastPosition, blastWorld, blastType);
        }
    }
    public static class SMine extends BlastEvent {
        public SMine(BlockPos blastPosition, ServerWorld blastWorld, BlastEvent.Type blastType) {
            super(blastPosition, blastWorld, blastType);
        }
    }

}
