package com.jdawg3636.icbm.common.capability.fluidhandler;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ICBMFluidTank extends FluidTank {

    private Consumer<ICBMFluidTank> onContentsChanged;
    private boolean canDrain;
    private boolean bypassValidator = true;

    public ICBMFluidTank(int capacity, Predicate<FluidStack> validator, Consumer<ICBMFluidTank> onContentsChanged, boolean canDrain) {
        super(capacity, validator);
        this.onContentsChanged = onContentsChanged;
        this.canDrain = canDrain;
    }

    @Override
    public void onContentsChanged() {
        this.onContentsChanged.accept(this);
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

}
