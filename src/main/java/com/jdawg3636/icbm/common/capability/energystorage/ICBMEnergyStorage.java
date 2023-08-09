package com.jdawg3636.icbm.common.capability.energystorage;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class ICBMEnergyStorage extends EnergyStorage implements INBTSerializable<CompoundNBT> {

    public ICBMEnergyStorage() {
        super(0);
    }

    private Runnable callbackOnChanged = () -> {};

    public ICBMEnergyStorage setCallbackOnChanged(Runnable callbackOnChanged) {
        this.callbackOnChanged = callbackOnChanged;
        return this;
    }

    public ICBMEnergyStorage setCapacity(int capacity, boolean updateMaxThroughput) {
        this.capacity = capacity;
        this.energy = Math.min(energy, capacity);
        if(updateMaxThroughput) {
            this.maxReceive = capacity;
            this.maxExtract = capacity;
        }
        callbackOnChanged.run();
        return this;
    }

    public ICBMEnergyStorage setMaxReceive(int maxReceive) {
        this.maxReceive = maxReceive;
        callbackOnChanged.run();
        return this;
    }

    public ICBMEnergyStorage setMaxExtract(int maxExtract) {
        this.maxExtract = maxExtract;
        callbackOnChanged.run();
        return this;
    }

    public int getMaxReceive() {
        return this.maxReceive;
    }

    public int getMaxExtract() {
        return this.maxExtract;
    }

    public ICBMEnergyStorage setEnergy(int energy) {
        this.energy = Math.max(Math.min(energy, this.capacity), 0);
        callbackOnChanged.run();
        return this;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int returnValue = super.receiveEnergy(maxReceive, simulate);
        if(!simulate && returnValue != 0) callbackOnChanged.run();
        return returnValue;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int returnValue = super.extractEnergy(maxExtract, simulate);
        if(!simulate && returnValue != 0) callbackOnChanged.run();
        return returnValue;
    }

    // Doesn't enforce this.maxExtract, only limited by parameter and capacity
    public int extractEnergyUnchecked(int maxExtract, boolean simulate) {
        int energyExtracted = Math.min(energy, maxExtract);
        if(!simulate) energy -= energyExtracted;
        if(!simulate && energyExtracted != 0) callbackOnChanged.run();
        return energyExtracted;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("capacity", getMaxEnergyStored());
        nbt.putInt("maxReceive", maxReceive);
        nbt.putInt("maxExtract", maxExtract);
        nbt.putInt("energy", getEnergyStored());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        setCapacity(nbt.getInt("capacity"), false);
        setMaxReceive(nbt.getInt("maxReceive"));
        setMaxExtract(nbt.getInt("maxExtract"));
        setEnergy(nbt.getInt("energy"));
    }

}
