package com.jdawg3636.icbm.common.block.launcher_control_panel;

public interface ITileLaunchControlPanel {

    void setTargetX(double targetX);

    void setTargetZ(double targetZ);

    void setTargetY(double targetY);

    void setRadioFrequency(int radioFrequency);

    double getTargetX();

    double getTargetZ();

    double getTargetY();

    int getRadioFrequency();

    void launchMissile();
    
}
