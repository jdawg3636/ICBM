package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.common.entity.EntityShrapnel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class EventBlastFragmentation extends AbstractBlastEvent {

    public EventBlastFragmentation(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType) {
        super(blastPosition, blastWorld, blastType);
    }

    @Override
    public boolean executeBlast() {
        ICBMBlastEventUtil.doBlastSoundAndParticles(this);
        ICBMBlastEventUtil.doVanillaExplosion(this);
        for(double i = -0.5; i <= 0.5; i += 0.0625) {
            for(double j = -0.5; j <= 0.5; j += 0.0625) {
                if(i * i + j * j > 0.5 * 0.5) continue; // Circle, not a square.
                EntityShrapnel shrapnelEntity = new EntityShrapnel(getBlastWorld(), getBlastPosition().getX(), getBlastPosition().getY(), getBlastPosition().getZ(), true, getBlastType());
                shrapnelEntity.setDeltaMovement(i, getBlastWorld().random.nextFloat(), j);
                getBlastWorld().addFreshEntity(shrapnelEntity);
            }
        }
        return true;
    }

}
