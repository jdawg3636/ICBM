package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.entity.EntityBlastAntigravitational;
import com.jdawg3636.icbm.common.reg.EntityReg;
import com.jdawg3636.icbm.common.reg.SoundEventReg;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class EventBlastAntigravitational extends AbstractBlastEvent {

    public EventBlastAntigravitational(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType, Direction blastDirection) {
        super(blastPosition, blastWorld, blastType, blastDirection, SoundEventReg.EXPLOSION_ANTIGRAVITATIONAL);
    }

    @Override
    public boolean executeBlast() {
        // Particles
        ICBMBlastEventUtil.doBlastParticles(this);
        // Early return if the explosion epicenter is inside an explosion-resistant fluid (ex. lava, water)
        if(getBlastWorld().getBlockState(getBlastPosition()).getFluidState().getExplosionResistance() > 0) {
            return true;
        }
        // Get radius from config
        final double radius = ICBMReference.COMMON_CONFIG.getBlastRadiusAntigravitational();
        // Try to construct entity
        EntityBlastAntigravitational entity = EntityReg.BLAST_ANTIGRAVITATIONAL.get().create(getBlastWorld());
        // If successful, initialize its variable, add it to the level, and return true.
        if(entity != null) {
            entity.setPos(getBlastPosition().getX() + 0.5, getBlastPosition().getY() + 0.5, getBlastPosition().getZ() + 0.5);
            entity.ticksRemaining = ICBMReference.COMMON_CONFIG.getAntigravitationalBlastLingerDuration();
            entity.blastType = getBlastType();
            entity.addEntityToLevel(getBlastType() == Type.GRENADE ? (radius / 2.0) : radius);
            return true;
        }
        // Else, return false.
        return false;
    }

}
