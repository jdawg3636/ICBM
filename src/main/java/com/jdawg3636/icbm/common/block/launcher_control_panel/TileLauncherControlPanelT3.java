package com.jdawg3636.icbm.common.block.launcher_control_panel;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;

public class TileLauncherControlPanelT3 extends TileLauncherControlPanelT2 {

    // Tier 3
    public int radioFrequency;

    public TileLauncherControlPanelT3(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public CompoundNBT save(CompoundNBT compoundNBT) {
        super.save(compoundNBT);
        compoundNBT.putInt("radioFrequency", radioFrequency);
        return compoundNBT;
    }

    @Override
    public void load(BlockState blockState, CompoundNBT compoundNBT) {
        super.load(blockState, compoundNBT);
        radioFrequency = compoundNBT.getInt("radioFrequency");
    }

    @Override
    public void setRadioFrequency(int radioFrequency) {
        this.radioFrequency = radioFrequency;
    }

    @Override
    public int getRadioFrequency() {
        return radioFrequency;
    }

}
