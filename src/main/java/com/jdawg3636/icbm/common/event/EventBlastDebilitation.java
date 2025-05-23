package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.common.entity.EntityLingeringBlast;
import com.jdawg3636.icbm.common.reg.EntityReg;
import com.jdawg3636.icbm.common.reg.SoundEventReg;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class EventBlastDebilitation extends AbstractBlastEvent {

    public EventBlastDebilitation(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType, Direction blastDirection) {
        super(blastPosition, blastWorld, blastType, blastDirection, SoundEventReg.EXPLOSION_DEBILITATION);
    }

    @Override
    public boolean executeBlast() {
        ICBMBlastEventUtil.doBlastSoundAndParticles(this);
        ICBMBlastEventUtil.doVanillaExplosionServerOnly(getBlastWorld(), getBlastPosition());
        // Early return if the explosion epicenter is inside an explosion-resistant fluid (ex. lava, water)
        if(getBlastWorld().getBlockState(getBlastPosition()).getFluidState().getExplosionResistance() > 0) {
            return true;
        }
        EntityLingeringBlast entity = EntityReg.BLAST_DEBILITATION.get().create(getBlastWorld());
        if(entity != null) {
            entity.setPos(getBlastPosition().getX() + 0.5, getBlastPosition().getY() + 0.5, getBlastPosition().getZ() + 0.5);
            entity.ticksRemaining = 400;
            entity.blastType = getBlastType();
            getBlastWorld().addFreshEntity(entity);
            return true;
        }
        return false;
    }

}
