package com.jdawg3636.icbm.common.listener;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.camouflage.BlockCamouflage;
import com.jdawg3636.icbm.common.block.camouflage.DynamicBakedModelCamouflage;
import com.jdawg3636.icbm.common.block.camouflage.TileCamouflage;
import com.jdawg3636.icbm.common.block.coal_generator.ContainerCoalGenerator;
import com.jdawg3636.icbm.common.block.coal_generator.ScreenCoalGenerator;
import com.jdawg3636.icbm.common.block.cruise_launcher.ContainerCruiseLauncher;
import com.jdawg3636.icbm.common.block.cruise_launcher.ScreenCruiseLauncher;
import com.jdawg3636.icbm.common.block.cruise_launcher.TERCruiseLauncher;
import com.jdawg3636.icbm.common.block.cruise_launcher.TileCruiseLauncher;
import com.jdawg3636.icbm.common.block.emp_tower.ContainerEMPTower;
import com.jdawg3636.icbm.common.block.emp_tower.ScreenEMPTower;
import com.jdawg3636.icbm.common.block.emp_tower.TEREMPTower;
import com.jdawg3636.icbm.common.block.emp_tower.TileEMPTower;
import com.jdawg3636.icbm.common.block.launcher_control_panel.ScreenLauncherControlPanel;
import com.jdawg3636.icbm.common.block.launcher_control_panel.TileLauncherControlPanel;
import com.jdawg3636.icbm.common.block.launcher_platform.ScreenLauncherPlatform;
import com.jdawg3636.icbm.common.block.machine.IScreenMachine;
import com.jdawg3636.icbm.common.block.oil_refinery.ContainerOilRefinery;
import com.jdawg3636.icbm.common.block.oil_refinery.ScreenOilRefinery;
import com.jdawg3636.icbm.common.block.oil_refinery.TEROilRefinery;
import com.jdawg3636.icbm.common.block.oil_refinery.TileOilRefinery;
import com.jdawg3636.icbm.common.block.particle_accelerator.ScreenParticleAccelerator;
import com.jdawg3636.icbm.common.block.radar_station.TERRadarStation;
import com.jdawg3636.icbm.common.block.radar_station.TileRadarStation;
import com.jdawg3636.icbm.common.entity.*;
import com.jdawg3636.icbm.common.item.ItemTracker;
import com.jdawg3636.icbm.common.particle.ColoredSmokeParticle;
import com.jdawg3636.icbm.common.particle.EnderParticle;
import com.jdawg3636.icbm.common.reg.*;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.entity.TNTMinecartRenderer;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientProxy extends CommonProxy {

    // BlockEntity Models
    public static final ResourceLocation MODEL_CRUISE_LAUNCHER_DYNAMIC    = new ResourceLocation(ICBMReference.MODID + ":block/cruise_launcher_dynamic");
    public static final ResourceLocation MODEL_CRUISE_LAUNCHER_STATIC     = new ResourceLocation(ICBMReference.MODID + ":block/cruise_launcher_static");
    public static final ResourceLocation MODEL_EMP_TOWER_CLOCKWISE        = new ResourceLocation(ICBMReference.MODID + ":block/emp_tower_clockwise");
    public static final ResourceLocation MODEL_EMP_TOWER_COUNTERCLOCKWISE = new ResourceLocation(ICBMReference.MODID + ":block/emp_tower_counterclockwise");
    public static final ResourceLocation MODEL_EMP_TOWER_STATIC           = new ResourceLocation(ICBMReference.MODID + ":block/emp_tower_static");
    public static final ResourceLocation MODEL_RADAR_STATION_DYNAMIC      = new ResourceLocation(ICBMReference.MODID + ":block/radar_station_dynamic");
    public static final ResourceLocation MODEL_RADAR_STATION_STATIC       = new ResourceLocation(ICBMReference.MODID + ":block/radar_station_static");

    // Entity Models
    public static final ResourceLocation MODEL_ENDER_BLAST_SPHERE             = new ResourceLocation(ICBMReference.MODID + ":entity/ender_blast_sphere");
    public static final ResourceLocation MODEL_REDMATTER_BLAST_ACCRETION_DISK = new ResourceLocation(ICBMReference.MODID + ":entity/redmatter_blast_accretion_disk");
    public static final ResourceLocation MODEL_REDMATTER_BLAST_SPHERE         = new ResourceLocation(ICBMReference.MODID + ":entity/redmatter_blast_sphere");

    // Other Models
    public static final ResourceLocation MODEL_CAMOUFLAGE_DEFAULT = new ResourceLocation(ICBMReference.MODID + ":block/camouflage_default");

    @SuppressWarnings("unchecked")
    public void onClientSetupEvent(FMLClientSetupEvent event) {

        // Set Render Layers
        RenderTypeLookup.setRenderLayer(BlockReg.CAMOUFLAGE.get(), renderType -> true);
        RenderTypeLookup.setRenderLayer(BlockReg.ELECTROMAGNETIC_GLASS.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockReg.GLASS_BUTTON.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockReg.GLASS_PRESSURE_PLATE.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockReg.LAUNCHER_PLATFORM_T3.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockReg.LAUNCHER_SUPPORT_FRAME_T1.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockReg.LAUNCHER_SUPPORT_FRAME_T2.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockReg.LAUNCHER_SUPPORT_FRAME_T3.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockReg.OIL_REFINERY.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockReg.REINFORCED_GLASS.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockReg.RUBBER_LEAVES.get(), RenderType.cutoutMipped());
        RenderTypeLookup.setRenderLayer(BlockReg.RUBBER_SAPLING.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockReg.SPIKES.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockReg.SPIKES_POISON.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockReg.SPIKES_FIRE.get(), RenderType.cutout());

        // Register Container Screens
        //noinspection RedundantTypeArguments
        ScreenManager.<ContainerCoalGenerator, ScreenCoalGenerator>register(ContainerReg.COAL_GENERATOR.get(), ScreenCoalGenerator::new);
        ScreenManager.<ContainerCruiseLauncher, ScreenCruiseLauncher>register(ContainerReg.CRUISE_LAUNCHER.get(), ScreenCruiseLauncher::new);
        ScreenManager.<ContainerEMPTower, ScreenEMPTower>register(ContainerReg.EMP_TOWER.get(), ScreenEMPTower::new);
        ScreenManager.<ContainerOilRefinery, ScreenOilRefinery>register(ContainerReg.OIL_REFINERY.get(), ScreenOilRefinery::new);
        ScreenManager.register(ContainerReg.LAUNCHER_PLATFORM_T1.get(), ScreenLauncherPlatform::new);
        ScreenManager.register(ContainerReg.LAUNCHER_PLATFORM_T2.get(), ScreenLauncherPlatform::new);
        ScreenManager.register(ContainerReg.LAUNCHER_PLATFORM_T3.get(), ScreenLauncherPlatform::new);
        ScreenManager.register(ContainerReg.PARTICLE_ACCELERATOR.get(), ScreenParticleAccelerator::new);

        // Register Explosives Entity Rendering Handlers
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_CONDENSED.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_CONDENSED.get().defaultBlockState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_SHRAPNEL.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_SHRAPNEL.get().defaultBlockState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_INCENDIARY.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_INCENDIARY.get().defaultBlockState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_DEBILITATION.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_DEBILITATION.get().defaultBlockState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_CHEMICAL.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_CHEMICAL.get().defaultBlockState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_ANVIL.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_ANVIL.get().defaultBlockState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_REPULSIVE.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_REPULSIVE.get().defaultBlockState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_ATTRACTIVE.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_ATTRACTIVE.get().defaultBlockState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_NIGHTMARE.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_NIGHTMARE.get().defaultBlockState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_FRAGMENTATION.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_FRAGMENTATION.get().defaultBlockState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_CONTAGION.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_CONTAGION.get().defaultBlockState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_SONIC.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_SONIC.get().defaultBlockState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_BREACHING.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_BREACHING.get().defaultBlockState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_REJUVENATION.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_REJUVENATION.get().defaultBlockState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_THERMOBARIC.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_THERMOBARIC.get().defaultBlockState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_NUCLEAR.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_NUCLEAR.get().defaultBlockState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_EMP.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_EMP.get().defaultBlockState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_EXOTHERMIC.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_EXOTHERMIC.get().defaultBlockState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_ENDOTHERMIC.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_ENDOTHERMIC.get().defaultBlockState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_ANTIGRAVITATIONAL.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_ANTIGRAVITATIONAL.get().defaultBlockState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_ENDER.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_ENDER.get().defaultBlockState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_HYPERSONIC.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_HYPERSONIC.get().defaultBlockState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_ANTIMATTER.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_ANTIMATTER.get().defaultBlockState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.EXPLOSIVES_REDMATTER.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.EXPLOSIVES_REDMATTER.get().defaultBlockState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.S_MINE.get(), (manager) -> new EntityPrimedExplosivesRenderer(manager, BlockReg.S_MINE.get().defaultBlockState()));

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
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MISSILE_CONTAGION.get(), (manager) -> new EntityMissileRenderer(manager, ItemReg.MISSILE_CONTAGION.get().getDefaultInstance()));
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

        // Register Grenade Entity Rendering Handlers
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.GRENADE_CONVENTIONAL.get(), (manager) -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.GRENADE_SHRAPNEL.get(), (manager) -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.GRENADE_INCENDIARY.get(), (manager) -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.GRENADE_DEBILITATION.get(), (manager) -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.GRENADE_CHEMICAL.get(), (manager) -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.GRENADE_ANVIL.get(), (manager) -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.GRENADE_REPULSIVE.get(), (manager) -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer()));
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.GRENADE_ATTRACTIVE.get(), (manager) -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer()));

        // Register Minecart Entity Rendering Handlers
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MINECART_EXPLOSIVE.get(), TNTMinecartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MINECART_SHRAPNEL.get(), TNTMinecartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MINECART_INCENDIARY.get(), TNTMinecartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MINECART_DEBILITATION.get(), TNTMinecartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MINECART_CHEMICAL.get(), TNTMinecartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MINECART_ANVIL.get(), TNTMinecartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MINECART_REPULSIVE.get(), TNTMinecartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MINECART_ATTRACTIVE.get(), TNTMinecartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MINECART_NIGHTMARE.get(), TNTMinecartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MINECART_FRAGMENTATION.get(), TNTMinecartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MINECART_CONTAGION.get(), TNTMinecartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MINECART_SONIC.get(), TNTMinecartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MINECART_BREACHING.get(), TNTMinecartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MINECART_REJUVENATION.get(), TNTMinecartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MINECART_THERMOBARIC.get(), TNTMinecartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MINECART_NUCLEAR.get(), TNTMinecartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MINECART_EMP.get(), TNTMinecartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MINECART_EXOTHERMIC.get(), TNTMinecartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MINECART_ENDOTHERMIC.get(), TNTMinecartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MINECART_ANTIGRAVITATIONAL.get(), TNTMinecartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MINECART_ENDER.get(), TNTMinecartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MINECART_HYPERSONIC.get(), TNTMinecartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MINECART_ANTIMATTER.get(), TNTMinecartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.MINECART_REDMATTER.get(), TNTMinecartRenderer::new);

        // Register Blast Utility Entity Rendering Handlers
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.BLAST_CHEMICAL.get(), EntityNOPRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.BLAST_CONTAGION.get(), EntityNOPRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.BLAST_DEBILITATION.get(), EntityNOPRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.BLAST_ENDER.get(), EntityLingeringBlastEnderRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.BLAST_RADIATION.get(), EntityNOPRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.BLAST_REDMATTER.get(), EntityRedmatterBlastRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.BLAST_SONIC.get(), EntityNOPRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.SHRAPNEL.get(), EntityShrapnelRenderer::new);

        // Register Other Entity Rendering Handlers
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.ACCELERATING_PARTICLE.get(), EntityAcceleratingParticleRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReg.LIGHTNING_VISUAL.get(), EntityLightningVisualRenderer::new);

        // Register Tile Entity Renderers
        ClientRegistry.bindTileEntityRenderer((TileEntityType<? extends TileCruiseLauncher>) TileReg.CRUISE_LAUNCHER.get(), TERCruiseLauncher::new);
        ClientRegistry.bindTileEntityRenderer((TileEntityType<? extends TileEMPTower>) TileReg.EMP_TOWER.get(), TEREMPTower::new);
        ClientRegistry.bindTileEntityRenderer((TileEntityType<? extends TileOilRefinery>) TileReg.OIL_REFINERY.get(), TEROilRefinery::new);
        ClientRegistry.bindTileEntityRenderer((TileEntityType<? extends TileRadarStation>) TileReg.RADAR_STATION.get(), TERRadarStation::new);

        // Register Item Model Properties
        ItemModelsProperties.register(ItemReg.TRACKER.get(), new ResourceLocation("angle"),          ItemTracker::getAngleFromItemStack);
        ItemModelsProperties.register(ItemReg.TRACKER.get(), new ResourceLocation("icbm:hastarget"), ItemTracker::getHasTargetFromItemStack);

        // Register ItemColors for Dyeable Armor
        Minecraft.getInstance().getItemColors().register(
                (itemStack, tintIndex) -> {
                    return tintIndex > 0 ? -1 : ((IDyeableArmorItem)itemStack.getItem()).getColor(itemStack);
                },
                ItemReg.HAZMAT_MASK.get(),
                ItemReg.HAZMAT_JACKET.get(),
                ItemReg.HAZMAT_PANTS.get(),
                ItemReg.HAZMAT_BOOTS.get()
        );

        // Register BlockColors for Camouflage
        Minecraft.getInstance().getBlockColors().register((BlockState blockState, IBlockDisplayReader level, BlockPos blockPos, int tint) -> {
            if (level == null) return -1;
            return BlockCamouflage.getTileEntity(level, blockPos)
                    .map(TileCamouflage::getAppearanceNoSelf)
                    .map(appearance -> Minecraft.getInstance().getBlockColors().getColor(appearance, level, blockPos, tint))
                    .orElse(blockState.getMapColor(level, blockPos).col);
        }, BlockReg.CAMOUFLAGE.get());

    }

    public void onModelRegistryEvent(final ModelRegistryEvent event) {

        // BlockEntity Models
        ModelLoader.addSpecialModel(MODEL_CRUISE_LAUNCHER_DYNAMIC);
        ModelLoader.addSpecialModel(MODEL_CRUISE_LAUNCHER_STATIC);
        ModelLoader.addSpecialModel(MODEL_EMP_TOWER_CLOCKWISE);
        ModelLoader.addSpecialModel(MODEL_EMP_TOWER_COUNTERCLOCKWISE);
        ModelLoader.addSpecialModel(MODEL_EMP_TOWER_STATIC);
        ModelLoader.addSpecialModel(MODEL_RADAR_STATION_DYNAMIC);
        ModelLoader.addSpecialModel(MODEL_RADAR_STATION_STATIC);

        // Entity Models
        ModelLoader.addSpecialModel(MODEL_ENDER_BLAST_SPHERE);
        ModelLoader.addSpecialModel(MODEL_REDMATTER_BLAST_ACCRETION_DISK);
        ModelLoader.addSpecialModel(MODEL_REDMATTER_BLAST_SPHERE);

        // Other Models
        ModelLoader.addSpecialModel(MODEL_CAMOUFLAGE_DEFAULT);
        ModelLoaderRegistry.registerLoader(new ResourceLocation(ICBMReference.MODID, "camouflage_loader"), new DynamicBakedModelCamouflage.CamouflageModelLoader());

    }

    public void onParticleFactoryRegisterEvent(ParticleFactoryRegisterEvent event) {

        Minecraft.getInstance().particleEngine.register(ParticleTypeReg.ENDER_EFFECT.get(), (IAnimatedSprite sprites) -> EnderParticle::new);

        Minecraft.getInstance().particleEngine.register(ParticleTypeReg.SMOKE_DEBILITATION_A.get(), (IAnimatedSprite sprites) -> new ColoredSmokeParticle.Factory(sprites, 0xFF, 0xFF, 0xFF));
        Minecraft.getInstance().particleEngine.register(ParticleTypeReg.SMOKE_DEBILITATION_B.get(), (IAnimatedSprite sprites) -> new ColoredSmokeParticle.Factory(sprites, 0xBC, 0xBC, 0xBC));
        Minecraft.getInstance().particleEngine.register(ParticleTypeReg.SMOKE_DEBILITATION_C.get(), (IAnimatedSprite sprites) -> new ColoredSmokeParticle.Factory(sprites, 0x4B, 0x4B, 0x4B));
        Minecraft.getInstance().particleEngine.register(ParticleTypeReg.SMOKE_DEBILITATION_D.get(), (IAnimatedSprite sprites) -> new ColoredSmokeParticle.Factory(sprites, 0x2C, 0x2C, 0x2C));
        Minecraft.getInstance().particleEngine.register(ParticleTypeReg.SMOKE_DEBILITATION_E.get(), (IAnimatedSprite sprites) -> new ColoredSmokeParticle.Factory(sprites, 0x02, 0x02, 0x02));

        Minecraft.getInstance().particleEngine.register(ParticleTypeReg.SMOKE_CHEMICAL_A.get(), (IAnimatedSprite sprites) -> new ColoredSmokeParticle.Factory(sprites, 0xB2, 0xB2, 0x00));
        Minecraft.getInstance().particleEngine.register(ParticleTypeReg.SMOKE_CHEMICAL_B.get(), (IAnimatedSprite sprites) -> new ColoredSmokeParticle.Factory(sprites, 0x9D, 0x9D, 0x00));
        Minecraft.getInstance().particleEngine.register(ParticleTypeReg.SMOKE_CHEMICAL_C.get(), (IAnimatedSprite sprites) -> new ColoredSmokeParticle.Factory(sprites, 0x6A, 0x6A, 0x00));
        Minecraft.getInstance().particleEngine.register(ParticleTypeReg.SMOKE_CHEMICAL_D.get(), (IAnimatedSprite sprites) -> new ColoredSmokeParticle.Factory(sprites, 0x3C, 0x3C, 0x00));
        Minecraft.getInstance().particleEngine.register(ParticleTypeReg.SMOKE_CHEMICAL_E.get(), (IAnimatedSprite sprites) -> new ColoredSmokeParticle.Factory(sprites, 0x05, 0x05, 0x00));

        Minecraft.getInstance().particleEngine.register(ParticleTypeReg.SMOKE_CONTAGION_A.get(), (IAnimatedSprite sprites) -> new ColoredSmokeParticle.Factory(sprites, 0x43, 0xB3, 0x00));
        Minecraft.getInstance().particleEngine.register(ParticleTypeReg.SMOKE_CONTAGION_B.get(), (IAnimatedSprite sprites) -> new ColoredSmokeParticle.Factory(sprites, 0x38, 0x96, 0x00));
        Minecraft.getInstance().particleEngine.register(ParticleTypeReg.SMOKE_CONTAGION_C.get(), (IAnimatedSprite sprites) -> new ColoredSmokeParticle.Factory(sprites, 0x2D, 0x7B, 0x00));
        Minecraft.getInstance().particleEngine.register(ParticleTypeReg.SMOKE_CONTAGION_D.get(), (IAnimatedSprite sprites) -> new ColoredSmokeParticle.Factory(sprites, 0x20, 0x56, 0x00));
        Minecraft.getInstance().particleEngine.register(ParticleTypeReg.SMOKE_CONTAGION_E.get(), (IAnimatedSprite sprites) -> new ColoredSmokeParticle.Factory(sprites, 0x08, 0x16, 0x00));

        Minecraft.getInstance().particleEngine.register(ParticleTypeReg.RADIOACTIVE_EFFECT.get(), (IAnimatedSprite sprites) -> new ColoredSmokeParticle.FactoryRadioactive(sprites, 0xb2, 0xff, 0x4d));

    }

    public void setScreenLauncherControlPanel(TileLauncherControlPanel tileEntity) {
        Minecraft.getInstance().setScreen(new ScreenLauncherControlPanel(tileEntity));
    }

    public void updateScreenMachine(BlockPos posOfTileEntity) {
        if (Minecraft.getInstance().screen instanceof IScreenMachine) {
            ((IScreenMachine)Minecraft.getInstance().screen).updateGui(posOfTileEntity);
        }
    }

    public int getRenderDistance() {
        return Minecraft.getInstance().options.renderDistance;
    }

}
