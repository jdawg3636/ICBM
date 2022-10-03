package com.jdawg3636.icbm.common.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

public class BlockModdedGlass extends Block {

    public static AbstractBlock.Properties getModGlassBlockProperties() {
        return Block.Properties
            /* Copied(ish) from registration for GLASS in net.minecraft.block.Blocks */
            .of(Material.GLASS)
            .harvestTool(ToolType.PICKAXE)
            .noOcclusion()
            .isValidSpawn((BlockState state, IBlockReader reader, BlockPos pos, EntityType<?> entity)->false)
            .isRedstoneConductor((BlockState state, IBlockReader reader, BlockPos pos)->false)
            .isSuffocating((BlockState state, IBlockReader reader, BlockPos pos)->false)
            .isViewBlocking((BlockState state, IBlockReader reader, BlockPos pos)->false);
    }

    public BlockModdedGlass(AbstractBlock.Properties blockProperties) {
        super(blockProperties);
    }

    public BlockModdedGlass(float destroyTime, float explosionResistance) {
        this(getModGlassBlockProperties().strength(destroyTime, explosionResistance));
    }

    /**
     * Copied from {@link net.minecraft.block.AbstractGlassBlock}
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public float getShadeBrightness(BlockState state, IBlockReader level, BlockPos blockPos) {
        return 1.0F;
    }

    /**
     * Prevents the rendering of internal faces when multiple blocks of the same type are placed next to each other
     */
    @SuppressWarnings("deprecation")
    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        return state.getBlock() == adjacentBlockState.getBlock() || super.skipRendering(state, adjacentBlockState, side);
    }

    /**
     * Copied from {@link net.minecraft.block.AbstractGlassBlock}
     */
    @Override
    public boolean propagatesSkylightDown(BlockState blockState, IBlockReader level, BlockPos blockPos) {
        return true;
    }

}
