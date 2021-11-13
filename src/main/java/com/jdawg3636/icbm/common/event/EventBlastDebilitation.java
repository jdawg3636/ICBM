package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.common.entity.EntityLingeringBlast;
import com.jdawg3636.icbm.common.reg.EntityReg;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class EventBlastDebilitation extends AbstractBlastEvent {

    public EventBlastDebilitation(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType) {
        super(blastPosition, blastWorld, blastType);
    }

    @Override
    public boolean executeBlast() {
        ICBMBlastEventUtil.doBlastSoundAndParticles(this);
        ICBMBlastEventUtil.doVanillaExplosion(this);
        EntityLingeringBlast entity = EntityReg.BLAST_DEBILITATION.get().create(getBlastWorld());
        entity.setPos(getBlastPosition().getX(), getBlastPosition().getY(), getBlastPosition().getZ());
        entity.ticksRemaining = 400;
        getBlastWorld().addFreshEntity(entity);
        return true;
    }

}
