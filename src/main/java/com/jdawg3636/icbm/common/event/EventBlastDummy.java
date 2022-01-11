package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.common.reg.SoundEventReg;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

/**
 * For use by NYI BlastEventProviders
 * Does Nothing and Returns True
 */
public class EventBlastDummy extends AbstractBlastEvent{
	
	public EventBlastDummy(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType, Direction blastDirection) {
        super(blastPosition, blastWorld, blastType, blastDirection, SoundEventReg.EXPLOSION_ANTIGRAVITATIONAL);
    }

    @Override
    public boolean executeBlast() {
        ICBMBlastEventUtil.doBlastSoundAndParticles(this);
        return true;
    }
    
}
