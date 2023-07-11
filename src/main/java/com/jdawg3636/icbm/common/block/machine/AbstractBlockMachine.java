package com.jdawg3636.icbm.common.block.machine;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public abstract class AbstractBlockMachine extends Block implements IWaterLoggable {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public boolean waterloggable;

    public AbstractBlockMachine() {
        this(true);
    }

    public AbstractBlockMachine(boolean waterloggable) {
        this(getMultiblockMachineBlockProperties(), waterloggable);
    }

    public AbstractBlockMachine(AbstractBlock.Properties properties, boolean waterloggable) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
        this.waterloggable = waterloggable;
    }

    // Properties Copied(ish) from registration for GLASS in net.minecraft.block.Blocks
    // Intended to be used for all multiblocks but separating out to leave flexibility. At the time of writing this is also used by the S-Mine (not a multiblock).
    public static AbstractBlock.Properties getMultiblockMachineBlockProperties() {
        return AbstractBlock.Properties.of(Material.STONE, MaterialColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 6.0F)
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
        builder.add(WATERLOGGED);
    }

    /**
     * Sets {@link this.FACING} to opposite of player
     */
    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState blockState = super.getStateForPlacement(context);
        return (blockState == null) ? null : blockState.setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, waterloggable && context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }

    /**
     * Override to take into account the {@link this.FACING} property
     * Copied from net.minecraft.block.AnvilBlock
     */
    @SuppressWarnings("deprecation")
    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    /**
     * Override to take into account the {@link this.WATERLOGGED} property
     */
    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean placeLiquid(IWorld level, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
        return waterloggable && IWaterLoggable.super.placeLiquid(level, blockPos, blockState, fluidState);
    }

    @Override
    public boolean canPlaceLiquid(IBlockReader level, BlockPos blockPos, BlockState blockState, Fluid fluid) {
        return waterloggable && IWaterLoggable.super.canPlaceLiquid(level, blockPos, blockState, fluid);
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction facing, BlockState facingState, IWorld level, BlockPos currentPos, BlockPos facingPos) {
        if (blockState.getValue(WATERLOGGED)) {
            level.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(blockState, facing, facingState, level, currentPos, facingPos);
    }

    /**
     * Override to Block Interaction with Pistons
     */
    @SuppressWarnings("deprecation")
    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    /**
     * Override to Block Levers/Buttons/etc. from being placed on this block
     */
    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getBlockSupportShape(BlockState blockState, IBlockReader level, BlockPos blockPos) {
        return VoxelShapes.empty();
    }

    @SuppressWarnings("deprecation")
    @Override
    @OnlyIn(Dist.CLIENT)
    public float getShadeBrightness(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return true;
    }

    @Override
    public void setPlacedBy(World level, BlockPos blockPos, BlockState blockState, LivingEntity placer, ItemStack itemStack) {
        super.setPlacedBy(level, blockPos, blockState, placer, itemStack);
        if (itemStack.hasCustomHoverName()) {
            TileEntity tileentity = level.getBlockEntity(blockPos);
            if (tileentity instanceof TileMachine) {
                ((TileMachine)tileentity).setCustomName(itemStack.getHoverName());
            }
        }
    }

}
