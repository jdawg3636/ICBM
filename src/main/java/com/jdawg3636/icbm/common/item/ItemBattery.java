package com.jdawg3636.icbm.common.item;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.capability.energystorage.ICBMEnergyStorage;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBattery extends Item {

    public static final int ENERGY_CAPACITY = 1_000_000;
    public final boolean infinite;

    public ItemBattery(boolean infinite) {
        this(new Item.Properties().stacksTo(1).tab(ICBMReference.CREATIVE_TAB), infinite);
    }

    public ItemBattery(Properties properties, boolean infinite) {
        super(properties);
        this.infinite = infinite;
    }

    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if(this.infinite) {
            super.fillItemCategory(group, items);
        }
        else {
            if (this.allowdedIn(group)) {
                ItemStack empty = this.getDefaultInstance();
                ItemStack full = empty.copy();
                full.getCapability(CapabilityEnergy.ENERGY).ifPresent(energyStorage -> energyStorage.receiveEnergy(energyStorage.getMaxEnergyStored(), false));
                items.add(empty);
                items.add(full);
            }
        }
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        if(stack.isEmpty()) return super.initCapabilities(stack, nbt);
        return ICBMEnergyStorage.getNewCapabilityProvider(stack, (energyStorage) -> energyStorage.setCapacity(ENERGY_CAPACITY, true).setInfinite(this.infinite));
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return !this.infinite;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return stack.getCapability(CapabilityEnergy.ENERGY).map((energyStorage) -> 1.0 - ((ICBMEnergyStorage)energyStorage).getEnergyPercentage()).orElse(0.0);
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World level, List<ITextComponent> tooltip, ITooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, tooltip, tooltipFlag);
        if(!this.infinite) {
            tooltip.add(stack.getCapability(CapabilityEnergy.ENERGY).map((energyStorage) -> {
                IFormattableTextComponent stored = ((ICBMEnergyStorage) energyStorage).getEnergyStoredFormatted(2, true);
                IFormattableTextComponent slash = new StringTextComponent("/");
                IFormattableTextComponent capacity = ICBMEnergyStorage.EnergyMeasurementUnit.formatEnergyValue(energyStorage.getMaxEnergyStored(), 0, true);
                return stored.append(slash).append(capacity).withStyle(TextFormatting.GRAY);
            }).orElse(new StringTextComponent("ERROR: MISSING CAPABILITY!")));
        }
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || !oldStack.getItem().equals(newStack.getItem());
    }

    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return oldStack.getItem() != newStack.getItem();
    }

}
