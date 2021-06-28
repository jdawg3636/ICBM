package com.jdawg3636.icbm.common;

import com.jdawg3636.icbm.common.network.ICBMNetworking;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonProxy {

    public void onClientSetupEvent(final FMLClientSetupEvent event) {
    }

    public void onCommonSetupEvent(final FMLCommonSetupEvent event) {
        ICBMNetworking.init();
    }

}
