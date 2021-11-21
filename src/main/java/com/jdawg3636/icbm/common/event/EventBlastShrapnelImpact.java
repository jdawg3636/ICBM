package com.jdawg3636.icbm.common.event;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

/**
 * Implements the explosion when a shrapnel entity from a fragmentation explosive collides with a block/player
 */
public class EventBlastShrapnelImpact extends AbstractBlastEvent {

    public EventBlastShrapnelImpact(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType, Direction blastDirection) {
        super(blastPosition, blastWorld, blastType, blastDirection);
    }

    @Override
    public boolean executeBlast() {
        // Using vanilla sounds/particles
        ICBMBlastEventUtil.doVanillaExplosion(this, 0.5F * 4.0F);
        return true;
    }

}
