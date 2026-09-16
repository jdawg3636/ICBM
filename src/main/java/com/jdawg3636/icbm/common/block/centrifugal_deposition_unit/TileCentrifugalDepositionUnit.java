package com.jdawg3636.icbm.common.block.centrifugal_deposition_unit;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.machine.ICBMItemStackHandler;
import com.jdawg3636.icbm.common.block.machine.TileMachineProcessing;
import com.jdawg3636.icbm.common.block.multiblock.AbstractBlockMulti;
import com.jdawg3636.icbm.common.capability.fluidhandler.ICBMFluidTank;
import com.jdawg3636.icbm.common.recipe.CentrifugalDepositionRecipe;
import com.jdawg3636.icbm.common.reg.ContainerReg;
import com.jdawg3636.icbm.common.reg.ICBMRecipeTypes;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiPredicate;

public class TileCentrifugalDepositionUnit extends TileMachineProcessing<CentrifugalDepositionRecipe> {

    public static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("gui.icbm.centrifugal_deposition_unit");

    public static enum MachineSlots {
        BATTERY(152, 22, ICBMItemStackHandler.SlotTag.BATTERY, (stack, level) -> {
            return stack.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::canExtract).orElse(false);
        }),
        FLUID_INPUT(152 - (6 * 18) - 9, 22, ICBMItemStackHandler.SlotTag.FLUID_ITEM, (stack, level) -> {
            return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent();
        }),
        ITEM_OUTPUT_A(152 - (2 * 18), 22 + 24 + 10, ICBMItemStackHandler.SlotTag.INVENTORY_MACHINE, (stack, level) -> false),
        ITEM_OUTPUT_B(152 - (2 * 18), 22 + 24 + 37, ICBMItemStackHandler.SlotTag.INVENTORY_MACHINE, (stack, level) -> false);

        final int xPos;
        final int yPos;
        final ICBMItemStackHandler.SlotTag tag;
        final BiPredicate<ItemStack, World> itemStackValidityPredicate;

