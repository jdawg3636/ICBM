package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.capability.ICBMCapabilities;
import com.jdawg3636.icbm.common.capability.blastcontroller.IBlastControllerCapability;
import com.jdawg3636.icbm.common.reg.BlastManagerThreadReg;
import com.jdawg3636.icbm.common.reg.SoundEventReg;
import com.jdawg3636.icbm.common.thread.AbstractBlastManagerThread;
import com.jdawg3636.icbm.common.thread.SonicBlastManagerThread;
import com.jdawg3636.icbm.common.thread.builder.AbstractBlastManagerThreadBuilder;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;

public class EventBlastHypersonic extends AbstractBlastEvent {

    public EventBlastHypersonic(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType, Direction blastDirection) {
        super(blastPosition, blastWorld, blastType, blastDirection, SoundEventReg.EXPLOSION_HYPERSONIC, 5F);
    }

    @Override
    public boolean executeBlast() {

        ICBMBlastEventUtil.doBlastSoundAndParticles(this);

        final double radius = ICBMReference.COMMON_CONFIG.getBlastRadiusHypersonic();
        ICBMBlastEventUtil.doExplosionDamageAndKnockback(this, radius);

        // Early return if the explosion epicenter is inside an explosion-resistant fluid (ex. lava, water)
        if(getBlastWorld().getBlockState(getBlastPosition()).getFluidState().getExplosionResistance() > 0) {
            return true;
        }

        AbstractBlastManagerThreadBuilder abstractBlastManagerThreadBuilder = BlastManagerThreadReg.getBuilderFromID("icbm:hypersonic");
        if(abstractBlastManagerThreadBuilder == null) {
            return false;
        }

        AbstractBlastManagerThread abstractBlastManagerThread = abstractBlastManagerThreadBuilder.build();
        if(!(abstractBlastManagerThread instanceof SonicBlastManagerThread)) {
            return false;
        }

        SonicBlastManagerThread blastManagerThread = (SonicBlastManagerThread) abstractBlastManagerThread;
        blastManagerThread.explosionCenterPosX = getBlastPosition().getX() + 0.5;
        blastManagerThread.explosionCenterPosY = getBlastPosition().getY() + 0.5;
        blastManagerThread.explosionCenterPosZ = getBlastPosition().getZ() + 0.5;
        blastManagerThread.radius = (float) radius;
        blastManagerThread.blocksAffectedPerTick = 36;
        blastManagerThread.threadCount = 4;
        LazyOptional<IBlastControllerCapability> capOptional = getBlastWorld().getCapability(ICBMCapabilities.BLAST_CONTROLLER_CAPABILITY);
        capOptional.ifPresent((IBlastControllerCapability cap) -> cap.enqueueBlastThread(blastManagerThread));
        return true;
    }

}
