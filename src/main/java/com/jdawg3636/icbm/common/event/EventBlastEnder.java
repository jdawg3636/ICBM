package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.common.entity.EntityLingeringBlastEnder;
import com.jdawg3636.icbm.common.reg.EntityReg;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class EventBlastEnder extends AbstractBlastEvent {

    public EventBlastEnder(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType, Direction blastDirection) {
        super(blastPosition, blastWorld, blastType, blastDirection);
    }

    @Override
    public boolean executeBlast() {
        ICBMBlastEventUtil.doBlastSoundAndParticles(this);
        EntityLingeringBlastEnder entityLingeringBlast = new EntityLingeringBlastEnder(EntityReg.BLAST_ENDER.get(), getBlastWorld(), 10 * 20);
        entityLingeringBlast.blastType = getBlastType();
        entityLingeringBlast.setPos(getBlastPosition().getX() + 0.5, getBlastPosition().getY() + 0.5, getBlastPosition().getZ() + 0.5);
        getBlastWorld().addFreshEntity(entityLingeringBlast);
        return true;
    }

}
