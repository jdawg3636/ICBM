package com.jdawg3636.icbm.common;

import com.jdawg3636.icbm.common.block.launcher_control_panel.TileLauncherControlPanel;
import com.jdawg3636.icbm.common.network.ICBMNetworking;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonProxy {

    // Client Events
    public void onClientSetupEvent(final FMLClientSetupEvent event) {}
    public void onModelRegistryEvent(final ModelRegistryEvent event) {}

    // Client Misc
    public void setScreenLauncherControlPanel(TileLauncherControlPanel tileEntity) {}
    public void updateScreenLauncherControlPanel() {}

    // Common Events
    public void onCommonSetupEvent(final FMLCommonSetupEvent event) {
        ICBMNetworking.init();
    }

}
