package com.jdawg3636.icbm.common.capability.energystorage;

import com.jdawg3636.icbm.ICBMReference;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ICBMEnergyStorage extends EnergyStorage implements INBTSerializable<CompoundNBT> {

    public ICBMEnergyStorage() {
        super(0);
    }

    public ICBMEnergyStorage(Consumer<ICBMEnergyStorage> settingsCallback) {
        this();
        settingsCallback.accept(this);
    }

    public static ICapabilityProvider getNewCapabilityProvider(ItemStack stack, Consumer<ICBMEnergyStorage> settingsCallback) {
        return new ICBMEnergyStorageCapabilityProvider(stack, settingsCallback);
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

    public double getEnergyPercentage() {
        return getEnergyStored() / (double)getMaxEnergyStored();
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
        if(nbt == null) return;
        if(nbt.contains("capacity")) setCapacity(nbt.getInt("capacity"), false);
        if(nbt.contains("maxReceive")) setMaxReceive(nbt.getInt("maxReceive"));
        if(nbt.contains("maxExtract")) setMaxExtract(nbt.getInt("maxExtract"));
        if(nbt.contains("energy")) setEnergy(nbt.getInt("energy"));
    }

    public IFormattableTextComponent getEnergyStoredFormatted(int decimalPlaces, boolean useShortName) {
        return EnergyMeasurementUnit.formatEnergyValue(getEnergyStored(), decimalPlaces, useShortName);
    }

    /**
     * Loosely adapted from mekanism.common.util.UnitDisplayUtils
     */
    public static enum EnergyMeasurementUnit {

        BASE("", "", 1L, false),
        KILO("Kilo", "k", 1000L),
        MEGA("Mega", "M", 1000000L),
        GIGA("Giga", "G", 1000000000L),
        TERA("Tera", "T", 1000000000000L),
        PETA("Peta", "P", 1000000000000000L),
        EXA("Exa", "E", 1000000000000000000L);

        private final String name;
        private final String shortName;
        private final long unitValue;
        private final boolean canBeFractional;

        EnergyMeasurementUnit(String name, String shortName, long value) {
            this(name, shortName, value, true);
        }

        EnergyMeasurementUnit(String name, String shortName, long value, boolean canBeFractional) {
            this.name = name;
            this.shortName = shortName;
            this.unitValue = value;
            this.canBeFractional = canBeFractional;
        }

        public String energyValueToString(long value, int decimalPlaces, boolean useShortName) {
            // Calculate whole part of quotient
            long whole = value / this.unitValue;
            String wholeString = Long.toString(whole);
            // Calculate decimal part of quotient
            String decimalString = "";
            if(this.canBeFractional && decimalPlaces != 0) {
                long remainder = value - (whole * this.unitValue);
                double decimal = remainder / (double) this.unitValue;
                decimalString = String.format("%." + decimalPlaces + "f", decimal).substring(1);
            }
            // Return concatenation of whole and decimal parts
            return wholeString + decimalString + " " + (useShortName ? shortName : name);
        }

        public static IFormattableTextComponent formatEnergyValue(long value, int decimalPlaces, boolean useShortName) {
            // Iteratively find largest suitable unit
            EnergyMeasurementUnit unitToUse = EnergyMeasurementUnit.BASE;
            for(EnergyMeasurementUnit currentUnit : EnergyMeasurementUnit.values()) {
                if(value >= currentUnit.unitValue) {
                    unitToUse = currentUnit;
                } else {
                    break;
                }
            }
            // Format value and append localized unit
            return new StringTextComponent(unitToUse.energyValueToString(value, decimalPlaces, useShortName)).append(new TranslationTextComponent("energy." + ICBMReference.MODID + ".forge" + (useShortName ? ".short" : "")));
        }

    }

    public static class ICBMEnergyStorageCapabilityProvider implements ICapabilityProvider {
        public final ICBMEnergyStorage energyStorage;
        public final ItemStack stack;

        public ICBMEnergyStorageCapabilityProvider(ItemStack stack, Consumer<ICBMEnergyStorage> settingsCallback) {
            super();
            this.stack = stack;
            this.energyStorage = new ICBMEnergyStorage(settingsCallback).setCallbackOnChanged(this::onContentsChanged);
            energyStorage.deserializeNBT((CompoundNBT)(stack.getOrCreateTag().get("energy")));
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
            return capability == CapabilityEnergy.ENERGY ? LazyOptional.of(() -> energyStorage).cast() : LazyOptional.empty();
        }

        public void onContentsChanged() {
            if (!this.stack.isEmpty()) {
                stack.getOrCreateTag().put("energy", energyStorage.serializeNBT());
            }
        }

    };

}
