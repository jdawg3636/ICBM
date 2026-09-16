package com.jdawg3636.icbm.common.capability.fluidhandler;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ICBMFluidTank extends FluidTank {

    private final Consumer<ICBMFluidTank> onContentsChanged;
    private final int fillLimitPerTick;

    private boolean bypassValidator = false;
    private boolean canDrain;
    private int realCapacity;

    public ICBMFluidTank(int capacity, Predicate<FluidStack> validator, Consumer<ICBMFluidTank> onContentsChanged, boolean canDrain) {
        this(capacity, validator, onContentsChanged, canDrain, capacity);
    }

    public ICBMFluidTank(int capacity, Predicate<FluidStack> validator, Consumer<ICBMFluidTank> onContentsChanged, boolean canDrain, int fillLimitPerTick) {
        super(capacity, validator);
        this.onContentsChanged = onContentsChanged;
        this.canDrain = canDrain;
        this.realCapacity = capacity;
        this.capacity = fillLimitPerTick;
        this.fillLimitPerTick = fillLimitPerTick;
    }

    private void updateFakeCapacity() {
        this.capacity = Math.min(this.realCapacity, this.fluid.getAmount() + this.fillLimitPerTick);
    }

    @Override
    public FluidStack getFluid() {
        return super.getFluid().copy();
    }

    @Override
    public void setFluid(FluidStack stack) {
        super.setFluid(stack);
        this.updateFakeCapacity();
    }

    @Override
    public void onContentsChanged() {
        this.onContentsChanged.accept(this);
        this.updateFakeCapacity();
    }

    @Override
    public boolean isFluidValid(FluidStack stack) {
        return bypassValidator || super.isFluidValid(stack);
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return canDrain ? super.drain(resource, action) : FluidStack.EMPTY;
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return canDrain ? super.drain(maxDrain, action) : FluidStack.EMPTY;
    }

    public int fillBypass(FluidStack resource, FluidAction action) {
        this.bypassValidator = true;
        int result = this.fill(resource, action);
        this.bypassValidator = false;
        return result;
    }

    @Nonnull
    public FluidStack drainBypass(FluidStack resource, FluidAction action) {
        boolean canDrain = this.canDrain;
        this.canDrain = true;
        FluidStack result = this.drain(resource, action);
        this.canDrain = canDrain;
        return result;
    }

    @Nonnull
    public FluidStack drainBypass(int maxDrain, FluidAction action) {
        boolean canDrain = this.canDrain;
        this.canDrain = true;
        FluidStack result = this.drain(maxDrain, action);
        this.canDrain = canDrain;
        return result;
    }

    @Override
    public FluidTank setCapacity(int capacity) {
        this.realCapacity = capacity;
        return this;
    }

    @Override
    public int getCapacity() {
        return this.realCapacity;
    }

}
