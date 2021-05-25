package com.jdawg3636.icbm.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockReinforcedGlass extends Block {

    public BlockReinforcedGlass() {
        super(Block.Properties.create(Material.GLASS).hardnessAndResistance(10, 48)
                /* Copied(ish) from registration for GLASS in net.minecraft.block.Blocks */
                .notSolid()
                .setAllowsSpawn((BlockState state, IBlockReader reader, BlockPos pos, EntityType<?> entity)->false)
                .setOpaque((BlockState state, IBlockReader reader, BlockPos pos)->false)
                .setSuffocates((BlockState state, IBlockReader reader, BlockPos pos)->false)
                .setBlocksVision((BlockState state, IBlockReader reader, BlockPos pos)->false)
        );
    }

    /**
     * Copied from {@link net.minecraft.block.AbstractGlassBlock}
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public float getAmbientOcclusionLightValue(BlockState p_220080_1_, IBlockReader p_220080_2_, BlockPos p_220080_3_) {
        return 1.0F;
    }

    /**
     * Prevents the rendering of internal faces when multiple blocks of the same type are placed next to each other
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
        return state.getBlock() == adjacentBlockState.getBlock() || super.isSideInvisible(state, adjacentBlockState, side);
    }

    /**
     * Copied from {@link net.minecraft.block.AbstractGlassBlock}
     */
    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, IBlockReader p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }

    // TODO - drop nothing on normal break
    /* TODO - silk touch support - might have to be done through loot tables now? https://github.com/MinecraftForge/MinecraftForge/blob/1.15.x/src/test/java/net/minecraftforge/debug/gameplay/loot/GlobalLootModifiersTest.java
    @Override
    protected boolean canSilkHarvest() {
        return true;
    }
    */

}
