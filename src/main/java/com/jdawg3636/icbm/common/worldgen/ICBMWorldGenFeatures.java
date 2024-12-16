package com.jdawg3636.icbm.common.worldgen;

import com.jdawg3636.icbm.common.reg.BlockReg;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;

public class ICBMWorldGenFeatures {

    public static ConfiguredFeature<?, ?> OIL_LAKE = Feature.LAKE.configured(new BlockStateFeatureConfig(BlockReg.OIL.get().defaultBlockState())).decorated(Placement.LAVA_LAKE.configured(new ChanceConfig(30)));
    public static ConfiguredFeature<BaseTreeFeatureConfig, ?> RUBBER_TREE = Feature.TREE.configured((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(BlockReg.RUBBER_LOG.get().defaultBlockState()), new SimpleBlockStateProvider(BlockReg.RUBBER_LEAVES.get().defaultBlockState()), new BlobFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(0), 3), new StraightTrunkPlacer(5, 2, 0), new TwoLayerFeature(1, 0, 1))).ignoreVines().build());

}