        MachineSlots(int xPos, int yPos, ICBMItemStackHandler.SlotTag tag, BiPredicate<ItemStack, World> itemStackValidityPredicate) {
            this.xPos = xPos;
            this.yPos = yPos;
            this.tag = tag;
            this.itemStackValidityPredicate = itemStackValidityPredicate;
        }

    }

    // "Real" ticksProcessing is synced to the client, but timing over network isn't consistent enough for the animation look
    // good when interpolation using partialTicks is enabled. This variable exists as a smoothed copy calculated client-side.
    private int clientTicksProcessing = 0;
    private final ICBMFluidTank inputTank;

    public TileCentrifugalDepositionUnit(TileEntityType<?> tileEntityType) {
        this(tileEntityType, DEFAULT_NAME);
    }

    public TileCentrifugalDepositionUnit(TileEntityType<?> tileEntityType, ITextComponent name) {
        super(tileEntityType, ContainerReg.CENTRIFUGAL_DEPOSITION_UNIT::get, true, 9_000, 9_000, 0, new ArrayList<>(2), name);
        this.inputTank = new ICBMFluidTank(5_000, fluidStack -> true, this::onInputTankContentsChanged, true, 25);
        this.fluidTanks.add(LazyOptional.of(() -> inputTank));
        Arrays.stream(MachineSlots.values()).forEach(slot -> this.itemHandlerLazyOptional.orElse(null).addSlot(slot.xPos, slot.yPos, slot.tag));
    }

    public double getClientProcessingPercentage(float partialTicks) {
        if(this.clientTicksProcessing == 0) {
            return 0.0;
        }
        return Math.min(1.0, (this.clientTicksProcessing + partialTicks) / (double)clientProcessingTime);
    }

    @Override
    public Optional<CentrifugalDepositionRecipe> getRecipeForCurrentInputs() {
        assert this.level != null;
        return CentrifugalDepositionRecipe.getRecipeFor(ICBMRecipeTypes.CENTRIFUGAL_DEPOSITION, this.inputTank.getFluid(), this.level);
    }

    @Override
    public boolean getOutputHasRoomForRecipeResult() {
        return this.activeRecipe.flatMap(recipe -> this.itemHandlerLazyOptional.map(itemHandler -> {
            return outputHasRoom(itemHandler.getStackInSlot(MachineSlots.ITEM_OUTPUT_A.ordinal()), recipe.getResultA())
                    && outputHasRoom(itemHandler.getStackInSlot(MachineSlots.ITEM_OUTPUT_B.ordinal()), recipe.getResultB());
        })).orElse(true);
    }

    private boolean outputHasRoom(ItemStack output, ItemStack result) {
        if(output.isEmpty()) return true;
        int spaceRemaining = output.getMaxStackSize() - output.getCount();
        return output.sameItem(result) && spaceRemaining >= result.getCount();
    }

    @Override
    public boolean tryConsumeInputsForRecipe() {
        return this.activeRecipe.map(recipe -> {
            int amountToDrain = recipe.getInputFluid().getAmount();
            return this.inputTank.drain(amountToDrain, IFluidHandler.FluidAction.EXECUTE).getAmount() == amountToDrain;
        }).orElse(false);
    }

    @Override
    public void onRecipeFulfilled(Optional<CentrifugalDepositionRecipe> fulfilledRecipe) {
        fulfilledRecipe.ifPresent(recipe -> this.itemHandlerLazyOptional.ifPresent(itemHandler -> {
            addRecipeResult(itemHandler, MachineSlots.ITEM_OUTPUT_A, recipe.getResultA());
            addRecipeResult(itemHandler, MachineSlots.ITEM_OUTPUT_B, recipe.getResultB());
        }));
    }

    private void addRecipeResult(ICBMItemStackHandler itemHandler, MachineSlots outputSlot, ItemStack result) {
        ItemStack output = itemHandler.getStackInSlot(outputSlot.ordinal());
        if(output.sameItem(result)) {
            output.grow(result.getCount());
        }
        else {
            itemHandler.setStackInSlot(outputSlot.ordinal(), result.copy());
        }
    }

    @Override
    public void updateActiveRecipe() {
        // Deserialization callbacks can sometimes cause this to be called before the world is loaded (and thus before recipe data exists) - need to guard
        if(level == null || level.isClientSide()) return;
        // Ensure current inputs match current recipe (if present), otherwise look for different matching recipe and reset processing time.
        if(activeRecipe.map(recipe -> !recipe.matchesCustom(inputTank.getFluid())).orElse(true)) {
            this.activeRecipe = CentrifugalDepositionRecipe.getRecipeFor(ICBMRecipeTypes.CENTRIFUGAL_DEPOSITION, inputTank.getFluid(), this.level);
            this.ticksProcessing = 0;
        }
    }

    public void onInputTankContentsChanged(FluidTank tank) {
        this.updateActiveRecipe();
        this.updateNearbyClients();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return LazyOptional.empty();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapabilityViaProxy(@Nonnull Capability<T> cap, @Nullable Direction side, BlockState proxyState) {
        if(AbstractBlockMulti.doesStateMatchPosition(proxyState, BlockCentrifugalDepositionUnit.LIQUID_INLET_POSITION) && cap.equals(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)) {
            return fluidTanks.get(0).cast();
        }
        if(AbstractBlockMulti.doesStateMatchPosition(proxyState, BlockCentrifugalDepositionUnit.ITEM_OUTLET_POSITION) && cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) {
            return itemHandlerLazyOptional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public void tick() {
        super.tick();
        assert level != null;
        if(level.isClientSide()) {
            if(this.ticksProcessing > 0) {
                this.clientTicksProcessing += 1;
            }
            else {
                this.clientTicksProcessing = 0;
            }
        }
        else {
            itemHandlerLazyOptional.ifPresent(itemHandler -> {
                getEnergyStorageOfItemInSlot(MachineSlots.BATTERY.ordinal()).ifPresent(battery -> tryReceiveEnergy(battery, battery.getEnergyStored()));
                ItemStack fluidContainer = itemHandler.getStackInSlot(MachineSlots.FLUID_INPUT.ordinal());
                fluidContainer.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(fluidHandler -> {
                    FluidStack canExtract = fluidHandler.drain(inputTank.getSpace(), IFluidHandler.FluidAction.SIMULATE);
                    int amountFilled = inputTank.fill(canExtract, IFluidHandler.FluidAction.EXECUTE);
                    fluidHandler.drain(amountFilled, IFluidHandler.FluidAction.EXECUTE);
                    itemHandler.extractItem(MachineSlots.FLUID_INPUT.ordinal(), fluidContainer.getCount(), false);
                    itemHandler.insertItem(MachineSlots.FLUID_INPUT.ordinal(), fluidHandler.getContainer(), false);
                });
            });
        }
    }

    @Override
    public boolean isInventoryItemValid(int slot, @Nonnull ItemStack stack) {
        if(slot < MachineSlots.values().length) {
            return MachineSlots.values()[slot].itemStackValidityPredicate.test(stack, level);
        }
        return false;
    }

    @Override
    public int getEnergyUsagePerProcessingTick() {
        return ICBMReference.COMMON_CONFIG.getCentrifugalDepositionUnitEnergyUsagePerTick();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getBlockPos().offset(-1, 0, -1), getBlockPos().offset(2, 3, 2));
    }

}
