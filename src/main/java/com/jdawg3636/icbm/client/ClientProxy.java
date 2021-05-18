package com.jdawg3636.icbm.client;

import com.jdawg3636.icbm.client.render.entity.EntityPrimedExplosivesRenderer;
import com.jdawg3636.icbm.client.render.entity.EntityMissileRenderer;
import com.jdawg3636.icbm.common.blocks.launcher_platform.ScreenLauncherPlatform;
import com.jdawg3636.icbm.common.reg.BlockReg;
import com.jdawg3636.icbm.common.reg.ContainerReg;
import com.jdawg3636.icbm.common.reg.EntityReg;
import com.jdawg3636.icbm.common.CommonProxy;
import com.jdawg3636.icbm.common.reg.ItemReg;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy {

    public void onClientSetupEvent(FMLClientSetupEvent event) {

        // Set Render Layers
        RenderTypeLookup.setRenderLayer(BlockReg.GLASS_BUTTON.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlockReg.GLASS_PRESSURE_PLATE.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlockReg.LAUNCHER_PLATFORM_T3.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlockReg.REINFORCED_GLASS.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlockReg.SPIKES.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlockReg.SPIKES_POISON.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlockReg.SPIKES_FIRE.get(), RenderType.getCutout());

        // Register Container Screens
        ScreenManager.registerFactory(ContainerReg.LAUNCHER_PLATFORM_T1.get(), ScreenLauncherPlatform::new);
        ScreenManager.registerFactory(ContainerReg.LAUNCHER_PLATFORM_T2.get(), ScreenLauncherPlatform::new);
        ScreenManager.registerFactory(ContainerReg.LAUNCHER_PLATFORM_T3.get(), ScreenLauncherPlatform::new);

        // Register Explosives Entity Rendering Handlers
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_CONDENSED.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_CONDENSED.get().getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_SHRAPNEL.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_SHRAPNEL.get().getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_INCENDIARY.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_INCENDIARY.get().getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_DEBILITATION.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_DEBILITATION.get().getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_CHEMICAL.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_CHEMICAL.get().getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_ANVIL.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_ANVIL.get().getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_REPULSIVE.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_REPULSIVE.get().getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_ATTRACTIVE.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_ATTRACTIVE.get().getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_NIGHTMARE.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_NIGHTMARE.get().getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_FRAGMENTATION.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_FRAGMENTATION.get().getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_CONTAGIOUS.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_CONTAGIOUS.get().getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_SONIC.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_SONIC.get().getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_BREACHING.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_BREACHING.get().getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_REJUVENATION.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_REJUVENATION.get().getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_THERMOBARIC.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_THERMOBARIC.get().getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_NUCLEAR.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_NUCLEAR.get().getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_EMP.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_EMP.get().getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_EXOTHERMIC.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_EXOTHERMIC.get().getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_ENDOTHERMIC.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_ENDOTHERMIC.get().getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_ANTIGRAVITATIONAL.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_ANTIGRAVITATIONAL.get().getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_ENDER.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_ENDER.get().getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_HYPERSONIC.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_HYPERSONIC.get().getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_ANTIMATTER.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_ANTIMATTER.get().getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_REDMATTER.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_REDMATTER.get().getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_INCENDIARY.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_INCENDIARY.get().getDefaultState()));

        // Register Missile Entity Rendering Handlers
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_MODULE.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_MODULE.get().getDefaultInstance()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_CONVENTIONAL.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_CONVENTIONAL.get().getDefaultInstance()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_SHRAPNEL.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_SHRAPNEL.get().getDefaultInstance()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_INCENDIARY.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_INCENDIARY.get().getDefaultInstance()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_DEBILITATION.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_DEBILITATION.get().getDefaultInstance()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_CHEMICAL.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_CHEMICAL.get().getDefaultInstance()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_ANVIL.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_ANVIL.get().getDefaultInstance()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_REPULSIVE.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_REPULSIVE.get().getDefaultInstance()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_ATTRACTIVE.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_ATTRACTIVE.get().getDefaultInstance()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_NIGHTMARE.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_NIGHTMARE.get().getDefaultInstance()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_FRAGMENTATION.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_FRAGMENTATION.get().getDefaultInstance()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_CONTAGIOUS.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_CONTAGIOUS.get().getDefaultInstance()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_SONIC.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_SONIC.get().getDefaultInstance()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_BREACHING.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_BREACHING.get().getDefaultInstance()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_REJUVENATION.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_REJUVENATION.get().getDefaultInstance()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_THERMOBARIC.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_THERMOBARIC.get().getDefaultInstance()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_NUCLEAR.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_NUCLEAR.get().getDefaultInstance()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_EMP.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_EMP.get().getDefaultInstance()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_EXOTHERMIC.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_EXOTHERMIC.get().getDefaultInstance()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_ENDOTHERMIC.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_ENDOTHERMIC.get().getDefaultInstance()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_ANTIGRAVITATIONAL.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_ANTIGRAVITATIONAL.get().getDefaultInstance()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_ENDER.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_ENDER.get().getDefaultInstance()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_HYPERSONIC.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_HYPERSONIC.get().getDefaultInstance()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_ANTIMATTER.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_ANTIMATTER.get().getDefaultInstance()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_REDMATTER.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_REDMATTER.get().getDefaultInstance()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_HOMING.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_HOMING.get().getDefaultInstance()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_ANTIBALLISTIC.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_ANTIBALLISTIC.get().getDefaultInstance()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_CLUSTER.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_CLUSTER.get().getDefaultInstance()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_CLUSTER_NUCLEAR.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_CLUSTER_NUCLEAR.get().getDefaultInstance()));

    }

    public void onCommonSetupEvent(final FMLCommonSetupEvent event) {
    }

}
