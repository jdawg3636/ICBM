package com.jdawg3636.icbm.common.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BlockModdedLog extends RotatedPillarBlock {

    public BlockModdedLog() {
        this(
            AbstractBlock.Properties.of(
                Material.WOOD,
                blockState -> blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MaterialColor.WOOD : MaterialColor.PODZOL
            )
            .strength(2.0F)
            .sound(SoundType.WOOD)
        );
    }

    public BlockModdedLog(Properties properties) {
        super(properties);
    }

    /**
     * Replicating the hardcoded behavior of vanilla logs as defined by {@link net.minecraft.block.FireBlock#bootStrap}
     */
    @Override
    public int getFireSpreadSpeed(BlockState state, IBlockReader level, BlockPos pos, Direction face) {
        return 5;
    }

    /**
     * Replicating the hardcoded behavior of vanilla logs as defined by {@link net.minecraft.block.FireBlock#bootStrap}
     */
    @Override
    public int getFlammability(BlockState state, IBlockReader level, BlockPos pos, Direction face) {
        return 5;
    }

}
