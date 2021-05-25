package com.jdawg3636.icbm.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockSpikes extends Block {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public BlockSpikes() {
        super(Block.Properties.create((new Material.Builder(MaterialColor.IRON)).notSolid().build()).hardnessAndResistance(1.0F).doesNotBlockMovement().notSolid());
        this.setDefaultState(
                this.stateContainer.getBaseState()
                .with(WATERLOGGED, true)
        );
    }

    /**
     * Add Properties to BlockState
     */
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Nullable
    // Based on net.minecraft.block.LadderBlock
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        if (!context.replacingClickedOnBlock()) {
            BlockState blockstate = context.getWorld().getBlockState(context.getPos().offset(context.getFace().getOpposite()));
            if (blockstate.isIn(this)) {
                return null;
            }
        }

        BlockState blockstate = this.getDefaultState();
        IWorldReader iworldreader = context.getWorld();
        BlockPos blockpos = context.getPos();
        FluidState fluidstate = context.getWorld().getFluidState(context.getPos());

        if (blockstate.isValidPosition(iworldreader, blockpos)) {
            return blockstate.with(WATERLOGGED, fluidstate.getFluid() == Fluids.WATER);
        }

        return null;
    }

    // Copied from net.minecraft.block.CakeBlock
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.down()).getMaterial().isSolid();
    }

    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity) entity.attackEntityFrom(DamageSource.CACTUS, 1);
    }

}
