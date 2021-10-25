package com.jdawg3636.icbm.common.capability.blastcontroller;

import com.jdawg3636.icbm.common.blast.thread.AbstractBlastManagerThread;
import com.jdawg3636.icbm.common.blast.thread.VanillaBlastManagerThread;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class BlastControllerCapabilityStorage implements Capability.IStorage<IBlastControllerCapability> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<IBlastControllerCapability> capability, IBlastControllerCapability instance, Direction side) {

        CompoundNBT nbt = new CompoundNBT();
        CompoundNBT blasts = new CompoundNBT();

        int i = 0;
        for(AbstractBlastManagerThread blastManagerThread : instance.getActiveBlastThreads()) {
            blasts.put("" + i++, blastManagerThread.serializeNBT());
        }
        for(AbstractBlastManagerThread blastManagerThread : instance.getQueuedBlastThreads()) {
            blasts.put("" + i++, blastManagerThread.serializeNBT());
        }

        nbt.put("blasts", blasts);

        return nbt;

    }

    @Override
    public void readNBT(Capability<IBlastControllerCapability> capability, IBlastControllerCapability instance, Direction side, INBT nbt) {
        if(!(nbt instanceof CompoundNBT)) return;
        try {
            CompoundNBT nbtCasted = (CompoundNBT) nbt;
            CompoundNBT blasts = ((CompoundNBT) nbt).getCompound("blasts");
            for (String blastID : blasts.getAllKeys()) {
                CompoundNBT blastNBT = blasts.getCompound(blastID);
                AbstractBlastManagerThread blastManagerThread = new VanillaBlastManagerThread(); // todo generalize to non-vanilla blast manager threads
                blastManagerThread.deserializeNBT(blastNBT);
                instance.enqueueBlastThread(blastManagerThread);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
