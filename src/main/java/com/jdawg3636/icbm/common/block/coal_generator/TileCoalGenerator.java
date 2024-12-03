package com.jdawg3636.icbm.common.block.coal_generator;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.machine.ICBMItemStackHandler;
import com.jdawg3636.icbm.common.block.machine.TileMachine;
import com.jdawg3636.icbm.common.reg.ContainerReg;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import java.util.Optional;

public class TileCoalGenerator extends TileMachine implements ITickableTileEntity {

    public static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("gui.icbm.coal_generator");

    public static enum SlotIDs {
        FUEL,
        BATTERY,
    }

    public int remainingBurnTicks = 0;
    public int totalBurnTicksForCurrentFuel = 0;

    public TileCoalGenerator(TileEntityType<?> tileEntityType) {
        this(tileEntityType, DEFAULT_NAME);
    }

    public TileCoalGenerator(TileEntityType<?> tileEntityType, ITextComponent name) {
        super(tileEntityType, ContainerReg.COAL_GENERATOR::get, ContainerCoalGenerator::new, 2, 0, 0, ICBMReference.COMMON_CONFIG.getCoalGeneratorEnergyGenerationPerTick(), name);
    }

    public Optional<IEnergyStorage> getBatteryEnergyStorage() {
        return itemHandlerLazyOptional
                .filter(ih -> SlotIDs.BATTERY.ordinal() < ih.getSlots())
                .map(ih -> ih.getStackInSlot(SlotIDs.BATTERY.ordinal()))
                .flatMap(stack -> stack.getCapability(ICBMReference.FORGE_ENERGY_CAPABILITY).resolve());
    }

    public int tryConsumeFuel() {
        return itemHandlerLazyOptional
                .map(ih -> (ICBMItemStackHandler)ih)
                .map(ih -> {
                    ItemStack extractedStack = ih.extractItem(SlotIDs.FUEL.ordinal(), 1, false);
                    // If fuel has a container (ex. lava bucket), give back the empty container.
                    if (extractedStack.hasContainerItem()) {
                        ItemStack hypotheticalLeftoverStack = ih.insertItemUnchecked(SlotIDs.FUEL.ordinal(), extractedStack.getContainerItem().getStack(), true);
                        if(hypotheticalLeftoverStack.isEmpty()) ih.insertItemUnchecked(SlotIDs.FUEL.ordinal(), extractedStack.getContainerItem().getStack(), false);
                    }
                    return extractedStack;
                })
                .map(stack -> ForgeHooks.getBurnTime(stack, IRecipeType.SMELTING))
                .orElse(0);
    }

    @Override
    public void tick() {
        assert level != null;
        if(!level.isClientSide) {
            // Consume fuel if remainingBurnTicks == 0
            if(remainingBurnTicks == 0) {
                final int burnTimeForConsumedFuel = tryConsumeFuel();
                remainingBurnTicks = burnTimeForConsumedFuel;
                totalBurnTicksForCurrentFuel = burnTimeForConsumedFuel;
                if(burnTimeForConsumedFuel != 0) updateNearbyClients();
            }
            // If currently generating, send to battery, then send excess to outside
            if(remainingBurnTicks > 0) {
                remainingBurnTicks -= 1;
                final int energyGenerated = ICBMReference.COMMON_CONFIG.getCoalGeneratorEnergyGenerationPerTick();
                final int energySentToBattery = getBatteryEnergyStorage().map(bes -> bes.receiveEnergy(energyGenerated, false)).orElse(0);
                final int energyToSendToOutside = energyGenerated - energySentToBattery;
                if(energyToSendToOutside > 0) sendEnergy(energyToSendToOutside, Direction.values());
            }
            // If not generating, attempt to extract from battery to outside
//            else {
//                final int maxEnergyToTryToSendFromBattery = ICBMReference.COMMON_CONFIG.getCoalGeneratorEnergyGenerationPerTick();
//                final int energyAvailableToSendFromBattery = getBatteryEnergyStorage().map(bes -> bes.extractEnergy(maxEnergyToTryToSendFromBattery, true)).orElse(0);
//                if(energyAvailableToSendFromBattery > 0) {
//                    final int energySentToOutside = sendEnergy(energyAvailableToSendFromBattery, Direction.values());
//                    getBatteryEnergyStorage().ifPresent(bes -> bes.extractEnergy(energySentToOutside, false));
//                }
//            }
        }
        else {
            remainingBurnTicks -= 1;
        }
    }

    @Override
    public boolean isInventoryItemValid(int slot, @Nonnull ItemStack stack) {
        if(slot == SlotIDs.BATTERY.ordinal()) {
            return stack.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::canReceive).orElse(false);
        }
        if(slot == SlotIDs.FUEL.ordinal()) {
            return ForgeHooks.getBurnTime(stack, IRecipeType.SMELTING) > 0;
        }
        return false;
    }

    public double getPercentageFuelLeft() {
        return totalBurnTicksForCurrentFuel == 0 ? 0 : remainingBurnTicks / (double)totalBurnTicksForCurrentFuel;
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        remainingBurnTicks = tag.getInt("remainingBurnTicks");
        totalBurnTicksForCurrentFuel = tag.getInt("totalBurnTicksForCurrentFuel");
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        super.save(tag);
        tag.putInt("remainingBurnTicks", remainingBurnTicks);
        tag.putInt("totalBurnTicksForCurrentFuel", totalBurnTicksForCurrentFuel);
        return tag;
    }

}
