package com.jdawg3636.icbm;

import com.jdawg3636.icbm.common.blast.event.ICBMBlastEventHandler;
import com.jdawg3636.icbm.common.listener.ICBMForgeEventListener;
import com.jdawg3636.icbm.common.listener.ICBMModEventListener;
import com.jdawg3636.icbm.common.reg.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ICBMReference.MODID)
public final class ICBM {

    public ICBM() {

        MinecraftForge.EVENT_BUS.register(ICBMForgeEventListener.class);
        FMLJavaModLoadingContext.get().getModEventBus().register(ICBMModEventListener.class);

        // TODO Redo this system to integrate with the main event listeners
        MinecraftForge.EVENT_BUS.register(ICBMBlastEventHandler.class);

        BlockReg.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ContainerReg.CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        EntityReg.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ItemReg.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TileReg.TILES.register(FMLJavaModLoadingContext.get().getModEventBus());

    }

}
