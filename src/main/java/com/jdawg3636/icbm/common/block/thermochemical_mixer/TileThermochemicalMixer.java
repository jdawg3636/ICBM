package com.jdawg3636.icbm.common.block.thermochemical_mixer;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.machine.ICBMItemStackHandler;
import com.jdawg3636.icbm.common.block.machine.TileMachineProcessing;
import com.jdawg3636.icbm.common.block.metallurgic_leaching_chamber.TileMetallurgicLeachingChamber;
import com.jdawg3636.icbm.common.capability.fluidhandler.ICBMFluidTank;
import com.jdawg3636.icbm.common.recipe.ThermochemicalMixerRecipe;
import com.jdawg3636.icbm.common.reg.ContainerReg;
import com.jdawg3636.icbm.common.reg.ICBMRecipeTypes;
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

public class TileThermochemicalMixer extends TileMachineProcessing<ThermochemicalMixerRecipe> {

    public static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("gui.icbm.thermochemical_mixer");

    public static enum MachineSlots {
        BATTERY(152, 22, ICBMItemStackHandler.SlotTag.BATTERY, (stack, level) -> {
            return stack.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::canExtract).orElse(false);
        }),
        FLUID_INPUT(152 - (7 * 18) - 9, 22, ICBMItemStackHandler.SlotTag.FLUID_ITEM, (stack, level) -> {
            return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent();
        }),
        ITEM_INPUT_A(152 - (5 * 18) - 10, 22 + 24 + 10, ICBMItemStackHandler.SlotTag.INVENTORY_MACHINE, (stack, level) -> {
            return ThermochemicalMixerRecipe.getAllRecipes(ICBMRecipeTypes.THERMOCHEMICAL_MIXER, level).anyMatch(recipe -> recipe.getInputItemA().getSingleIngredient().test(stack));
        }),
        ITEM_INPUT_B(152 - (5 * 18) - 10, 22 + 24 + 37, ICBMItemStackHandler.SlotTag.INVENTORY_MACHINE, (stack, level) -> {
            return ThermochemicalMixerRecipe.getAllRecipes(ICBMRecipeTypes.THERMOCHEMICAL_MIXER, level).anyMatch(recipe -> recipe.getInputItemB().getSingleIngredient().test(stack));
        }),
        FLUID_OUTPUT(152 - 34, 22, ICBMItemStackHandler.SlotTag.FLUID_ITEM, (stack, level) -> {
            return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent();
        });

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

    private ICBMFluidTank inputTank = new ICBMFluidTank(32_000, fluidStack -> true, this::onTankContentsChanged, true);
    private ICBMFluidTank outputTank = new ICBMFluidTank(32_000, fluidStack -> false, this::onTankContentsChanged, true);

    public TileThermochemicalMixer(TileEntityType<?> tileEntityType) {
        this(tileEntityType, DEFAULT_NAME);
    }

    public TileThermochemicalMixer(TileEntityType<?> tileEntityType, ITextComponent name) {
        super(tileEntityType, ContainerReg.THERMOCHEMICAL_MIXER::get, true, 9_000, 9_000, 0, new ArrayList<>(2), name);
        Arrays.stream(TileThermochemicalMixer.MachineSlots.values()).forEach(slot -> this.itemHandlerLazyOptional.orElse(null).addSlot(slot.xPos, slot.yPos, slot.tag));
        this.fluidTanks.add(LazyOptional.of(() -> inputTank));
        this.fluidTanks.add(LazyOptional.of(() -> outputTank));
    }

    @Override
    public void updateActiveRecipe() {
        if(level == null || level.isClientSide()) {
            return;
        }
        ItemStack itemStackInInputSlotA = itemHandlerLazyOptional.map(ish -> ish.getStackInSlot(MachineSlots.ITEM_INPUT_A.ordinal())).orElse(ItemStack.EMPTY);
        ItemStack itemStackInInputSlotB = itemHandlerLazyOptional.map(ish -> ish.getStackInSlot(MachineSlots.ITEM_INPUT_B.ordinal())).orElse(ItemStack.EMPTY);
        // If active recipe doesn't match current state of inputs (or if there is no active recipe)
        if(activeRecipe.map(rr -> !rr.matchesCustom(inputTank.getFluid(), itemStackInInputSlotA, itemStackInInputSlotB)).orElse(true)) {
            // Then set the active recipe to match current inputs and reset ticksProcessing
            assert this.level != null;
            this.activeRecipe = ThermochemicalMixerRecipe.getRecipeFor(ICBMRecipeTypes.THERMOCHEMICAL_MIXER, inputTank.getFluid(), itemStackInInputSlotA, itemStackInInputSlotB, this.level);
            this.ticksProcessing = 0;
        }
    }

    public void onTankContentsChanged(FluidTank tank) {
        this.updateActiveRecipe();
        this.updateNearbyClients();
    }

    @Override
    protected void onInventorySlotChanged(int slot, boolean isBeingDestroyed) {
        super.onInventorySlotChanged(slot, isBeingDestroyed);
        if(!isBeingDestroyed) {
            this.updateActiveRecipe();
        }
    }

    @Override
    public int getEnergyUsagePerProcessingTick() {
        return ICBMReference.COMMON_CONFIG.getThermochemicalMixerEnergyUsagePerTick();
    }

