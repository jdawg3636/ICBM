package com.jdawg3636.icbm.common.block.metallurgic_leaching_chamber;

import com.jdawg3636.icbm.common.block.machine.TileMachine;
import com.jdawg3636.icbm.common.block.multiblock.AbstractBlockMulti;
import com.jdawg3636.icbm.common.block.oil_refinery.ContainerOilRefinery;
import com.jdawg3636.icbm.common.capability.fluidhandler.ICBMFluidTank;
import com.jdawg3636.icbm.common.reg.ContainerReg;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.ITickableTileEntity;
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
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class TileMetallurgicLeachingChamber extends TileMachine implements ITickableTileEntity {

    public static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("gui.icbm.metallurgic_leaching_chamber");

    private ICBMFluidTank inputTank = new ICBMFluidTank(64_000, fluidStack -> true, tank -> this.onInputTankContentsChanged(tank), true);

    public TileMetallurgicLeachingChamber(TileEntityType<?> tileEntityType) {
        this(tileEntityType, DEFAULT_NAME);
    }

    public TileMetallurgicLeachingChamber(TileEntityType<?> tileEntityType, ITextComponent name) {
        super(tileEntityType, ContainerReg.OIL_REFINERY::get, ContainerOilRefinery::new, 3, 9_000, 9_000, 0, new ArrayList<>(2), name);
        this.fluidTanks.add(LazyOptional.of(() -> inputTank));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        // Connect left and right faces of root to item handler
        if(itemHandlerLazyOptional.isPresent() && side == relDirectionToAbsDirection(Direction.WEST) && cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) return itemHandlerLazyOptional.cast();
        if(itemHandlerLazyOptional.isPresent() && side == relDirectionToAbsDirection(Direction.EAST) && cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) return itemHandlerLazyOptional.cast();
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

    public void onInputTankContentsChanged(FluidTank tank) {
        // TODO: Handle recipe checking/starting
        //        if(activeRecipe.map(rr -> !rr.getIngredient().test(tank.getFluid())).orElse(true)) {
//            this.activeRecipe = RefineryRecipe.getRecipeFor(ICBMRecipeTypes.REFINERY, tank.getFluid(), this.level);
//            this.ticksProcessing = 0;
//        }
        this.updateNearbyClients();
    }

    @Override
    public void tick() {}

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getBlockPos(), getBlockPos().offset(1, 2, 1));
    }

}
