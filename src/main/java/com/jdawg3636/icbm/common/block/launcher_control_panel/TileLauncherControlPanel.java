package com.jdawg3636.icbm.common.block.launcher_control_panel;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public abstract class TileLauncherControlPanel extends TileEntity {

    public TileLauncherControlPanel(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public void setTargetX(double targetX) {}

    public void setTargetZ(double targetZ) {}

    public void setTargetY(double targetY) {}

    public void setRadioFrequency(int radioFrequency) {}

    public double getTargetX() {
        return 0D;
    }

    public double getTargetZ() {
        return 0D;
    }

    public double getTargetY() {
        return 63D;
    }

    public int getRadioFrequency() {
        return 0;
    }

}
