package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.entity.EntityBlastEnder;
import com.jdawg3636.icbm.common.reg.EntityReg;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class EventBlastEnder extends AbstractBlastEvent {

    public EventBlastEnder(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType, Direction blastDirection) {
        super(blastPosition, blastWorld, blastType, blastDirection, () -> SoundEvents.END_PORTAL_SPAWN, 10F);
    }

    @Override
    public boolean executeBlast() {
        ICBMBlastEventUtil.doBlastSoundAndParticles(this);

        // Early return if the explosion epicenter is inside an explosion-resistant fluid (ex. lava, water)
        if(getBlastWorld().getBlockState(getBlastPosition()).getFluidState().getExplosionResistance() > 0) {
            return true;
        }

        EntityBlastEnder entityLingeringBlast = new EntityBlastEnder(
                EntityReg.BLAST_ENDER.get(),
                getBlastWorld(),
                ICBMReference.COMMON_CONFIG.getEnderBlastLingerDuration()
        );
        entityLingeringBlast.blastType = getBlastType();
        entityLingeringBlast.setPos(getBlastPosition().getX() + 0.5, getBlastPosition().getY() + 0.5, getBlastPosition().getZ() + 0.5);
        getBlastWorld().addFreshEntity(entityLingeringBlast);
        return true;
    }

}
