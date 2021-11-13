package com.jdawg3636.icbm.common.capability.blastcontroller;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.thread.AbstractBlastManagerThread;
import com.jdawg3636.icbm.common.thread.builder.AbstractBlastManagerThreadBuilder;
import com.jdawg3636.icbm.common.reg.BlastManagerThreadReg;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.RegistryObject;

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
                String blastManagerThreadTypeName = blastNBT.getString("manager_thread_type");
                AbstractBlastManagerThread blastManagerThread = null;
                for(RegistryObject<AbstractBlastManagerThreadBuilder> blastManagerThreadBuilder : BlastManagerThreadReg.BLAST_MANAGER_THREADS.getEntries()) {
                    if(blastManagerThreadBuilder.getId().toString().equals(blastManagerThreadTypeName)) {
                        blastManagerThread = blastManagerThreadBuilder.get().build();
                    }
                }

                if(blastManagerThread != null) {
                    blastManagerThread.deserializeNBT(blastNBT);
                    instance.enqueueBlastThread(blastManagerThread);
                }
                else {
                    ICBMReference.logger().error("Failed to deserialize blast of manager thread type \"" + blastManagerThreadTypeName + "\"");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
