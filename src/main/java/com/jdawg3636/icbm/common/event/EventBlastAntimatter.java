package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.common.capability.ICBMCapabilities;
import com.jdawg3636.icbm.common.capability.blastcontroller.IBlastControllerCapability;
import com.jdawg3636.icbm.common.reg.BlastManagerThreadReg;
import com.jdawg3636.icbm.common.reg.SoundEventReg;
import com.jdawg3636.icbm.common.thread.AbstractBlastManagerThread;
import com.jdawg3636.icbm.common.thread.AntimatterBlastManagerThread;
import com.jdawg3636.icbm.common.thread.builder.AbstractBlastManagerThreadBuilder;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;

public class EventBlastAntimatter extends AbstractBlastEvent {

    public EventBlastAntimatter(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType, Direction blastDirection) {
        super(blastPosition, blastWorld, blastType, blastDirection, SoundEventReg.EXPLOSION_ANTIMATTER, 8.0F);
    }

    @Override
    public boolean executeBlast() {

        ICBMBlastEventUtil.doBlastSoundAndParticles(this);

        AbstractBlastManagerThreadBuilder abstractBlastManagerThreadBuilder = BlastManagerThreadReg.getBuilderFromID("icbm:antimatter");
        if(abstractBlastManagerThreadBuilder == null) {
            return false;
        }

        AbstractBlastManagerThread abstractBlastManagerThread = abstractBlastManagerThreadBuilder.build();
        if(!(abstractBlastManagerThread instanceof AntimatterBlastManagerThread)) {
            return false;
        }

        AntimatterBlastManagerThread blastManagerThread = (AntimatterBlastManagerThread) abstractBlastManagerThread;
        blastManagerThread.explosionCenterPosX = getBlastPosition().getX();
        blastManagerThread.explosionCenterPosY = getBlastPosition().getY();
        blastManagerThread.explosionCenterPosZ = getBlastPosition().getZ();
        blastManagerThread.radius = 50;
        blastManagerThread.fuzzyEdgeThickness = 2;
        LazyOptional<IBlastControllerCapability> cap = getBlastWorld().getCapability(ICBMCapabilities.BLAST_CONTROLLER_CAPABILITY);
        if(cap.isPresent()) {
            cap.orElse(null).enqueueBlastThread(blastManagerThread);
        }

        return true;

    }

}
