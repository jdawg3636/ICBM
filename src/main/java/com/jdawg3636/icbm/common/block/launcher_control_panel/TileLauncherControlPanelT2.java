package com.jdawg3636.icbm.common.block.launcher_control_panel;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;

public class TileLauncherControlPanelT2 extends TileLauncherControlPanelT1 {

    // Tier 2
    public double targetY;

    public TileLauncherControlPanelT2(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public CompoundNBT save(CompoundNBT compoundNBT) {
        super.save(compoundNBT);
        compoundNBT.putDouble("targetY", targetY);
        return compoundNBT;
    }

    @Override
    public void load(BlockState blockState, CompoundNBT compoundNBT) {
        super.load(blockState, compoundNBT);
        targetY = compoundNBT.getDouble("targetY");
    }

    @Override
    public void setTargetY(double targetY) {
        this.targetY = targetY;
    }

    @Override
    public double getTargetY() {
        return targetY;
    }

}