    @Override
    public Optional<ThermochemicalMixerRecipe> getRecipeForCurrentInputs() {
        ItemStack itemStackInInputSlotA = itemHandlerLazyOptional.map(ish -> ish.getStackInSlot(MachineSlots.ITEM_INPUT_A.ordinal())).orElse(ItemStack.EMPTY);
        ItemStack itemStackInInputSlotB = itemHandlerLazyOptional.map(ish -> ish.getStackInSlot(MachineSlots.ITEM_INPUT_B.ordinal())).orElse(ItemStack.EMPTY);
        assert this.level != null;
        return ThermochemicalMixerRecipe.getRecipeFor(ICBMRecipeTypes.THERMOCHEMICAL_MIXER, this.inputTank.getFluid(), itemStackInInputSlotA, itemStackInInputSlotB, this.level);
    }

    @Override
    public boolean getOutputHasRoomForRecipeResult() {
        return this.activeRecipe.map(ar -> {
            FluidStack result = ar.getOutputFluid();
            return result.getAmount() == this.outputTank.fillBypass(result, IFluidHandler.FluidAction.SIMULATE);
        }).orElse(true);
    }

    @Override
    public boolean tryConsumeInputsForRecipe() {
        return this.activeRecipe.map(ar -> {
            // Fluid
            int amountToDrain = ar.getInputFluid().getAmount();
            int amountDrained = this.inputTank.drain(amountToDrain, IFluidHandler.FluidAction.EXECUTE).getAmount();
            boolean drainedEnoughFluid = amountToDrain == amountDrained;
            // Items
            boolean consumedAllItems = this.itemHandlerLazyOptional.map(ish -> {
                // Item A
                ItemStack stackInInputSlotA = ish.getStackInSlot(MachineSlots.ITEM_INPUT_A.ordinal());
                boolean isCorrectItemA = ar.getInputItemA().test(stackInInputSlotA);
                if(isCorrectItemA) {
                    stackInInputSlotA.shrink(ar.getInputItemA().getCount());
                }
                // Item B
                ItemStack stackInInputSlotB = ish.getStackInSlot(MachineSlots.ITEM_INPUT_B.ordinal());
                boolean isCorrectItemB = ar.getInputItemB().test(stackInInputSlotB);
                if(isCorrectItemB) {
                    stackInInputSlotB.shrink(ar.getInputItemB().getCount());
                }
                // Merge Items
                return isCorrectItemA && isCorrectItemB;
            }).orElse(false);
            // Merge Fluid + Items
            return drainedEnoughFluid && consumedAllItems;
        }).orElse(false);
    }

    @Override
    public void onRecipeFulfilled(Optional<ThermochemicalMixerRecipe> fulfilledRecipe) {
        fulfilledRecipe.ifPresent(fr -> this.outputTank.fillBypass(fr.getOutputFluid(), IFluidHandler.FluidAction.EXECUTE));
    }

    @Override
    public void tick() {
        super.tick();
        assert level != null;
        if(!level.isClientSide()) {
            itemHandlerLazyOptional.ifPresent(itemHandler -> {
                // Ingest energy from battery
                getEnergyStorageOfItemInSlot(TileMetallurgicLeachingChamber.MachineSlots.BATTERY.ordinal()).ifPresent(bes -> tryReceiveEnergy(bes, bes.getEnergyStored()));
                // Ingest fluid from input item
                ItemStack itemStackInInputSlot = itemHandler.getStackInSlot(MachineSlots.FLUID_INPUT.ordinal());
                itemStackInInputSlot.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(itemFluidHandler -> {
                    FluidStack canExtract = itemFluidHandler.drain(inputTank.getSpace(), IFluidHandler.FluidAction.SIMULATE);
                    int amountFilled = inputTank.fill(canExtract, IFluidHandler.FluidAction.EXECUTE);
                    if(amountFilled != 0) {
                        itemFluidHandler.drain(amountFilled, IFluidHandler.FluidAction.EXECUTE);
                        itemHandler.extractItem(MachineSlots.FLUID_INPUT.ordinal(), itemStackInInputSlot.getCount(), false);
                        itemHandler.insertItem(MachineSlots.FLUID_INPUT.ordinal(), itemFluidHandler.getContainer(), false);
                    }
                });
                // Eject fluid into output item
                ItemStack itemStackInOutputSlot = itemHandler.getStackInSlot(MachineSlots.FLUID_OUTPUT.ordinal());
                itemStackInOutputSlot.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(itemFluidHandler -> {
                    int amountFilled = itemFluidHandler.fill(outputTank.getFluid(), IFluidHandler.FluidAction.EXECUTE);
                    if(amountFilled != 0) {
                        itemHandler.extractItem(MachineSlots.FLUID_OUTPUT.ordinal(), itemStackInOutputSlot.getCount(), false);
                        itemHandler.insertItem(MachineSlots.FLUID_OUTPUT.ordinal(), itemFluidHandler.getContainer(), false);
                        outputTank.drain(amountFilled, IFluidHandler.FluidAction.EXECUTE);
                    }
                });
            });
        }
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
        if(level != null && slot < MachineSlots.values().length) {
            return MachineSlots.values()[slot].itemStackValidityPredicate.test(stack, this.level);
        }
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getBlockPos(), getBlockPos().offset(1, 2, 1));
    }

}
