package com.jdawg3636.icbm.common.capability.trackingmanager;

import com.jdawg3636.icbm.common.capability.ICBMCapabilities;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TrackingManagerCapabilityProvider implements ICapabilitySerializable<INBT> {

    private ITrackingManagerCapability capabilityInstance = new TrackingManagerCapability();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
        return ICBMCapabilities.TRACKING_MANAGER_CAPABILITY.orEmpty(capability, LazyOptional.of(()-> this.capabilityInstance));
    }

    @Override
    public net.minecraft.nbt.INBT serializeNBT() {
        return ICBMCapabilities.TRACKING_MANAGER_CAPABILITY.getStorage().writeNBT(ICBMCapabilities.TRACKING_MANAGER_CAPABILITY, capabilityInstance, null);
    }

    @Override
    public void deserializeNBT(net.minecraft.nbt.INBT nbt) {
        ICBMCapabilities.TRACKING_MANAGER_CAPABILITY.getStorage().readNBT(ICBMCapabilities.TRACKING_MANAGER_CAPABILITY, capabilityInstance, null, nbt);
    }

}
