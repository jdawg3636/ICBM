package com.jdawg3636.icbm.common.listener;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.blast.BlastController;
import com.jdawg3636.icbm.common.capability.*;
import com.jdawg3636.icbm.common.block.launcher_control_panel.TileLauncherControlPanel;
import com.jdawg3636.icbm.common.capability.blastcontroller.BlastControllerCapabilityProvider;
import com.jdawg3636.icbm.common.network.ICBMNetworking;
import com.jdawg3636.icbm.common.reg.BlockReg;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
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
        ICBMCapabilities.register();
    }

    public void onAttachCapabilitiesEventWorld(final AttachCapabilitiesEvent<World> event) {
        event.addCapability(new ResourceLocation(ICBMReference.MODID, "blastcontroller"), new BlastControllerCapabilityProvider());
    }

    public void onBiomeLoadingEvent(final BiomeLoadingEvent event) {
        // Copper Ore
        event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES ).add(
                () -> Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, BlockReg.ORE_COPPER.get().defaultBlockState(), 9)).range(64).squared().count(20)
        );
        // Sulfur Ore
        event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES ).add(
                () -> Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, BlockReg.ORE_SULFUR.get().defaultBlockState(), 16)).range(11).squared().count(20)
        );
        // Tin Ore
        event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES ).add(
                () -> Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, BlockReg.ORE_TIN.get().defaultBlockState(), 6)).range(48).squared().count(20)
        );
        // Uranium Ore
        event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES ).add(
                () -> Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, BlockReg.ORE_URANIUM.get().defaultBlockState(), 2)).range(16).squared().count(10)
        );
    }

    public void onParticleFactoryRegisterEvent(ParticleFactoryRegisterEvent event) {}

    public void onTickEvent(final TickEvent.WorldTickEvent event) {
        BlastController.onTickEvent(event);
    }

}
