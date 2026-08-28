package com.jdawg3636.icbm.common.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import java.util.Optional;
import java.util.function.Supplier;

public class BlockModdedLog extends RotatedPillarBlock {

    public Optional<Supplier<Block>> strippedLogSupplier;

    public BlockModdedLog(Optional<Supplier<Block>> strippedLogSupplier) {
        this(
            AbstractBlock.Properties.of(
                Material.WOOD,
                blockState -> blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MaterialColor.WOOD : MaterialColor.PODZOL
            ).strength(2.0F).sound(SoundType.WOOD),
            strippedLogSupplier
        );
    }

    public BlockModdedLog(Properties properties, Optional<Supplier<Block>> strippedLogSupplier) {
        super(properties);
        this.strippedLogSupplier = strippedLogSupplier;
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

    @Override
    public BlockState getToolModifiedState(BlockState state, World level, BlockPos pos, PlayerEntity player, ItemStack stack, ToolType toolType) {
        if (toolType == ToolType.AXE) {
            return this.strippedLogSupplier.map(a -> a.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS))).orElse(null);
        }
        return super.getToolModifiedState(state, level, pos, player, stack, toolType);
    }

}
