package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.ICBMReference;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class EventBlastCondensed extends AbstractBlastEvent {

    public EventBlastCondensed(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType, Direction blastDirection) {
        super(blastPosition, blastWorld, blastType, blastDirection);
    }

    @Override
    public boolean executeBlast() {
        ICBMBlastEventUtil.doBlastSoundAndParticles(this);
        ICBMBlastEventUtil.doVanillaExplosionServerOnly(getBlastWorld(), getBlastPosition(), (getBlastType() == AbstractBlastEvent.Type.GRENADE ? 4/(float)7 : 1F) * (float)ICBMReference.COMMON_CONFIG.getBlastRadiusCondensed());
        return true;
    }

}
