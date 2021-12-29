package com.jdawg3636.icbm.common.capability;

import com.jdawg3636.icbm.common.capability.blastcontroller.BlastControllerCapability;
import com.jdawg3636.icbm.common.capability.blastcontroller.IBlastControllerCapability;
import com.jdawg3636.icbm.common.capability.trackingmanager.ITrackingManagerCapability;
import com.jdawg3636.icbm.common.capability.trackingmanager.TrackingManagerCapability;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class ICBMCapabilities {

    private ICBMCapabilities() {}

    @CapabilityInject(IBlastControllerCapability.class)
    public static Capability<IBlastControllerCapability> BLAST_CONTROLLER_CAPABILITY;

    @CapabilityInject(ITrackingManagerCapability.class)
    public static Capability<ITrackingManagerCapability> TRACKING_MANAGER_CAPABILITY;

    public static void register() {
        BlastControllerCapability.register();
        TrackingManagerCapability.register();
    }

}
