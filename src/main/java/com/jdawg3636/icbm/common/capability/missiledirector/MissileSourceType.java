package com.jdawg3636.icbm.common.capability.missiledirector;

import com.jdawg3636.icbm.common.event.AbstractBlastEvent;

public enum MissileSourceType {

    LAUNCHER_PLATFORM(AbstractBlastEvent.Type.PLATFORM_MISSILE),
    CRUISE_LAUNCHER(AbstractBlastEvent.Type.CRUISE_MISSILE),
    ROCKET_LAUNCHER(AbstractBlastEvent.Type.HANDHELD_ROCKET);

    MissileSourceType(final AbstractBlastEvent.Type blastType) {
        this.blastType = blastType;
    }

    private final AbstractBlastEvent.Type blastType;

    public AbstractBlastEvent.Type getResultantBlastType() {
        return blastType;
    }

}
