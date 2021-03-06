package com.jdawg3636.icbm.client;

import com.jdawg3636.icbm.client.render.entity.EntityExplosivesIncendiaryRenderer;
import com.jdawg3636.icbm.common.gui.ScreenLauncherPlatform;
import com.jdawg3636.icbm.common.reg.BlockReg;
import com.jdawg3636.icbm.common.reg.ContainerReg;
import com.jdawg3636.icbm.common.reg.EntityReg;
import com.jdawg3636.icbm.common.CommonProxy;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy {

    public void onClientSetupEvent() {
        // Set Render Layers
        RenderTypeLookup.setRenderLayer(BlockReg.GLASS_BUTTON.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlockReg.GLASS_PRESSURE_PLATE.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlockReg.REINFORCED_GLASS.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlockReg.SPIKES.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlockReg.SPIKES_POISON.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlockReg.SPIKES_FIRE.get(), RenderType.getCutout());

        // Register Entity Rendering Handlers
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_INCENDIARY.get(), EntityExplosivesIncendiaryRenderer::new);

        // Register Container Screens
        ScreenManager.registerFactory(ContainerReg.LAUNCHER_PLATFORM_T1.get(), ScreenLauncherPlatform::new);
    }

}
