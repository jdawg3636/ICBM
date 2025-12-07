package com.jdawg3636.icbm.common.item;

import com.jdawg3636.icbm.ICBMReference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

import java.util.ArrayList;
import java.util.List;

public class ItemSignalDisruptor extends ItemFrequency {

    public static final int ENERGY_CAPACITY = 1_000_000; // todo make configurable

    public static List<Integer> getFrequenciesDisruptedByPlayer(PlayerEntity player) {
        List<Integer> toReturn = new ArrayList<>();
        // Iterate through inventory
        for(NonNullList<ItemStack> nonnulllist : player.inventory.compartments) {
            for(int i = 0; i < nonnulllist.size(); ++i) {
                ItemStack itemStack = nonnulllist.get(i);
                // Validate Item
                if (!(itemStack.getItem() instanceof ItemSignalDisruptor)) {
                    continue;
                }
                // Validate Energy
                if (itemStack.getCapability(CapabilityEnergy.ENERGY).map(es -> es.getEnergyStored() == 0).orElse(true)) {
                    continue;
                }
                // Validate Frequency
                int frequencyBeingDisrupted = ItemFrequency.getFrequency(itemStack);
                if(frequencyBeingDisrupted != 0) {
                    // If all validations passed, add frequency to the list to be returned.
                    toReturn.add(frequencyBeingDisrupted);
                }
            }
        }
        // Return
        return toReturn;
    }

    public ItemSignalDisruptor() {
        this(new Item.Properties().tab(ICBMReference.CREATIVE_TAB));
    }

    public ItemSignalDisruptor(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, World levelCurrent, Entity entity, int slot, boolean isSelected) {
        super.inventoryTick(itemStack, levelCurrent, entity, slot, isSelected);
        if(ItemFrequency.getFrequency(itemStack) != 0) {
            itemStack.getCapability(CapabilityEnergy.ENERGY).ifPresent(energyStorage -> {
                // TODO: make energy usage per tick configurable
                energyStorage.extractEnergy(5, false);
            });
        }
    }

    @Override
    public IFormattableTextComponent getFrequencySetMessage(int frequency) {
        return new TranslationTextComponent("message." + ICBMReference.MODID + ".signal_disruptor.frequency_set", frequency);
    }

    @Override
    public IFormattableTextComponent getFrequencyTooltip(int frequency) {
        return new TranslationTextComponent("info." + ICBMReference.MODID + ".signal_disruptor.disrupting_frequency", frequency);
    }

    @Override
    public IFormattableTextComponent getFrequencyMissingTooltip() {
        return new TranslationTextComponent("info." + ICBMReference.MODID + ".signal_disruptor.not_disrupting_frequency");
    }

    @Override
    public int getEnergyCapacity() {
        return ENERGY_CAPACITY;
    }

    @Override
    public boolean getIsInfinite() {
        return false;
    }

}
