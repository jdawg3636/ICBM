package com.jdawg3636.icbm.common.block.launcher_control_panel;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;

public class TileLauncherControlPanelT1 extends TileLauncherControlPanel {

    // Tier 1
    private double targetX;
    private double targetZ;

    // TODO Needs to accept (and store) Forge Energy

    public TileLauncherControlPanelT1(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public CompoundNBT save(CompoundNBT compoundNBT) {
        super.save(compoundNBT);
        compoundNBT.putDouble("targetX", targetX);
        compoundNBT.putDouble("targetZ", targetZ);
        return compoundNBT;
    }

    @Override
    public void load(BlockState blockState, CompoundNBT compoundNBT) {
        super.load(blockState, compoundNBT);
        targetX = compoundNBT.getDouble("targetX");
        targetZ = compoundNBT.getDouble("targetZ");
    }

    @Override
    public void setTargetX(double targetX) {
        this.targetX = targetX;
    }

    @Override
    public void setTargetZ(double targetZ) {
        this.targetZ = targetZ;
    }

    @Override
    public double getTargetX() {
        return targetX;
    }

    @Override
    public double getTargetZ() {
        return targetZ;
    }

}
