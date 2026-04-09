package com.jdawg3636.icbm.mixin.accessors;

import net.minecraftforge.energy.EnergyStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EnergyStorage.class)
public interface IEnergyStorageAccessor {
    @Accessor(value = "energy", remap = false)
    int getEnergy();
    @Accessor(value = "energy", remap = false)
    void setEnergy(int value);
}
