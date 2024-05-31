package com.jdawg3636.icbm.common.capability.missiledirector;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class MissileDirectorCapabilityStorage implements Capability.IStorage<IMissileDirectorCapability> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<IMissileDirectorCapability> capability, IMissileDirectorCapability instance, Direction side) {
        CompoundNBT nbt = new CompoundNBT();
        // todo
        return nbt;
    }

    @Override
    public void readNBT(Capability<IMissileDirectorCapability> capability, IMissileDirectorCapability instance, Direction side, INBT nbt) {
        // todo
    }

}
