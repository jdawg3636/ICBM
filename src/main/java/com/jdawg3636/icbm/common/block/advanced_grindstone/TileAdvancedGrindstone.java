package com.jdawg3636.icbm.common.block.advanced_grindstone;

import com.jdawg3636.icbm.common.block.machine.ICBMItemStackHandler;
import com.jdawg3636.icbm.common.block.machine.TileMachineProcessing;
import com.jdawg3636.icbm.common.recipe.AdvancedGrindstoneRecipe;
import com.jdawg3636.icbm.common.reg.ContainerReg;
import com.jdawg3636.icbm.common.reg.ICBMRecipeTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiPredicate;

public class TileAdvancedGrindstone extends TileMachineProcessing<AdvancedGrindstoneRecipe> {

    public static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("gui.icbm.advanced_grindstone");

    public enum MachineSlots {
        INPUT((stack, tile) -> {
            return AdvancedGrindstoneRecipe.getAllRecipes(ICBMRecipeTypes.ADVANCED_GRINDSTONE, tile.level).anyMatch(recipe -> recipe.getInput().test(stack));
        }),
        ABRASIVE((stack, tile) -> {
            return AdvancedGrindstoneRecipe.getAllRecipes(ICBMRecipeTypes.ADVANCED_GRINDSTONE, tile.level).anyMatch(recipe -> recipe.getAbrasive().test(stack));
        }),
        OUTPUT((stack, tile) -> false);

        final BiPredicate<ItemStack, TileAdvancedGrindstone> itemStackValidityPredicate;

        MachineSlots(BiPredicate<ItemStack, TileAdvancedGrindstone> itemStackValidityPredicate) {
            this.itemStackValidityPredicate = itemStackValidityPredicate;
        }
    }

    private float clientSpinProgress;
    private float previousClientSpinProgress;
    private float clientSpinTarget;
    private int lastSyncedProcessingTicks;
    private boolean clientSpinInitialized;

    public TileAdvancedGrindstone(TileEntityType<?> type) {
        super(type, ContainerReg.ADVANCED_GRINDSTONE::get, true, 0, 0, 0, new ArrayList<>(), DEFAULT_NAME);
        itemHandlerLazyOptional.ifPresent(handler -> {
            handler.addSlot(52, 46, ICBMItemStackHandler.SlotTag.INVENTORY_MACHINE);
            handler.addSlot(79, 73, ICBMItemStackHandler.SlotTag.INVENTORY_MACHINE, 1);
            handler.addSlot(106, 46, ICBMItemStackHandler.SlotTag.INVENTORY_MACHINE);
        });
    }

    @Override
    public void tick() {
        if (level == null || !level.isClientSide) return;

        previousClientSpinProgress = clientSpinProgress;
        if (!clientSpinInitialized) {
            clientSpinProgress = ticksProcessing;
            previousClientSpinProgress = ticksProcessing;
            clientSpinTarget = ticksProcessing;
            lastSyncedProcessingTicks = ticksProcessing;
            clientSpinInitialized = true;
        }
        else if (ticksProcessing != lastSyncedProcessingTicks) {
            if (ticksProcessing < lastSyncedProcessingTicks && lastSyncedProcessingTicks > AdvancedGrindstoneRecipe.INTERACTIONS_REQUIRED / 2) {
                clientSpinTarget += AdvancedGrindstoneRecipe.INTERACTIONS_REQUIRED - lastSyncedProcessingTicks + ticksProcessing;
            }
            else if (ticksProcessing >= lastSyncedProcessingTicks) {
                clientSpinTarget += ticksProcessing - lastSyncedProcessingTicks;
            }
            else {
                clientSpinProgress = ticksProcessing;
                previousClientSpinProgress = ticksProcessing;
                clientSpinTarget = ticksProcessing;
            }
            lastSyncedProcessingTicks = ticksProcessing;
        }

        float remaining = clientSpinTarget - clientSpinProgress;
        clientSpinProgress += Math.min(0.25F, Math.max(0.0F, remaining));
    }

    public float getClientSpinProgress(float partialTicks) {
        return previousClientSpinProgress + (clientSpinProgress - previousClientSpinProgress) * partialTicks;
    }

    public boolean grind() {
        if (level == null || level.isClientSide || !activeRecipe.isPresent() || !getOutputHasRoomForRecipeResult()) {
            return false;
        }
        ticksProcessing++;
        if (ticksProcessing >= AdvancedGrindstoneRecipe.INTERACTIONS_REQUIRED) {
            Optional<AdvancedGrindstoneRecipe> fulfilled = activeRecipe;
            if (tryConsumeInputsForRecipe()) {
                onRecipeFulfilled(fulfilled);
            }
            ticksProcessing = 0;
        }
        setChanged();
        return true;
    }

    @Override
    public void updateActiveRecipe() {
        if (level == null || level.isClientSide) return;
        Optional<AdvancedGrindstoneRecipe> next = getRecipeForCurrentInputs();
        if (!next.map(recipe -> activeRecipe.map(active -> active.getId().equals(recipe.getId())).orElse(false)).orElse(!activeRecipe.isPresent())) ticksProcessing = 0;
        activeRecipe = next;
    }

    @Override
    protected void onInventorySlotChanged(int slot, boolean isBeingDestroyed) {
        super.onInventorySlotChanged(slot, isBeingDestroyed);
        if (!isBeingDestroyed) {
            updateActiveRecipe();
        }
    }

    @Override
    public int getEnergyUsagePerProcessingTick() {
        return 0;
    }

    @Override
    public Optional<AdvancedGrindstoneRecipe> getRecipeForCurrentInputs() {
        if (level == null) return Optional.empty();
        return itemHandlerLazyOptional.map(itemHandler -> AdvancedGrindstoneRecipe.getRecipeFor(itemHandler.getStackInSlot(MachineSlots.INPUT.ordinal()), itemHandler.getStackInSlot(MachineSlots.ABRASIVE.ordinal()), level)).orElse(Optional.empty());
    }

    @Override
    public boolean getOutputHasRoomForRecipeResult() {
        return activeRecipe.map(recipe -> itemHandlerLazyOptional.map(h -> {
            ItemStack output = h.getStackInSlot(MachineSlots.OUTPUT.ordinal());
            ItemStack result = recipe.getResultItem();
            return output.isEmpty() || (ItemStack.isSame(output, result) && ItemStack.tagMatches(output, result) && output.getCount() + result.getCount() <= output.getMaxStackSize());
        }).orElse(false)).orElse(false);
    }

    @Override
    public boolean tryConsumeInputsForRecipe() {
        return activeRecipe.map(recipe -> itemHandlerLazyOptional.map(h -> {
            ItemStack input = h.getStackInSlot(MachineSlots.INPUT.ordinal());
            ItemStack abrasive = h.getStackInSlot(MachineSlots.ABRASIVE.ordinal());
            if (!recipe.matchesCustom(input, abrasive)) return false;
            input.shrink(1);
            return true;
        }).orElse(false)).orElse(false);
    }

    @Override
    public void onRecipeFulfilled(Optional<AdvancedGrindstoneRecipe> fulfilledRecipe) {
        fulfilledRecipe.ifPresent(recipe -> itemHandlerLazyOptional.ifPresent(h -> h.insertItemUnchecked(MachineSlots.OUTPUT.ordinal(), recipe.getResultItem(), false)));
    }

    @Override
    public boolean isInventoryItemValid(int slot, ItemStack stack) {
        if (level != null && slot >= 0 && slot < MachineSlots.values().length) {
            return MachineSlots.values()[slot].itemStackValidityPredicate.test(stack, this);
        }
        return false;
    }

}
