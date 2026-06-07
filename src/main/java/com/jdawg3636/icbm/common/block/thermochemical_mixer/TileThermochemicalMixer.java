package com.jdawg3636.icbm.common.block.thermochemical_mixer;

import com.jdawg3636.icbm.common.block.machine.ICBMItemStackHandler;
import com.jdawg3636.icbm.common.block.machine.TileMachineProcessing;
import com.jdawg3636.icbm.common.capability.fluidhandler.ICBMFluidTank;
import com.jdawg3636.icbm.common.recipe.ThermochemicalMixerRecipe;
import com.jdawg3636.icbm.common.reg.ContainerReg;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public class TileThermochemicalMixer extends TileMachineProcessing<ThermochemicalMixerRecipe> {

    public static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("gui.icbm.thermochemical_mixer");

    public static enum MachineSlots {
        BATTERY(152, 22, ICBMItemStackHandler.SlotTag.BATTERY, stack -> {
            return stack.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::canExtract).orElse(false);
        }),
        FLUID_INPUT(152 - (7 * 18) - 9, 22, ICBMItemStackHandler.SlotTag.FLUID_ITEM, stack -> {
            return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent();
        }),
        ITEM_INPUT_A(152 - (5 * 18) - 10, 69, ICBMItemStackHandler.SlotTag.INVENTORY_MACHINE, stack -> true),
        ITEM_INPUT_B(152 - (5 * 18) - 10, 69 + 18, ICBMItemStackHandler.SlotTag.INVENTORY_MACHINE, stack -> true),
        FLUID_OUTPUT(152 - (2 * 18) - 8, 69, ICBMItemStackHandler.SlotTag.INVENTORY_MACHINE, stack -> {
            return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent();
        });

        final int xPos;
        final int yPos;
        final ICBMItemStackHandler.SlotTag tag;
        final Predicate<ItemStack> itemStackValidityPredicate;

        MachineSlots(int xPos, int yPos, ICBMItemStackHandler.SlotTag tag, Predicate<ItemStack> itemStackValidityPredicate) {
            this.xPos = xPos;
            this.yPos = yPos;
            this.tag = tag;
            this.itemStackValidityPredicate = itemStackValidityPredicate;
        }

    }

    private ICBMFluidTank inputTank = new ICBMFluidTank(32_000, fluidStack -> true, this::onInputTankContentsChanged, true);
    private ICBMFluidTank outputTank = new ICBMFluidTank(32_000, fluidStack -> false, tank -> this.updateNearbyClients(), true);

    public TileThermochemicalMixer(TileEntityType<?> tileEntityType) {
        this(tileEntityType, DEFAULT_NAME);
    }

    public TileThermochemicalMixer(TileEntityType<?> tileEntityType, ITextComponent name) {
        super(tileEntityType, ContainerReg.THERMOCHEMICAL_MIXER::get, true, 9_000, 9_000, 0, new ArrayList<>(2), name);
        Arrays.stream(TileThermochemicalMixer.MachineSlots.values()).forEach(slot -> this.itemHandlerLazyOptional.orElse(null).addSlot(slot.xPos, slot.yPos, slot.tag));
        this.fluidTanks.add(LazyOptional.of(() -> inputTank));
        this.fluidTanks.add(LazyOptional.of(() -> outputTank));
    }

    public void onInputTankContentsChanged(FluidTank tank) {
        // todo implement
    }

    @Override
    public int getEnergyUsagePerProcessingTick() {
        // todo implement
        return 0;
    }

    @Override
    public Optional<ThermochemicalMixerRecipe> getRecipeForCurrentInputs() {
        // todo implement
        return Optional.empty();
    }

    @Override
    public boolean getOutputHasRoomForRecipeResult() {
        // todo implement
        return false;
    }

    @Override
    public boolean tryConsumeInputsForRecipe() {
        // todo implement
        return false;
    }

    @Override
    public void onRecipeFulfilled(Optional<ThermochemicalMixerRecipe> fulfilledRecipe) {
        // todo implement
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        // Don't allow anything to connect to front or back (inset and made of glass)
        if(side == relDirectionToAbsDirection(Direction.NORTH) || side == relDirectionToAbsDirection(Direction.SOUTH)) {
            return LazyOptional.empty();
        }
        // Connect all faces to energy
        if(energyStorageLazyOptional.isPresent() && cap.equals(CapabilityEnergy.ENERGY)) {
            return energyStorageLazyOptional.cast();
        }
        // Connect all faces to item handler
        if(itemHandlerLazyOptional.isPresent() && cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) {
            return itemHandlerLazyOptional.cast();
        }
        // Connect top and left faces to the input tank, and all other faces to the output tank
        if(cap.equals(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) && side != null) {
            if(side == Direction.UP || side == relDirectionToAbsDirection(Direction.WEST)) {
                return fluidTanks.get(0).cast();
            }
            else {
                return fluidTanks.get(1).cast();
            }
        }
        return LazyOptional.empty();
    }

    @Override
    public boolean isInventoryItemValid(int slot, @Nonnull ItemStack stack) {
        if(slot < MachineSlots.values().length) {
            return MachineSlots.values()[slot].itemStackValidityPredicate.test(stack);
        }
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getBlockPos(), getBlockPos().offset(1, 2, 1));
    }

}
