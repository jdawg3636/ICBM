package com.jdawg3636.icbm.common.block.oil_refinery;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.machine.AbstractBlockMachine;
import com.jdawg3636.icbm.common.block.machine.ICBMItemStackHandler;
import com.jdawg3636.icbm.common.block.machine.TileMachineProcessing;
import com.jdawg3636.icbm.common.block.multiblock.AbstractBlockMulti;
import com.jdawg3636.icbm.common.capability.fluidhandler.ICBMFluidTank;
import com.jdawg3636.icbm.common.recipe.RefineryRecipe;
import com.jdawg3636.icbm.common.reg.ContainerReg;
import com.jdawg3636.icbm.common.reg.ICBMRecipeTypes;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Optional;

public class TileOilRefinery extends TileMachineProcessing<RefineryRecipe> {

    public static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("gui.icbm.oil_refinery");

    public static enum SlotIDs {
        OIL_INPUT,
        FUEL_OUTPUT,
        BATTERY,
    }

    private ICBMFluidTank inputTank = new ICBMFluidTank(64_000, fluidStack -> true, this::onInputTankContentsChanged, true);
    private ICBMFluidTank outputTank = new ICBMFluidTank(64_000, fluidStack -> false, tank -> this.updateNearbyClients(), true);

    public TileOilRefinery(TileEntityType<?> tileEntityType) {
        this(tileEntityType, DEFAULT_NAME);
    }

    public TileOilRefinery(TileEntityType<?> tileEntityType, ITextComponent name) {
        super(tileEntityType, ContainerReg.OIL_REFINERY::get, true, 9_000, 9_000, 0, new ArrayList<>(2), name);
        this.itemHandlerLazyOptional.orElse(null).addSlot(152 - (7 * 18) - 9, 22, ICBMItemStackHandler.SlotTag.FLUID_ITEM);
        this.itemHandlerLazyOptional.orElse(null).addSlot(152 - (2 * 18) - 9, 22, ICBMItemStackHandler.SlotTag.FLUID_ITEM);
        this.itemHandlerLazyOptional.orElse(null).addSlot(152, 22, ICBMItemStackHandler.SlotTag.BATTERY);
        this.fluidTanks.add(LazyOptional.of(() -> inputTank));
        this.fluidTanks.add(LazyOptional.of(() -> outputTank));
    }

    @Override
    public int getEnergyUsagePerProcessingTick() {
        return ICBMReference.COMMON_CONFIG.getOilRefineryEnergyUsagePerTick();
    }

    @Override
    public Optional<RefineryRecipe> getRecipeForCurrentInputs() {
        assert this.level != null;
        return RefineryRecipe.getRecipeFor(ICBMRecipeTypes.REFINERY, this.inputTank.getFluid(), this.level);
    }

    @Override
    public boolean getOutputHasRoomForRecipeResult() {
        return this.activeRecipe.map(ar -> outputTank.getSpace() >= ar.getResult().getAmount()).orElse(true);
    }

    @Override
    public boolean tryConsumeInputsForRecipe() {
        assert this.activeRecipe.isPresent();
        int amountToDrain = this.activeRecipe.get().getIngredient().getAmount();
        // This next line may have the side effect of changing the activeRecipe due to triggering the onContentsChanged callback
        boolean recipeFulfilled = this.fluidTanks.get(0).map(ft -> ft.drain(amountToDrain, IFluidHandler.FluidAction.EXECUTE).getAmount() >= amountToDrain).orElse(false);
        return recipeFulfilled;
    }

    @Override
    public void onRecipeFulfilled(Optional<RefineryRecipe> fulfilledRecipe) {
        fulfilledRecipe.ifPresent(ar -> {
            outputTank.fillBypass(ar.getResult(), IFluidHandler.FluidAction.EXECUTE);
        });
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

    public void onInputTankContentsChanged(FluidTank tank) {
        if(activeRecipe.map(rr -> !rr.getIngredient().test(tank.getFluid())).orElse(true)) {
            assert this.level != null;
            this.activeRecipe = RefineryRecipe.getRecipeFor(ICBMRecipeTypes.REFINERY, tank.getFluid(), this.level);
            this.ticksProcessing = 0;
        }
        this.updateNearbyClients();
    }

    @Override
    public void tick() {
        assert this.level != null;
        // Handle processing for active recipes
        super.tick();
        // Ingest energy from battery
        getEnergyStorageOfItemInSlot(SlotIDs.BATTERY.ordinal()).ifPresent(bes -> tryReceiveEnergy(bes, bes.getEnergyStored()));
        // Ingest fluid from input item
        itemHandlerLazyOptional.ifPresent(itemHandler -> {
            ItemStack itemStackInInputSlot = itemHandler.getStackInSlot(SlotIDs.OIL_INPUT.ordinal());
            itemStackInInputSlot.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(itemFluidHandler -> {
                FluidStack canExtract = itemFluidHandler.drain(inputTank.getSpace(), IFluidHandler.FluidAction.SIMULATE);
                int amountFilled = inputTank.fill(canExtract, IFluidHandler.FluidAction.EXECUTE);
                itemFluidHandler.drain(amountFilled, IFluidHandler.FluidAction.EXECUTE);
                itemHandler.extractItem(SlotIDs.OIL_INPUT.ordinal(), itemStackInInputSlot.getCount(), false);
                itemHandler.insertItem(SlotIDs.OIL_INPUT.ordinal(), itemFluidHandler.getContainer(), false);
            });
        });
        // Extract fluid to output item
        itemHandlerLazyOptional.ifPresent(itemHandler -> {
            ItemStack itemStackInOutputSlot = itemHandler.getStackInSlot(SlotIDs.FUEL_OUTPUT.ordinal());
            itemStackInOutputSlot.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(itemFluidHandler -> {
                int amountFilled = itemFluidHandler.fill(outputTank.getFluid(), IFluidHandler.FluidAction.EXECUTE);
                itemHandler.extractItem(SlotIDs.FUEL_OUTPUT.ordinal(), itemStackInOutputSlot.getCount(), false);
                itemHandler.insertItem(SlotIDs.FUEL_OUTPUT.ordinal(), itemFluidHandler.getContainer(), false);
                outputTank.drain(amountFilled, IFluidHandler.FluidAction.EXECUTE);
            });
        });
        // Extract fluid to outside TileEntities
        BlockPos outletPosition = ((BlockOilRefinery)getBlockState().getBlock()).getBlockPosFromOffset(BlockOilRefinery.OULTET_POSITION, getBlockPos(), getBlockState().getValue(AbstractBlockMachine.FACING));
        Optional.ofNullable(this.level.getBlockEntity(outletPosition.above())).flatMap(te -> te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.DOWN).resolve()).ifPresent(outsideFluidHandler -> {
            FluidStack canExtract = this.outputTank.drain(this.outputTank.getFluidAmount(), IFluidHandler.FluidAction.SIMULATE);
            int amountFilled = outsideFluidHandler.fill(canExtract, IFluidHandler.FluidAction.EXECUTE);
            this.outputTank.drain(amountFilled, IFluidHandler.FluidAction.EXECUTE);
        });
    }

    @Override
    public boolean isInventoryItemValid(int slot, @Nonnull ItemStack stack) {
        if(slot == SlotIDs.BATTERY.ordinal()) {
            return stack.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::canExtract).orElse(false);
        }
        if(slot == SlotIDs.OIL_INPUT.ordinal() || slot == SlotIDs.FUEL_OUTPUT.ordinal()) {
            return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent();
        }
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getBlockPos().offset(-1, 0, -1), getBlockPos().offset(2, 2, 2));
    }

}
