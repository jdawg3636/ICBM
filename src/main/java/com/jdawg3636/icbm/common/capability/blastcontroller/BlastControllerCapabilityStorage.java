package com.jdawg3636.icbm.common.capability.blastcontroller;

import com.jdawg3636.icbm.common.blast.Blast;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class BlastControllerCapabilityStorage implements Capability.IStorage<IBlastControllerCapability> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<IBlastControllerCapability> capability, IBlastControllerCapability instance, Direction side) {
        int i = 0;
        CompoundNBT nbt = new CompoundNBT();
        for(Blast blast : instance.getCurrentBlasts()) nbt.put("" + i++, blast.serializeNBT());
        nbt.putInt("testVar", 69);
        return nbt;
    }

    @Override
    public void readNBT(Capability<IBlastControllerCapability> capability, IBlastControllerCapability instance, Direction side, INBT nbt) {
        if(!(nbt instanceof CompoundNBT)) return;
        for(String blastNBTKey : ((CompoundNBT) nbt).getAllKeys()) {
            instance.addBlast(Blast.deserializeNBT(((CompoundNBT) nbt).getCompound(blastNBTKey)));
        }
    }

}
