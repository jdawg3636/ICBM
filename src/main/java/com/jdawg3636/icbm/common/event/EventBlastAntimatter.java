package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.common.capability.ICBMCapabilities;
import com.jdawg3636.icbm.common.capability.blastcontroller.IBlastControllerCapability;
import com.jdawg3636.icbm.common.thread.AntimatterBlastManagerThread;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;

public class EventBlastAntimatter extends AbstractBlastEvent {

    public EventBlastAntimatter(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType) {
        super(blastPosition, blastWorld, blastType);
    }

    @Override
    public boolean executeBlast() {

        ICBMBlastEventUtil.doBlastSoundAndParticles(this);

        AntimatterBlastManagerThread blastManagerThread = new AntimatterBlastManagerThread();
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
