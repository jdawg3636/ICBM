package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.ICBMReference;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class EventBlastThermobaric extends AbstractBlastEvent {

    public EventBlastThermobaric(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType, Direction blastDirection) {
        super(blastPosition, blastWorld, blastType, blastDirection);
    }

    @Override
    public boolean executeBlast() {
        ICBMBlastEventUtil.doBlastSoundAndParticles(this);
        ICBMBlastEventUtil.doVanillaExplosionServerOnly(getBlastWorld(), getBlastPosition(), (float)ICBMReference.COMMON_CONFIG.getBlastRadiusThermobaric());
        return true;
    }

}
