package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.capability.ICBMCapabilities;
import com.jdawg3636.icbm.common.capability.blastcontroller.IBlastControllerCapability;
import com.jdawg3636.icbm.common.reg.BlastManagerThreadReg;
import com.jdawg3636.icbm.common.thread.AbstractBlastManagerThread;
import com.jdawg3636.icbm.common.thread.ExothermicBlastManagerThread;
import com.jdawg3636.icbm.common.thread.builder.AbstractBlastManagerThreadBuilder;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;

public class EventBlastExothermic extends AbstractBlastEvent {

    public EventBlastExothermic(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType, Direction blastDirection) {
        super(blastPosition, blastWorld, blastType, blastDirection);
    }

    @Override
    public boolean executeBlast() {

        ICBMBlastEventUtil.doBlastSoundAndParticles(this);

        AbstractBlastManagerThreadBuilder abstractBlastManagerThreadBuilder = BlastManagerThreadReg.getBuilderFromID("icbm:exothermic");
        if(abstractBlastManagerThreadBuilder == null) {
            return false;
        }

        AbstractBlastManagerThread abstractBlastManagerThread = abstractBlastManagerThreadBuilder.build();
        if(!(abstractBlastManagerThread instanceof ExothermicBlastManagerThread)) {
            return false;
        }

        ExothermicBlastManagerThread blastManagerThread = (ExothermicBlastManagerThread) abstractBlastManagerThread;
        blastManagerThread.explosionCenterPosX = getBlastPosition().getX();
        blastManagerThread.explosionCenterPosY = getBlastPosition().getY();
        blastManagerThread.explosionCenterPosZ = getBlastPosition().getZ();
        blastManagerThread.radius = (float)ICBMReference.COMMON_CONFIG.getBlastRadiusExothermic();
        blastManagerThread.threadCount = 4;
        LazyOptional<IBlastControllerCapability> cap = getBlastWorld().getCapability(ICBMCapabilities.BLAST_CONTROLLER_CAPABILITY);
        if(cap.isPresent()) {
            cap.orElse(null).enqueueBlastThread(blastManagerThread);
        }
        return true;

    }

}
