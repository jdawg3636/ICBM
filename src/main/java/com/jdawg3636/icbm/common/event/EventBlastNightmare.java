package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.ICBMReference;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class EventBlastNightmare extends AbstractBlastEvent {

    public EventBlastNightmare(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType, Direction blastDirection) {
        super(blastPosition, blastWorld, blastType, blastDirection);
    }

    @Override
    public boolean executeBlast() {
        ICBMBlastEventUtil.doBlastSoundAndParticles(this);

        final double radius = ICBMReference.COMMON_CONFIG.getBlastRadiusNightmare();
        ICBMBlastEventUtil.doExplosionDamageAndKnockback(this, radius);

        // Early return if the explosion epicenter is inside an explosion-resistant fluid (ex. lava, water)
        if(getBlastWorld().getBlockState(getBlastPosition()).getFluidState().getExplosionResistance() > 0) {
            return true;
        }

        // todo: implement nightmare blast
        return false;

    }

}