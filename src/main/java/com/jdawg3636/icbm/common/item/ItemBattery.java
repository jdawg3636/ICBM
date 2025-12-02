package com.jdawg3636.icbm.common.item;

import com.jdawg3636.icbm.ICBMReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.energy.CapabilityEnergy;

public class ItemBattery extends ItemEnergy {

    public static final int ENERGY_CAPACITY = 1_000_000; // todo make configurable
    public final boolean infinite;

    public ItemBattery(boolean infinite) {
        this(new Item.Properties().stacksTo(1).tab(ICBMReference.CREATIVE_TAB), infinite);
    }

    public ItemBattery(Properties properties, boolean infinite) {
        super(properties);
        this.infinite = infinite;
    }

    @Override
    public int getEnergyCapacity() {
        return ENERGY_CAPACITY;
    }

    @Override
    public boolean getIsInfinite() {
        return infinite;
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if(getIsInfinite()) {
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

}
