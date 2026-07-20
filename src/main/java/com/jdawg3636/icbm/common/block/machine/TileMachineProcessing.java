package com.jdawg3636.icbm.common.block.machine;

import com.jdawg3636.icbm.common.recipe.IProcessingRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class TileMachineProcessing<T extends IProcessingRecipe> extends TileMachine implements ITickableTileEntity {

    public int ticksProcessing = 0;
    public Optional<T> activeRecipe = Optional.empty();
    public int clientProcessingTime = 1;

    public TileMachineProcessing(TileEntityType<?> tileEntityTypeIn, Supplier<ContainerType<? extends ContainerMachine>> containerType, boolean hasItemStackHandler, int forgeEnergyCapacity, int forgeEnergyPerTickIn, int forgeEnergyPerTickOut, ArrayList<LazyOptional<FluidTank>> fluidTanks, ITextComponent defaultName) {
        super(tileEntityTypeIn, containerType, hasItemStackHandler, forgeEnergyCapacity, forgeEnergyPerTickIn, forgeEnergyPerTickOut, fluidTanks, defaultName);
    }

    @Override
    public void tick() {
        assert this.level != null;
        // Handle processing for active recipe
        if(!this.level.isClientSide && this.activeRecipe.isPresent()) {
            // Perform processing if requirements are met
            boolean haveRoomForOutput = getOutputHasRoomForRecipeResult();
            if(haveRoomForOutput && tryConsumeEnergy(getEnergyUsagePerProcessingTick())) {
                this.ticksProcessing += 1;
                updateNearbyClients();
            }
            // If processing is complete, produce result.
            if(this.ticksProcessing >= this.activeRecipe.get().getProcessingTime()) {
                // This next line may have the side effect of changing the activeRecipe due to triggering the onContentsChanged callback on a FluidTank
                Optional<T> fulfilledRecipe = activeRecipe;
                boolean recipeFulfilled = tryConsumeInputsForRecipe();
                if(recipeFulfilled) {
                    onRecipeFulfilled(fulfilledRecipe);
                }
                this.ticksProcessing = 0;
                updateNearbyClients();
            }
        }
    }

    public void setChanged() {
        super.setChanged();
        this.updateActiveRecipe();
    }

    public double getProcessingPercentage() {
        return this.ticksProcessing / (double)clientProcessingTime;
    }

    @Override
    public void setLevelAndPosition(World level, BlockPos worldPosition) {
        super.setLevelAndPosition(level, worldPosition);
        this.activeRecipe = getRecipeForCurrentInputs();
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        this.ticksProcessing = tag.getInt("ticks_processing");
        this.clientProcessingTime = tag.getInt("processing_time");
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        super.save(tag);
        tag.putInt("ticks_processing", ticksProcessing);
        tag.putInt("processing_time", activeRecipe.map(IProcessingRecipe::getProcessingTime).orElse(1));
        return tag;
    }

    public abstract void updateActiveRecipe();

    public abstract int getEnergyUsagePerProcessingTick();

    public abstract Optional<T> getRecipeForCurrentInputs();

    public abstract boolean getOutputHasRoomForRecipeResult();

    public abstract boolean tryConsumeInputsForRecipe();

    public abstract void onRecipeFulfilled(Optional<T> fulfilledRecipe);

}
