package com.jdawg3636.icbm.common.listener;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.launcher_control_panel.TileLauncherControlPanel;
import com.jdawg3636.icbm.common.capability.ICBMCapabilities;
import com.jdawg3636.icbm.common.capability.blastcontroller.BlastControllerCapabilityProvider;
import com.jdawg3636.icbm.common.capability.blastcontroller.IBlastControllerCapability;
import com.jdawg3636.icbm.common.capability.missiledirector.IMissileDirectorCapability;
import com.jdawg3636.icbm.common.capability.missiledirector.MissileDirectorCapabilityProvider;
import com.jdawg3636.icbm.common.capability.trackingmanager.ITrackingManagerCapability;
import com.jdawg3636.icbm.common.capability.trackingmanager.TrackingManagerCapabilityProvider;
import com.jdawg3636.icbm.common.item.ItemDefuser;
import com.jdawg3636.icbm.common.network.ICBMNetworking;
import com.jdawg3636.icbm.common.reg.BlockReg;
import com.jdawg3636.icbm.common.reg.ICBMRecipeTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class CommonProxy {

    // Client Events
    public void onClientSetupEvent(final FMLClientSetupEvent event) {}
    public void onModelRegistryEvent(final ModelRegistryEvent event) {}

    // Client Misc
    public void setScreenLauncherControlPanel(TileLauncherControlPanel tileEntity) {}
    public void updateScreenMachine(BlockPos posOfTileEntity) {}

    // Common Events
    public void onCommonSetupEvent(final FMLCommonSetupEvent event) {

        ICBMNetworking.init();
        ICBMCapabilities.register();
        event.enqueueWork(ICBMRecipeTypes::registerAll);

        // Override blast resistance of vanilla obsidian
        if(ICBMReference.COMMON_CONFIG.getBlastResistanceObsidian() >= 0) {
            Blocks.OBSIDIAN.explosionResistance = ICBMReference.COMMON_CONFIG.getBlastResistanceObsidian();
        }

    }

    public void onAttachCapabilitiesEventWorld(final AttachCapabilitiesEvent<World> event) {
        if(event.getObject() instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) event.getObject();
            event.addCapability(new ResourceLocation(ICBMReference.MODID, "blastcontroller"), new BlastControllerCapabilityProvider());
            event.addCapability(new ResourceLocation(ICBMReference.MODID, "missiledirector"), new MissileDirectorCapabilityProvider(serverWorld));
            if(event.getObject().dimension().equals(World.OVERWORLD)) {
                event.addCapability(new ResourceLocation(ICBMReference.MODID, "trackingmanager"), new TrackingManagerCapabilityProvider());
            }
        }
    }

    public void onBiomeLoadingEvent(final BiomeLoadingEvent event) {
        if(ICBMReference.COMMON_CONFIG.getOreCopperCount() != 0)  registerOreGenFeature(event.getGeneration(), BlockReg.ORE_COPPER.get().defaultBlockState(),  ICBMReference.COMMON_CONFIG.getOreCopperSize(),  ICBMReference.COMMON_CONFIG.getOreCopperRange(),  ICBMReference.COMMON_CONFIG.getOreCopperCount());
        if(ICBMReference.COMMON_CONFIG.getOreSulfurCount() != 0)  registerOreGenFeature(event.getGeneration(), BlockReg.ORE_SULFUR.get().defaultBlockState(),  ICBMReference.COMMON_CONFIG.getOreSulfurSize(),  ICBMReference.COMMON_CONFIG.getOreSulfurRange(),  ICBMReference.COMMON_CONFIG.getOreSulfurCount());
        if(ICBMReference.COMMON_CONFIG.getOreTinCount() != 0)     registerOreGenFeature(event.getGeneration(), BlockReg.ORE_TIN.get().defaultBlockState(),     ICBMReference.COMMON_CONFIG.getOreTinSize(),     ICBMReference.COMMON_CONFIG.getOreTinRange(),     ICBMReference.COMMON_CONFIG.getOreTinCount());
        if(ICBMReference.COMMON_CONFIG.getOreUraniumCount() != 0) registerOreGenFeature(event.getGeneration(), BlockReg.ORE_URANIUM.get().defaultBlockState(), ICBMReference.COMMON_CONFIG.getOreUraniumSize(), ICBMReference.COMMON_CONFIG.getOreUraniumRange(), ICBMReference.COMMON_CONFIG.getOreUraniumCount());
    }

    public static void registerOreGenFeature(BiomeGenerationSettingsBuilder builder, BlockState oreBlockState, int size, int range, int count) {
        builder.getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES).add(
                () -> Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, oreBlockState, size)).range(range).squared().count(count)
        );
    }

    public void onPlayerInteractEvent(final PlayerInteractEvent.EntityInteractSpecific event) {
        if(event.getPlayer().getItemInHand(event.getHand()).getItem() instanceof ItemDefuser) {
            ItemDefuser.onInteractWithEntity(event);
        }
    }

    public void onLivingDeathEvent(final LivingDeathEvent event) {
        // Remove Tracking Tickets on Death
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if(server != null) {
            World levelOverworld = server.getLevel(World.OVERWORLD);
            if (levelOverworld != null) {
                LazyOptional<ITrackingManagerCapability> capOptional = levelOverworld.getCapability(ICBMCapabilities.TRACKING_MANAGER_CAPABILITY);
                capOptional.ifPresent((ITrackingManagerCapability cap) -> cap.clearTickets(event.getEntity().getUUID()));
            }
        }
    }

    public void onParticleFactoryRegisterEvent(ParticleFactoryRegisterEvent event) {}

    public void onTickEvent(final TickEvent.WorldTickEvent event) {
    	if(event.phase != TickEvent.Phase.START) return;
        LazyOptional<IBlastControllerCapability> blastControllerCapability = event.world.getCapability(ICBMCapabilities.BLAST_CONTROLLER_CAPABILITY);
        blastControllerCapability.ifPresent((IBlastControllerCapability cap) -> cap.onWorldTickEvent(event));
        LazyOptional<IMissileDirectorCapability> missileDirectorCapability = event.world.getCapability(ICBMCapabilities.MISSILE_DIRECTOR_CAPABILITY);
        missileDirectorCapability.ifPresent((IMissileDirectorCapability cap) -> cap.onWorldTickEvent(event));
    }

    public int getRenderDistance() {
        MinecraftServer minecraftServer = ServerLifecycleHooks.getCurrentServer();
        if(!(minecraftServer instanceof DedicatedServer)) return 3; // Can't think of any circumstance where this would apply, but worth a check.
        return (((DedicatedServer)minecraftServer).getProperties().viewDistance);
    }

}
