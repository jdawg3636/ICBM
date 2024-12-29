package com.jdawg3636.icbm.common.block.oil_refinery;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.machine.AbstractBlockMachine;
import com.jdawg3636.icbm.common.block.machine.TileMachine;
import com.jdawg3636.icbm.common.block.multiblock.AbstractBlockMulti;
import com.jdawg3636.icbm.common.reg.ContainerReg;
import com.jdawg3636.icbm.common.reg.ICBMTags;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
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
import java.util.Optional;

public class TileOilRefinery extends TileMachine implements ITickableTileEntity {

    public static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("gui.icbm.oil_refinery");

    public static enum SlotIDs {
        OIL_INPUT,
        FUEL_OUTPUT,
        BATTERY,
    }

    private FluidTank inputTank = new FluidTank(64_000);
    private FluidTank outputTank = new FluidTank(64_000);

    public int remainingBurnTicks = 0;
    public int totalBurnTicksForCurrentFuel = 0;

    public TileOilRefinery(TileEntityType<?> tileEntityType) {
        this(tileEntityType, DEFAULT_NAME);
    }

    public TileOilRefinery(TileEntityType<?> tileEntityType, ITextComponent name) {
        super(tileEntityType, ContainerReg.OIL_REFINERY::get, ContainerOilRefinery::new, 3, 9_000, 9_000, 0, new ArrayList<>(2), name);
        this.fluidTanks.add(LazyOptional.of(() -> inputTank));
        this.fluidTanks.add(LazyOptional.of(() -> outputTank));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(itemHandlerLazyOptional.isPresent() && cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) return itemHandlerLazyOptional.cast();
        return LazyOptional.empty();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapabilityViaProxy(@Nonnull Capability<T> cap, @Nullable Direction side, BlockState proxyState) {
        // Accept Energy from Rear Power Port
        if(
            AbstractBlockMulti.doesStateMatchPosition(proxyState, BlockOilRefinery.POWER_POSITION) &&
            side == getBlockState().getValue(AbstractBlockMachine.FACING).getOpposite() &&
            energyStorageLazyOptional.isPresent() && cap.equals(CapabilityEnergy.ENERGY)
        ) {
            return energyStorageLazyOptional.cast();
        }
        // Connect Inlet (Oil) to Tank 0
        if(
            AbstractBlockMulti.doesStateMatchPosition(proxyState, BlockOilRefinery.INLET_POSITION) &&
            side == Direction.UP &&
            cap.equals(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        ) {
            return fluidTanks.get(0).cast();
        }
        // Connect Outlet (Fuel) to Tank 1
        if(
            AbstractBlockMulti.doesStateMatchPosition(proxyState, BlockOilRefinery.OULTET_POSITION) &&
            side == Direction.UP &&
            cap.equals(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        ) {
            return fluidTanks.get(1).cast();
        }
        return LazyOptional.empty();
    }

    public Optional<IEnergyStorage> getBatteryEnergyStorage() {
        return itemHandlerLazyOptional
                .filter(ih -> SlotIDs.BATTERY.ordinal() < ih.getSlots())
                .map(ih -> ih.getStackInSlot(SlotIDs.BATTERY.ordinal()))
                .flatMap(stack -> stack.getCapability(ICBMReference.FORGE_ENERGY_CAPABILITY).resolve());
    }

    @Override
    public void tick() {
        assert level != null;
        if(!level.isClientSide) {
            // todo
        }
    }

    @Override
    public boolean isInventoryItemValid(int slot, @Nonnull ItemStack stack) {
        if(slot == SlotIDs.BATTERY.ordinal()) {
            return stack.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::canReceive).orElse(false);
        }
        if(slot == SlotIDs.OIL_INPUT.ordinal()) {
            return ICBMReference.checkItemStackContainsFluidTag(stack, ICBMTags.Fluids.OIL.getName());
        }
        if(slot == SlotIDs.FUEL_OUTPUT.ordinal()) {
            return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent();
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
