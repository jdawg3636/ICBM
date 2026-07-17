package com.jdawg3636.icbm.common.block.metallurgic_leaching_chamber;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.machine.ICBMItemStackHandler;
import com.jdawg3636.icbm.common.block.machine.TileMachineProcessing;
import com.jdawg3636.icbm.common.block.multiblock.AbstractBlockMulti;
import com.jdawg3636.icbm.common.capability.fluidhandler.ICBMFluidTank;
import com.jdawg3636.icbm.common.recipe.MetallurgicLeachingRecipe;
import com.jdawg3636.icbm.common.reg.ContainerReg;
import com.jdawg3636.icbm.common.reg.ICBMRecipeTypes;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
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
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public class TileMetallurgicLeachingChamber extends TileMachineProcessing<MetallurgicLeachingRecipe> {

    public static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("gui.icbm.metallurgic_leaching_chamber");

    public static enum MachineSlots {
        BATTERY(152, 22, ICBMItemStackHandler.SlotTag.BATTERY, stack -> {
            return stack.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::canExtract).orElse(false);
        }),
        FLUID_INPUT(152 - (7 * 18) - 9, 22, ICBMItemStackHandler.SlotTag.FLUID_ITEM, stack -> {
            return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent();
        }),
        ITEM_INPUT(152 - (5 * 18) - 10, 69, ICBMItemStackHandler.SlotTag.INVENTORY_MACHINE, stack -> true),
        ITEM_OUTPUT(152 - (2 * 18) - 8, 69, ICBMItemStackHandler.SlotTag.INVENTORY_MACHINE, stack -> false);

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
    public int inputTankAmountClientLerped = 0;

    public TileMetallurgicLeachingChamber(TileEntityType<?> tileEntityType) {
        this(tileEntityType, DEFAULT_NAME);
    }

    public TileMetallurgicLeachingChamber(TileEntityType<?> tileEntityType, ITextComponent name) {
        super(tileEntityType, ContainerReg.METALLURGIC_LEACHING_CHAMBER::get, true, 9_000, 9_000, 0, new ArrayList<>(1), name);
        Arrays.stream(MachineSlots.values()).forEach(slot -> this.itemHandlerLazyOptional.orElse(null).addSlot(slot.xPos, slot.yPos, slot.tag));
        this.fluidTanks.add(LazyOptional.of(() -> inputTank));
    }

    public Optional<MetallurgicLeachingRecipe> getRecipeForCurrentInputs() {
        assert this.level != null;
        return MetallurgicLeachingRecipe.getRecipeFor(ICBMRecipeTypes.METALLURGIC_LEACHING, this.inputTank.getFluid(), itemHandlerLazyOptional.map(ish -> ish.getStackInSlot(MachineSlots.ITEM_INPUT.ordinal())).orElse(ItemStack.EMPTY), this.level);
    }

    public boolean getOutputHasRoomForRecipeResult() {
        return this.activeRecipe.flatMap(ar -> {
            return this.itemHandlerLazyOptional.map(ish -> {
                ItemStack stackInOutputSlot = ish.getStackInSlot(MachineSlots.ITEM_OUTPUT.ordinal());
                if(stackInOutputSlot.isEmpty()) {
                    return true;
                }
                int spaceRemainingInOutputSlot = stackInOutputSlot.getMaxStackSize() - stackInOutputSlot.getCount();
                return stackInOutputSlot.getItem().equals(ar.getResultItem().getItem()) && (spaceRemainingInOutputSlot >= ar.getResultItem().getCount());
            });
        }).orElse(true);
    }

    public boolean tryConsumeInputsForRecipe() {
        return this.activeRecipe.map(ar -> {
            int amountToDrain = ar.getInputFluid().getAmount();
            int amountDrained = this.inputTank.drain(amountToDrain, IFluidHandler.FluidAction.EXECUTE).getAmount();
            boolean drainedEnoughFluid = amountToDrain == amountDrained;
            boolean consumedItem = this.itemHandlerLazyOptional.map(ish -> {
                ItemStack stackInInputSlot = ish.getStackInSlot(MachineSlots.ITEM_INPUT.ordinal());
                boolean isCorrectItem = ar.getInputItem().test(stackInInputSlot);
                if(isCorrectItem) {
                    stackInInputSlot.shrink(1);
                }
                return isCorrectItem;
            }).orElse(false);
            return drainedEnoughFluid && consumedItem;
        }).orElse(false);
    }

    public void onRecipeFulfilled(Optional<MetallurgicLeachingRecipe> fulfilledRecipe) {
        fulfilledRecipe.ifPresent(fr -> {
            this.itemHandlerLazyOptional.ifPresent(ish -> {
                ItemStack stackInOutputSlot = ish.getStackInSlot(MachineSlots.ITEM_OUTPUT.ordinal());
                if(stackInOutputSlot.sameItem(fr.getResultItem())) {
                    stackInOutputSlot.grow(fr.getResultItem().getCount());
                }
                else {
                    ish.setStackInSlot(MachineSlots.ITEM_OUTPUT.ordinal(), fr.getResultItem().copy());
                }
            });
        });
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        // Connect left and right faces of root to item handler
        if(itemHandlerLazyOptional.isPresent() && side == relDirectionToAbsDirection(Direction.WEST) && cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) return itemHandlerLazyOptional.cast();
        if(itemHandlerLazyOptional.isPresent() && side == relDirectionToAbsDirection(Direction.EAST) && cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) return itemHandlerLazyOptional.cast();
        // Also expose item handler for side=null
        if(itemHandlerLazyOptional.isPresent() && side == null && cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) return itemHandlerLazyOptional.cast();
        // Connect all faces of root to energy
        if(energyStorageLazyOptional.isPresent() && cap.equals(CapabilityEnergy.ENERGY)) return energyStorageLazyOptional.cast();
        return LazyOptional.empty();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapabilityViaProxy(@Nonnull Capability<T> cap, @Nullable Direction side, BlockState proxyState) {
        // Connect Inlet (top face of non-root) to fluid tank and to energy
        if(side == Direction.UP && AbstractBlockMulti.doesStateMatchPosition(proxyState, BlockMetallurgicLeachingChamber.LIQUID_INLET_POSITION)) {
            if(cap.equals(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)) {
                return fluidTanks.get(0).cast();
            }
            if(cap.equals(CapabilityEnergy.ENERGY)) {
                return energyStorageLazyOptional.cast();
            }
        }
        return LazyOptional.empty();
    }

    public void updateActiveRecipe() {
        ItemStack itemStackInInputSlot = itemHandlerLazyOptional.map(ish -> ish.getStackInSlot(2)).orElse(ItemStack.EMPTY);
        if(activeRecipe.map(rr -> !rr.matchesCustom(inputTank.getFluid(), itemStackInInputSlot)).orElse(true)) {
            assert this.level != null;
            this.activeRecipe = MetallurgicLeachingRecipe.getRecipeFor(ICBMRecipeTypes.METALLURGIC_LEACHING, inputTank.getFluid(), itemStackInInputSlot, this.level);
            this.ticksProcessing = 0;
        }
    }

    public void onInputTankContentsChanged(FluidTank tank) {
        this.updateActiveRecipe();
        this.updateNearbyClients();
    }

    @Override
    protected void onInventorySlotChanged(int slot, boolean isBeingDestroyed) {
        super.onInventorySlotChanged(slot, isBeingDestroyed);
        if(slot == MachineSlots.ITEM_INPUT.ordinal()) {
            this.updateActiveRecipe();
            this.updateNearbyClients();
        }
    }

    @Override
    public void tick() {
        super.tick();
        assert level != null;
        if(level.isClientSide()) {
            this.inputTankAmountClientLerped = (int)Math.ceil(MathHelper.lerp(1.0/40.0, this.inputTankAmountClientLerped, this.inputTank.getFluid().getAmount()));
            return;
        }
        itemHandlerLazyOptional.ifPresent(itemHandler -> {
            // Ingest energy from battery
            getEnergyStorageOfItemInSlot(MachineSlots.BATTERY.ordinal()).ifPresent(bes -> tryReceiveEnergy(bes, bes.getEnergyStored()));
            // Ingest fluid from input item
            ItemStack itemStackInInputSlot = itemHandler.getStackInSlot(MachineSlots.FLUID_INPUT.ordinal());
            itemStackInInputSlot.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(itemFluidHandler -> {
                FluidStack canExtract = itemFluidHandler.drain(inputTank.getSpace(), IFluidHandler.FluidAction.SIMULATE);
                int amountFilled = inputTank.fill(canExtract, IFluidHandler.FluidAction.EXECUTE);
                itemFluidHandler.drain(amountFilled, IFluidHandler.FluidAction.EXECUTE);
                itemHandler.extractItem(MachineSlots.FLUID_INPUT.ordinal(), itemStackInInputSlot.getCount(), false);
                itemHandler.insertItem(MachineSlots.FLUID_INPUT.ordinal(), itemFluidHandler.getContainer(), false);
            });
            // Eject output items into outside TileEntity
            Direction machineOutputDirection = relDirectionToAbsDirection(Direction.EAST);
            Optional.ofNullable(level.getBlockEntity(getBlockPos().relative(machineOutputDirection))).ifPresent(externalTile -> {
                externalTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, machineOutputDirection.getOpposite()).ifPresent(externalItemHandler -> {
                    ItemStack outputItemStack = itemHandler.getStackInSlot(MachineSlots.ITEM_OUTPUT.ordinal()).copy();
                    for(int i = 0; !outputItemStack.isEmpty() && i < externalItemHandler.getSlots(); ++i) {
                        outputItemStack = externalItemHandler.insertItem(i, outputItemStack, false);
                    }
                    if(!ItemStack.matches(outputItemStack, itemHandler.getStackInSlot(MachineSlots.ITEM_OUTPUT.ordinal()))) {
                        itemHandler.setStackInSlot(MachineSlots.ITEM_OUTPUT.ordinal(), outputItemStack);
                    }
                });
            });
        });
    }

    @Override
    public boolean isInventoryItemValid(int slot, @Nonnull ItemStack stack) {
        if(slot < MachineSlots.values().length) {
            return MachineSlots.values()[slot].itemStackValidityPredicate.test(stack);
        }
        return false;
    }

    @Override
    public int getEnergyUsagePerProcessingTick() {
        return ICBMReference.COMMON_CONFIG.getMetallurgicLeachingChamberEnergyUsagePerTick();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getBlockPos(), getBlockPos().offset(1, 2, 1));
    }

}
