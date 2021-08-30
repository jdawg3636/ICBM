package com.jdawg3636.icbm.common.capability;

import com.jdawg3636.icbm.common.capability.blastcontroller.BlastControllerCapability;
import com.jdawg3636.icbm.common.capability.blastcontroller.IBlastControllerCapability;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class ICBMCapabilities {

    private ICBMCapabilities() {}

    @CapabilityInject(IBlastControllerCapability.class)
    public static Capability<IBlastControllerCapability> BLAST_CONTROLLER_CAPABILITY;

    public static void register() {
        BlastControllerCapability.register();
    }

}
