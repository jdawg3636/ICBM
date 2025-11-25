package com.jdawg3636.icbm.common.item;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.capability.energystorage.ICBMEnergyStorage;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemSignalDisruptor extends Item {

    public static final int ENERGY_CAPACITY = 1_000_000; // todo make configurable

    public static List<Integer> getFrequenciesDisruptedByPlayer(PlayerEntity player) {
        List<Integer> toReturn = new ArrayList<>();
        for(NonNullList<ItemStack> nonnulllist : player.inventory.compartments) {
            for(int i = 0; i < nonnulllist.size(); ++i) {
                ItemStack itemStack = nonnulllist.get(i);
                if (!(itemStack.getItem() instanceof ItemSignalDisruptor)) {
                    continue;
                }
                if (itemStack.getCapability(CapabilityEnergy.ENERGY).map(es -> es.getEnergyStored() == 0).orElse(true)) {
                    continue;
                }
                int frequencyBeingDisrupted = ((ItemSignalDisruptor)itemStack.getItem()).getFrequencyBeingDisrupted(itemStack);
                if(frequencyBeingDisrupted != 0) {
                    toReturn.add(frequencyBeingDisrupted);
                }
            }
        }
        return toReturn;
    }

    public static int getFrequencyBeingDisrupted(ItemStack itemStack) {
        if(!(itemStack.getItem() instanceof ItemSignalDisruptor)) return 0;
        // If this call fails, it does so silently and returns 0. That happens to be exactly what we want.
        return itemStack.getOrCreateTag().getInt("frequency_being_disrupted");
    }

    public static void setFrequencyBeingDisrupted(ItemStack itemStack, int frequency) {
        if(!(itemStack.getItem() instanceof ItemSignalDisruptor)) return;
        if(frequency < 0) return;
        itemStack.getOrCreateTag().putInt("frequency_being_disrupted", frequency);
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
        if(getFrequencyBeingDisrupted(itemStack) != 0) {
            itemStack.getCapability(CapabilityEnergy.ENERGY).ifPresent(energyStorage -> {
                // TODO: make energy usage per tick configurable
                energyStorage.extractEnergy(5, false);
            });
        }
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        // If on server, do nothing and consume the event.
        if(!level.isClientSide) {
            return ActionResult.sidedSuccess(player.getItemInHand(hand), false);
        }
        // Identify the slot of the held signal disruptor
        int slot = 0;
        if (hand == Hand.MAIN_HAND) {
            slot = player.inventory.selected;
            if(!PlayerInventory.isHotbarSlot(slot)) {
                // I don't know how/when this would be false, but vanilla code checks it so we will too.
                return super.use(level, player, hand);
            }
        }
        else if (hand == Hand.OFF_HAND) {
            // We could hard-code the slot index of the offhand, but it's safer to calculate it at runtime
            // in case another mod has modified the inventory in some way.
            for(NonNullList<ItemStack> compartment : player.inventory.compartments) {
                if(compartment != player.inventory.offhand) {
                    slot += compartment.size();
                } else {
                    break;
                }
            }
        }
        // Open the Signal Disruptor GUI
        ICBMReference.distProxy().setScreenSignalDisruptor(slot);
        // Return success (consume the right-click event)
        ItemStack itemStack = player.inventory.getItem(slot);
        return ActionResult.sidedSuccess(itemStack, true);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        if(stack.isEmpty()) return super.initCapabilities(stack, nbt);
        return ICBMEnergyStorage.getNewCapabilityProvider(stack, (energyStorage) -> energyStorage.setCapacity(ENERGY_CAPACITY, true));
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return stack.getCapability(CapabilityEnergy.ENERGY).map((energyStorage) -> 1.0 - ((ICBMEnergyStorage)energyStorage).getEnergyPercentage() != 0.0).orElse(false);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return stack.getCapability(CapabilityEnergy.ENERGY).map((energyStorage) -> 1.0 - ((ICBMEnergyStorage)energyStorage).getEnergyPercentage()).orElse(0.0);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World level, List<ITextComponent> tooltip, ITooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, tooltip, tooltipFlag);
        if(level == null) return;
        int frequency = getFrequencyBeingDisrupted(stack);
        if(frequency != 0) {
            tooltip.add(new TranslationTextComponent("info." + ICBMReference.MODID + ".signal_disruptor.disrupting_frequency").append(" " + frequency).withStyle(TextFormatting.GREEN));
        }
        else {
            tooltip.add(new TranslationTextComponent("info." + ICBMReference.MODID + ".signal_disruptor.not_disrupting_frequency").withStyle(TextFormatting.RED));
        }
        tooltip.add(stack.getCapability(CapabilityEnergy.ENERGY).map((energyStorage) -> {
            IFormattableTextComponent stored = ((ICBMEnergyStorage) energyStorage).getEnergyStoredFormatted(2, true);
            IFormattableTextComponent slash = new StringTextComponent("/");
            IFormattableTextComponent capacity = ICBMEnergyStorage.EnergyMeasurementUnit.formatEnergyValue(energyStorage.getMaxEnergyStored(), 0, true);
            return stored.append(slash).append(capacity).withStyle(TextFormatting.GRAY);
        }).orElse(new StringTextComponent("ERROR: MISSING CAPABILITY!")));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || !oldStack.getItem().equals(newStack.getItem());
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return oldStack.getItem() != newStack.getItem();
    }

}
