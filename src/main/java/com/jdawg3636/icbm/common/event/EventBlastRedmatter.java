package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.common.entity.EntityRedmatterBlast;
import com.jdawg3636.icbm.common.reg.EntityReg;
import com.jdawg3636.icbm.common.reg.SoundEventReg;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class EventBlastRedmatter extends AbstractBlastEvent {

    public EventBlastRedmatter(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType, Direction blastDirection) {
        super(blastPosition, blastWorld, blastType, blastDirection, SoundEventReg.EXPLOSION_GENERIC, 8.0F);
    }

    @Override
    public boolean executeBlast() {

        ICBMBlastEventUtil.doBlastSoundAndParticles(this);

        ServerWorld level = getBlastWorld();
        double explosionCenterPosX = getBlastPosition().getX() + 0.5;
        double explosionCenterPosY = getBlastPosition().getY() + 0.5;
        double explosionCenterPosZ = getBlastPosition().getZ() + 0.5;

        EntityRedmatterBlast entity = EntityReg.BLAST_REDMATTER.get().create(level);
        if(entity != null) {
            entity.setPos(explosionCenterPosX, explosionCenterPosY, explosionCenterPosZ);
            // Spawn Entity
            level.addFreshEntity(entity);
        }

        return true;

    }

}
