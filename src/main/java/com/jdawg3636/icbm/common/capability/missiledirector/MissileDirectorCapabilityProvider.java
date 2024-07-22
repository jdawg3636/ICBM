package com.jdawg3636.icbm.common.capability.missiledirector;

import com.jdawg3636.icbm.common.capability.ICBMCapabilities;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MissileDirectorCapabilityProvider implements ICapabilitySerializable<INBT> {

    private final IMissileDirectorCapability capabilityInstance;

    public MissileDirectorCapabilityProvider(World level) {
        capabilityInstance = new MissileDirectorCapability(level);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
        return ICBMCapabilities.MISSILE_DIRECTOR_CAPABILITY.orEmpty(capability, LazyOptional.of(()-> this.capabilityInstance));
    }

    @Override
    public net.minecraft.nbt.INBT serializeNBT() {
        return ICBMCapabilities.MISSILE_DIRECTOR_CAPABILITY.getStorage().writeNBT(ICBMCapabilities.MISSILE_DIRECTOR_CAPABILITY, capabilityInstance, null);
    }

    @Override
    public void deserializeNBT(net.minecraft.nbt.INBT nbt) {
        ICBMCapabilities.MISSILE_DIRECTOR_CAPABILITY.getStorage().readNBT(ICBMCapabilities.MISSILE_DIRECTOR_CAPABILITY, capabilityInstance, null, nbt);
    }

}
