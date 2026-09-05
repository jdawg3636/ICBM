package com.jdawg3636.icbm.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockModdedGlassSlab extends SlabBlock {

    public BlockModdedGlassSlab(Properties properties) {
        super(properties);
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
        if(state.getBlock() != adjacentBlockState.getBlock()) {
            return super.skipRendering(state, adjacentBlockState, side);
        }
        if(!side.getAxis().equals(Direction.Axis.Y) && state.getValue(SlabBlock.TYPE).equals(adjacentBlockState.getValue(SlabBlock.TYPE))) {
            return true;
        }
        if(side.equals(Direction.UP) && (state.getValue(SlabBlock.TYPE).equals(SlabType.DOUBLE) || state.getValue(SlabBlock.TYPE).equals(SlabType.TOP)) && (adjacentBlockState.getValue(SlabBlock.TYPE).equals(SlabType.DOUBLE) || adjacentBlockState.getValue(SlabBlock.TYPE).equals(SlabType.BOTTOM))) {
            return true;
        }
        if(side.equals(Direction.DOWN) && (state.getValue(SlabBlock.TYPE).equals(SlabType.DOUBLE) || state.getValue(SlabBlock.TYPE).equals(SlabType.BOTTOM)) && (adjacentBlockState.getValue(SlabBlock.TYPE).equals(SlabType.DOUBLE) || adjacentBlockState.getValue(SlabBlock.TYPE).equals(SlabType.TOP))) {
            return true;
        }
        return false;
    }

    /**
     * Copied from {@link net.minecraft.block.AbstractGlassBlock}
     */
    @Override
    public boolean propagatesSkylightDown(BlockState blockState, IBlockReader level, BlockPos blockPos) {
        return true;
    }

}
