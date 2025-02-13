package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.capability.ICBMCapabilities;
import com.jdawg3636.icbm.common.capability.blastcontroller.IBlastControllerCapability;
import com.jdawg3636.icbm.common.reg.BlastManagerThreadReg;
import com.jdawg3636.icbm.common.thread.AbstractBlastManagerThread;
import com.jdawg3636.icbm.common.thread.NuclearBlastManagerThread;
import com.jdawg3636.icbm.common.thread.builder.AbstractBlastManagerThreadBuilder;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;

public class EventBlastNuclear extends AbstractBlastEvent {

    public EventBlastNuclear(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType, Direction blastDirection) {
        super(blastPosition, blastWorld, blastType, blastDirection);
    }

    @Override
    public boolean executeBlast() {

        ICBMBlastEventUtil.doBlastSoundAndParticles(this);

        AbstractBlastManagerThreadBuilder abstractBlastManagerThreadBuilder = BlastManagerThreadReg.getBuilderFromID("icbm:nuclear");
        if(abstractBlastManagerThreadBuilder == null) {
            return false;
        }

        AbstractBlastManagerThread abstractBlastManagerThread = abstractBlastManagerThreadBuilder.build();
        if(!(abstractBlastManagerThread instanceof NuclearBlastManagerThread)) {
            return false;
        }

        NuclearBlastManagerThread blastManagerThread = (NuclearBlastManagerThread) abstractBlastManagerThread;
        blastManagerThread.explosionCenterPosX = getBlastPosition().getX() + 0.5;
        blastManagerThread.explosionCenterPosY = getBlastPosition().getY() + 0.5;
        blastManagerThread.explosionCenterPosZ = getBlastPosition().getZ() + 0.5;
        blastManagerThread.radius = (float)ICBMReference.COMMON_CONFIG.getBlastRadiusNuclear();
        blastManagerThread.threadCount = ICBMReference.COMMON_CONFIG.getNumWorkerThreadsPerNuclearBlast();
        LazyOptional<IBlastControllerCapability> capOptional = getBlastWorld().getCapability(ICBMCapabilities.BLAST_CONTROLLER_CAPABILITY);
        capOptional.ifPresent((IBlastControllerCapability cap) -> cap.enqueueBlastThread(blastManagerThread));
        return true;

    }

}
