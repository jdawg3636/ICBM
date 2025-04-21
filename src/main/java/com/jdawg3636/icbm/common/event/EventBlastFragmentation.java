package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.common.entity.EntityShrapnel;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class EventBlastFragmentation extends AbstractBlastEvent {

    public EventBlastFragmentation(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType, Direction blastDirection) {
        super(blastPosition, blastWorld, blastType, blastDirection);
    }

    @Override
    public boolean executeBlast() {
        ICBMBlastEventUtil.doBlastSoundAndParticles(this);
        ICBMBlastEventUtil.doVanillaExplosionServerOnly(getBlastWorld(), getBlastPosition());

        // Early return if the explosion epicenter is inside an explosion-resistant fluid (ex. lava, water)
        if(getBlastWorld().getBlockState(getBlastPosition()).getFluidState().getExplosionResistance() > 0) {
            return true;
        }

        for(double i = -0.5; i <= 0.5; i += 0.0625) {
            for(double j = -0.5; j <= 0.5; j += 0.0625) {
                if(i * i + j * j > 0.5 * 0.5) continue; // Circle, not a square.
                EntityShrapnel shrapnelEntity = new EntityShrapnel(getBlastWorld(), getBlastPosition().getX() + 0.5, getBlastPosition().getY() + 0.5, getBlastPosition().getZ() + 0.5, true, getBlastType());
                shrapnelEntity.setDeltaMovement(i, getBlastWorld().random.nextFloat(), j);
                getBlastWorld().addFreshEntity(shrapnelEntity);
            }
        }
        return true;
    }

}
