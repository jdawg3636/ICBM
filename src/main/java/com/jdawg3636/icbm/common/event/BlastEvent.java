package com.jdawg3636.icbm.common.event;

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

    private final BlockPos blastPosition;
    private final ServerWorld blastWorld;
    private final boolean isGrenade;

    public BlastEvent(BlockPos blastPosition, ServerWorld blastWorld, boolean isGrenade) {
        this.blastPosition = blastPosition;
        this.blastWorld = blastWorld;
        this.isGrenade = isGrenade;
    }

    public BlockPos getBlastPosition() {
        return blastPosition;
    }

    public ServerWorld getBlastWorld() {
        return blastWorld;
    }

    public boolean getBlastIsGrenade() {
        return isGrenade;
    }

    public static class Incendiary extends BlastEvent {
        public Incendiary(BlockPos blastPosition, ServerWorld blastWorld, boolean isGrenade) {
            super(blastPosition, blastWorld, isGrenade);
        }
    }

}
