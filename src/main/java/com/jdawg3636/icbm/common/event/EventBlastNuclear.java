package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.common.capability.ICBMCapabilities;
import com.jdawg3636.icbm.common.capability.blastcontroller.IBlastControllerCapability;
import com.jdawg3636.icbm.common.reg.BlastManagerThreadReg;
import com.jdawg3636.icbm.common.thread.AbstractBlastManagerThread;
import com.jdawg3636.icbm.common.thread.AntimatterBlastManagerThread;
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
        blastManagerThread.explosionCenterPosX = getBlastPosition().getX();
        blastManagerThread.explosionCenterPosY = getBlastPosition().getY();
        blastManagerThread.explosionCenterPosZ = getBlastPosition().getZ();
        blastManagerThread.radius = 30.0F; //todo: make configurable, default to 30 if performance is acceptable
        blastManagerThread.threadCount = 4;
        LazyOptional<IBlastControllerCapability> cap = getBlastWorld().getCapability(ICBMCapabilities.BLAST_CONTROLLER_CAPABILITY);
        if(cap.isPresent()) {
            cap.orElse(null).enqueueBlastThread(blastManagerThread);
        }
        return true;
    }

}
