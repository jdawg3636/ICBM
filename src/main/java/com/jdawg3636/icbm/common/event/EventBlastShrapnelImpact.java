package com.jdawg3636.icbm.common.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

/**
 * Implements the explosion when a shrapnel entity from a fragmentation explosive collides with a block/player
 */
public class EventBlastShrapnelImpact extends AbstractBlastEvent {

    public EventBlastShrapnelImpact(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType) {
        super(blastPosition, blastWorld, blastType);
    }

    @Override
    public boolean executeBlast() {
        ICBMBlastEventUtil.doVanillaExplosion(this, 0.5F * 4.0F);
        return true;
    }

}
