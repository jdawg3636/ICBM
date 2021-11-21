package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.common.entity.EntityLingeringBlast;
import com.jdawg3636.icbm.common.reg.EntityReg;
import com.jdawg3636.icbm.common.reg.SoundEventReg;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class EventBlastChemical extends AbstractBlastEvent {

    public EventBlastChemical(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType, Direction blastDirection) {
        super(blastPosition, blastWorld, blastType, blastDirection, SoundEventReg.EXPLOSION_DEBILITATION);
    }

    @Override
    public boolean executeBlast() {
        ICBMBlastEventUtil.doBlastSoundAndParticles(this);
        ICBMBlastEventUtil.doVanillaExplosion(this);
        EntityLingeringBlast entity = EntityReg.BLAST_CHEMICAL.get().create(getBlastWorld());
        entity.setPos(getBlastPosition().getX(), getBlastPosition().getY(), getBlastPosition().getZ());
        entity.ticksRemaining = 100;
        getBlastWorld().addFreshEntity(entity);
        return true;
    }

}
