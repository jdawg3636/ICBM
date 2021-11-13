package com.jdawg3636.icbm.common.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class EventBlastThermobaric extends AbstractBlastEvent {

    public EventBlastThermobaric(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType) {
        super(blastPosition, blastWorld, blastType);
    }

    @Override
    public boolean executeBlast() {
        ICBMBlastEventUtil.doVanillaExplosion(this, 4F * 4.0F);
        return true;
    }

}
