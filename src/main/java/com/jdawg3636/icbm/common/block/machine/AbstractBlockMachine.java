package com.jdawg3636.icbm.common.block.machine;

import com.jdawg3636.icbm.common.block.IHasCustomWallPassThroughLogic;
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

/**
 * Superclass for all machine blocks.
 * Handles facing direction for placement, waterlogging, piston interaction, complex model lighting, and more.
 * Note that machines are not necessarily TileEntities!
 * @see AbstractBlockMachineTile
 */
public abstract class AbstractBlockMachine extends Block implements IWaterLoggable, IHasCustomWallPassThroughLogic {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public boolean waterloggable;

    public AbstractBlockMachine(boolean waterloggable) {
        this(getMultiblockMachineBlockProperties(), waterloggable);
    }

    public AbstractBlockMachine(AbstractBlock.Properties properties, boolean waterloggable) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
        this.waterloggable = waterloggable;
    }

    /**
     * These properties are copied(ish) from the registration for {@link net.minecraft.block.Blocks#GLASS GLASS} in {@link net.minecraft.block.Blocks}.
     * This collection is primarily intended to be used as the default for multiblocks, but it is separated out to allow for use in other contexts.
     * For example, at the time of writing, it is also used by the {@link com.jdawg3636.icbm.common.block.BlockSMine S-Mine} (which is not a multiblock).
     */
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

    /**
     * Declare our custom {@link net.minecraft.state.Property properties} ({@link AbstractBlockMachine#FACING} and {@link AbstractBlockMachine#WATERLOGGED})
     * so that the game will create unique {@link BlockState BlockStates} for each permutation of their possible values.
     * Note that, since every permutation takes up a numerical ID for storage on disk, this system is not suitable for complex properties
     * that have more than a handful of possible values (ex. energy storage). Complex data should instead be handled using a {@link TileEntity} (see {@link AbstractBlockMachineTile}).
     */
    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
        builder.add(WATERLOGGED);
    }

    /**
     * Sets {@link AbstractBlockMachine#FACING} to the opposite of player's orientation (facing back towards the player)
     * and sets {@link AbstractBlockMachine#WATERLOGGED} to true if this machine is {@link AbstractBlockMachine#waterloggable}
     * and the existing {@link FluidState} is {@link Fluids#WATER water}.
     */
    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState blockState = super.getStateForPlacement(context);
        return (blockState == null) ? null : blockState
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, waterloggable && context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }

    /**
     * Override to take into account the {@link AbstractBlockMachine#FACING} property.
     * Copied from {@link net.minecraft.block.AnvilBlock}.
     */
    @SuppressWarnings("deprecation")
    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    /**
     * Override to take into account the {@link AbstractBlockMachine#WATERLOGGED} property.
     */
    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    /**
     * Override to take into account the {@link AbstractBlockMachine#waterloggable} parameter.
     */
    @Override
    public boolean placeLiquid(IWorld level, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
        return waterloggable && IWaterLoggable.super.placeLiquid(level, blockPos, blockState, fluidState);
    }

    /**
     * Override to take into account the {@link AbstractBlockMachine#waterloggable} parameter.
     */
    @Override
    public boolean canPlaceLiquid(IBlockReader level, BlockPos blockPos, BlockState blockState, Fluid fluid) {
        return waterloggable && IWaterLoggable.super.canPlaceLiquid(level, blockPos, blockState, fluid);
    }

    /**
     * Schedule a tick if the block is {@link AbstractBlockMachine#WATERLOGGED} and a neighbor is updated.
     */
    @Override
    public BlockState updateShape(BlockState blockState, Direction facing, BlockState facingState, IWorld level, BlockPos currentPos, BlockPos facingPos) {
        if (blockState.getValue(WATERLOGGED)) {
            level.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(blockState, facing, facingState, level, currentPos, facingPos);
    }

    /**
     * Prevent interaction with pistons. This is a good idea to prevent bugs when potentially working with {@link TileEntity TileEntities},
     * and is consistent with the behavior of vanilla blocks (ex. {@link net.minecraft.block.ChestBlock chests} and {@link net.minecraft.block.FurnaceBlock furnaces}).
     * Note: vanilla blocks usually implement this behavior through the {@link Material} system, but will also override this method in special cases such as {@link net.minecraft.block.DoorBlock}.
     */
    @SuppressWarnings("deprecation")
    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    /**
     * Prevent levers/buttons/etc. from being placed on this block.
     */
    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getBlockSupportShape(BlockState blockState, IBlockReader level, BlockPos blockPos) {
        return VoxelShapes.empty();
    }

    /**
     * (Attempt to) disable ambient occlusion for rendering due to the game's poor handling of complex models that don't
     * conform to the limitations of vanilla .json models.<br><br>
     * See Also (when using Minecraft's vanilla render pipeline):<br>{@link net.minecraft.client.renderer.BlockModelRenderer.AmbientOcclusionFace#calculate}.<br>
     * See Also (when using Forge's experimental render pipeline, which is disabled by default in Forge's config file but is forcibly enabled for this mod's
     * blocks using {@link com.jdawg3636.icbm.mixin.MixinClientOBJLightingPipeline}):<br>{@link net.minecraftforge.client.model.pipeline.BlockInfo#updateLightMatrix}.
     */
    @SuppressWarnings("deprecation")
    @Override
    @OnlyIn(Dist.CLIENT)
    public float getShadeBrightness(BlockState blockState, IBlockReader level, BlockPos blockPos) {
        return 1.0F;
    }

    /**
     * Further attempt to mitigate ambient occlusion and lazily fix shadows being cast by models with solid undersides.
     * This could be improved, but almost certainly isn't worth the time.
     */
    @Override
    public boolean propagatesSkylightDown(BlockState blockState, IBlockReader level, BlockPos blockPos) {
        return true;
    }

    /**
     * Transfers the {@link ItemStack}'s custom name (as applied by an anvil) to the newly-created {@link TileMachine}.
     * Copied(ish) from {@link net.minecraft.block.ChestBlock#setPlacedBy}.
     */
    @Override
    public void setPlacedBy(World level, BlockPos blockPos, BlockState blockState, LivingEntity placer, ItemStack itemInHandOfPlacer) {
        super.setPlacedBy(level, blockPos, blockState, placer, itemInHandOfPlacer);
        if (itemInHandOfPlacer.hasCustomHoverName()) {
            TileEntity tileEntity = level.getBlockEntity(blockPos);
            if (tileEntity instanceof TileMachine) {
                ((TileMachine)tileEntity).setCustomName(itemInHandOfPlacer.getHoverName());
            }
        }
    }

    /**
     * Override to take into account the {@link AbstractBlockMachine#waterloggable} parameter.
     * @see com.jdawg3636.icbm.mixin.MixinCommonCustomizableFluidFlowDirections
     * @see IHasCustomWallPassThroughLogic
     */
    @Override
    public VoxelShape getShapeForFluidBlocking(BlockState blockState, IBlockReader level, BlockPos blockPos) {
        return this.waterloggable ? IHasCustomWallPassThroughLogic.super.getShapeForFluidBlocking(blockState, level, blockPos) : VoxelShapes.block();
    }

}
