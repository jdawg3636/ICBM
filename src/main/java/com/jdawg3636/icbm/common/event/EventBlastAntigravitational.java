package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.common.entity.EntityLingeringBlastAntigravitational;
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
        ICBMBlastEventUtil.doBlastParticles(this);
        // Early return if the explosion epicenter is inside an explosion-resistant fluid (ex. lava, water)
        if(getBlastWorld().getBlockState(getBlastPosition()).getFluidState().getExplosionResistance() > 0) {
            return true;
        }
        EntityLingeringBlastAntigravitational entity = EntityReg.BLAST_ANTIGRAVITATIONAL.get().create(getBlastWorld());
        if(entity != null) {
            entity.setPos(getBlastPosition().getX() + 0.5, getBlastPosition().getY() + 0.5, getBlastPosition().getZ() + 0.5);
            entity.ticksRemaining = 25 * 20;
            entity.blastType = getBlastType();
            entity.addEntityToLevel(getBlastType() == Type.GRENADE ? 10 : 20); //todo: make configurable
            return true;
        }
        return false;
    }

}
