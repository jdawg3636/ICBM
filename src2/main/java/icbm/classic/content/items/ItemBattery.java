package icbm.classic.content.items;

import icbm.classic.config.ConfigBattery;
import icbm.classic.lib.energy.storage.EnergyBufferLimited;
import icbm.classic.prefab.item.ItemICBMBase;
import icbm.classic.prefab.item.ItemStackCapProvider;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Simple battery to move energy around between devices
 *
 *
 * Created by Dark(DarkGuardsman, Robert) on 3/21/2018.
 */
public class ItemBattery extends ItemICBMBase {

    public ItemBattery() {
        super(new Item.Properties().maxStackSize(1), "battery");
        //TODO add subtypes (Single Use battery, EMP resistant battery)
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        ItemStackCapProvider provider = new ItemStackCapProvider(stack);
        provider.add("battery", CapabilityEnergy.ENERGY, new EnergyBufferLimited(ConfigBattery.BATTERY_CAPACITY, ConfigBattery.BATTERY_INPUT_LIMIT, ConfigBattery.BATTERY_OUTPUT_LIMIT));
        return provider;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag flag) {

        if (stack.getCapability(CapabilityEnergy.ENERGY, null).isPresent()) {

            IEnergyStorage energyStorage = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);

            if (energyStorage != null) {
                double p = getDurabilityForDisplay(stack) * 100;
                list.add(new StringTextComponent("L: " + (int) p + "%"));
                list.add(new StringTextComponent("E: " + energyStorage.getEnergyStored() + "/" + energyStorage.getMaxEnergyStored() + " FE"));
            }

        }

        //TODO add info
        //TODO add shift info (input & output limits)

    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {

        if (stack.getCapability(CapabilityEnergy.ENERGY, null).isPresent()) {

            IEnergyStorage energyStorage = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);

            if (energyStorage != null)
                return energyStorage.getEnergyStored() / (double) energyStorage.getMaxEnergyStored();

        }

        return 1;

    }

}
