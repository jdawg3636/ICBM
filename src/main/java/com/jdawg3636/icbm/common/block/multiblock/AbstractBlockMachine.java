package com.jdawg3636.icbm.common.block.multiblock;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public abstract class AbstractBlockMachine extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public AbstractBlockMachine() {
        this(getMultiblockMachineBlockProperties());
    }

    public AbstractBlockMachine(AbstractBlock.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    // Properties Copied(ish) from registration for GLASS in net.minecraft.block.Blocks
    // Intended to be used for all multiblocks but separating out to leave flexibility. At the time of writing this is also used by the S-Mine (not a multiblock).
    public static AbstractBlock.Properties getMultiblockMachineBlockProperties() {
        return AbstractBlock.Properties.of(Material.GLASS, MaterialColor.METAL)
                .noOcclusion()
                .isValidSpawn((BlockState state, IBlockReader reader, BlockPos pos, EntityType<?> entity)->false)
                .isRedstoneConductor((BlockState state, IBlockReader reader, BlockPos pos)->false)
                .isSuffocating((BlockState state, IBlockReader reader, BlockPos pos)->false)
                .isViewBlocking((BlockState state, IBlockReader reader, BlockPos pos)->false);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    /**
     * Sets {@link this.FACING} to opposite of player
     */
    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    /**
     * Override to take into account the {@link this.FACING} property
     * Copied from net.minecraft.block.AnvilBlock
     */
    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    /**
     * Override to Block Interaction with Pistons
     */
    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    /**
     * Override to Block Levers/Buttons/etc. from being placed on this block
     */
    @Override
    public VoxelShape getBlockSupportShape(BlockState p_230335_1_, IBlockReader p_230335_2_, BlockPos p_230335_3_) {
        return VoxelShapes.empty();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public float getShadeBrightness(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return true;
    }

}
