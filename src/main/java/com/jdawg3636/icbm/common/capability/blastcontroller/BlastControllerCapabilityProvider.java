package com.jdawg3636.icbm.common.capability.blastcontroller;

import com.jdawg3636.icbm.common.capability.ICBMCapabilities;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlastControllerCapabilityProvider implements ICapabilitySerializable<INBT> {

    public final IBlastControllerCapability capabilityInstance = new BlastControllerCapability();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
        return ICBMCapabilities.BLAST_CONTROLLER_CAPABILITY.orEmpty(capability, LazyOptional.of(()-> this.capabilityInstance));
    }

    @Override
    public net.minecraft.nbt.INBT serializeNBT() {
        return ICBMCapabilities.BLAST_CONTROLLER_CAPABILITY.getStorage().writeNBT(ICBMCapabilities.BLAST_CONTROLLER_CAPABILITY, capabilityInstance, null);
    }

    @Override
    public void deserializeNBT(net.minecraft.nbt.INBT nbt) {
        ICBMCapabilities.BLAST_CONTROLLER_CAPABILITY.getStorage().readNBT(ICBMCapabilities.BLAST_CONTROLLER_CAPABILITY, capabilityInstance, null, nbt);
    }

}
