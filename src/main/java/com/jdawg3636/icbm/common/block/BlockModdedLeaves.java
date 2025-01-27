package com.jdawg3636.icbm.common.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BlockModdedLeaves extends LeavesBlock {

    public BlockModdedLeaves() {
        this(
            AbstractBlock.Properties.of(Material.LEAVES)
            .strength(0.2F)
            .randomTicks()
            .sound(SoundType.GRASS)
            .noOcclusion()
            .isValidSpawn((blockState, level, blockPos, entityType) -> false)
            .isSuffocating((blockState, level, blockPos) -> false)
            .isViewBlocking((blockState, level, blockPos) -> false)
        );
    }

    public BlockModdedLeaves(Properties properties) {
        super(properties);
    }

    /**
     * Replicating the hardcoded behavior of vanilla leaves as defined by {@link net.minecraft.block.FireBlock#bootStrap}
     */
    @Override
    public int getFireSpreadSpeed(BlockState state, IBlockReader level, BlockPos pos, Direction face) {
        return 30;
    }

    /**
     * Replicating the hardcoded behavior of vanilla leaves as defined by {@link net.minecraft.block.FireBlock#bootStrap}
     */
    @Override
    public int getFlammability(BlockState state, IBlockReader level, BlockPos pos, Direction face) {
        return 60;
    }

}
