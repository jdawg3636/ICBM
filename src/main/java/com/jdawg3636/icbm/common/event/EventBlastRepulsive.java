package com.jdawg3636.icbm.common.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class EventBlastRepulsive extends AbstractBlastEvent {

    public EventBlastRepulsive(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType) {
        super(blastPosition, blastWorld, blastType);
    }

    @Override
    public boolean executeBlast() {
        ICBMBlastEventUtil.doMovementBlast(this, 1);
        return true;
    }

}
