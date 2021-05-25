package com.jdawg3636.icbm.common.block.multiblock;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

/**
 * Superclass for Multiblocks
 * Multiblock Mechanics are handled through BlockState Properties, not TileEntities
 */
public abstract class AbstractBlockMulti extends Block {

    public static final int absHorizMax = 1;
    public static final int absHeightMax = 2;
    public static final int absDepthMax = 1;

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty MULTIBLOCK_OFFSET_HORIZONTAL            = IntegerProperty.create("multiblock_offset_horizontal", 0, absHorizMax);
    public static final BooleanProperty MULTIBLOCK_OFFSET_HORIZONTAL_NEGATIVE   = BooleanProperty.create("multiblock_offset_horizontal_negative");
    public static final IntegerProperty MULTIBLOCK_OFFSET_HEIGHT                = IntegerProperty.create("multiblock_offset_height", 0, absHeightMax);
    public static final BooleanProperty MULTIBLOCK_OFFSET_HEIGHT_NEGATIVE       = BooleanProperty.create("multiblock_offset_height_negative");
    public static final IntegerProperty MULTIBLOCK_OFFSET_DEPTH                 = IntegerProperty.create("multiblock_offset_depth", 0, absDepthMax);
    public static final BooleanProperty MULTIBLOCK_OFFSET_DEPTH_NEGATIVE        = BooleanProperty.create("multiblock_offset_depth_negative");

    public AbstractBlockMulti() {
        this(getMultiblockMachineBlockProperties());
    }

    public AbstractBlockMulti(AbstractBlock.Properties properties) {
        super(properties);
        this.setDefaultState(
                this.stateContainer.getBaseState()
                .with(FACING, Direction.NORTH)
                .with(MULTIBLOCK_OFFSET_HORIZONTAL, 0)
                .with(MULTIBLOCK_OFFSET_HORIZONTAL_NEGATIVE, false)
                .with(MULTIBLOCK_OFFSET_HEIGHT, 0)
                .with(MULTIBLOCK_OFFSET_HEIGHT_NEGATIVE, false)
                .with(MULTIBLOCK_OFFSET_DEPTH, 0)
                .with(MULTIBLOCK_OFFSET_DEPTH_NEGATIVE, false)
        );
    }

    // Properties Copied(ish) from registration for GLASS in net.minecraft.block.Blocks
    // Intended to be used for all multiblocks but separating out to leave flexibility. At the time of writing this is also used by the S-Mine (not a multiblock).
    public static AbstractBlock.Properties getMultiblockMachineBlockProperties() {
        return AbstractBlock.Properties.create(Material.GLASS, MaterialColor.IRON)
                .notSolid()
                .setAllowsSpawn((BlockState state, IBlockReader reader, BlockPos pos, EntityType<?> entity)->false)
                .setOpaque((BlockState state, IBlockReader reader, BlockPos pos)->false)
                .setSuffocates((BlockState state, IBlockReader reader, BlockPos pos)->false)
                .setBlocksVision((BlockState state, IBlockReader reader, BlockPos pos)->false);
    }

    /**
     * @return Height of the full multiblock structure starting from the root (root is included, blocks below the root are not)
     */
    public int getMultiblockHeight() {
        return absHeightMax + 1;
    }

    /**
     * Add Properties to BlockState
     */
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(MULTIBLOCK_OFFSET_HORIZONTAL);
        builder.add(MULTIBLOCK_OFFSET_HORIZONTAL_NEGATIVE);
        builder.add(MULTIBLOCK_OFFSET_HEIGHT);
        builder.add(MULTIBLOCK_OFFSET_HEIGHT_NEGATIVE);
        builder.add(MULTIBLOCK_OFFSET_DEPTH);
        builder.add(MULTIBLOCK_OFFSET_DEPTH_NEGATIVE);
    }

    /**
     * Override to Block Interaction with Pistons
     */
    @Override
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    /**
     * Sets {@link this.FACING} to opposite of player
     * Also using to prevent placement in invalid locations (ex. near world height), return null BlockState if invalid
     */
    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        if(context.getPos().getY() > context.getWorld().getHeight()-getMultiblockHeight()) return null;
        if(!context.getWorld().getBlockState(context.getPos().up()).isReplaceable(context)) return null;
        return super.getStateForPlacement(context).with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    /**
     * Override to take into account the {@link this.FACING} property
     *
     * Copied from net.minecraft.block.AnvilBlock
     */
    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    /**
     * Multiblock Placement Routine
     *
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        fillMultiblock(worldIn, pos, state, false);
    }

    /**
     * Initiates Multiblock Destruction Propagation
     *
     * Called before the Block is set to air in the world. Called regardless of if the player's tool can actually collect
     * this block
     */
    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBlockHarvested(worldIn, pos, state, player);
        destroyMultiblock(worldIn, pos, state);
    }

    public void fillMultiblock(World worldIn, BlockPos pos, BlockState state, boolean setToAir) {

        // Add Center Block to List
        Vector3i[] multiblockPositions = new Vector3i[getMultiblockPositions().length+1];
        for(int i = 0; i < getMultiblockPositions().length; i++) multiblockPositions[i] = getMultiblockPositions()[i];
        multiblockPositions[multiblockPositions.length-1] = new Vector3i(0, 0, 0);

        for(Vector3i multiblockPos : multiblockPositions) {

            // Calculate Rotation based on "facing" property
            // NOTE: Using opposite of "facing", so all offsets are from pov of the player
            Vector3i multiblockPosRotated;

            // North (-z) (Default)
            multiblockPosRotated = multiblockPos;
            // East (+x)
            if(state.get(FACING).getOpposite().getDirectionVec().getX() == 1)
                multiblockPosRotated = new Vector3i(+multiblockPos.getZ(), multiblockPos.getY(), +multiblockPos.getX());
                // South (+z)
            else if(state.get(FACING).getOpposite().getDirectionVec().getZ() == 1)
                multiblockPosRotated = new Vector3i(-multiblockPos.getX(), multiblockPos.getY(), -multiblockPos.getZ());
                // West (-x)
            else if(state.get(FACING).getOpposite().getDirectionVec().getX() == -1)
                multiblockPosRotated = new Vector3i(+multiblockPos.getZ(), multiblockPos.getY(), -multiblockPos.getX());

            // Use rotated offset + base coords for world placement
            BlockPos worldPos = pos.add(multiblockPosRotated.getX(), multiblockPosRotated.getY(), multiblockPosRotated.getZ());

            if(worldIn.getBlockState(new BlockPos(worldPos)).getBlock().getDefaultState().equals(getDefaultState()) || worldIn.getBlockState(new BlockPos(worldPos)).getMaterial().isReplaceable()) {

                if(setToAir){
                    worldIn.setBlockState(worldPos, Blocks.AIR.getDefaultState());
                }
                else {
                    worldIn.setBlockState(
                            worldPos,
                            // Encode unrotated offset into BlockState
                            this.getDefaultState()
                                    .with(FACING, state.get(FACING))
                                    .with(MULTIBLOCK_OFFSET_HORIZONTAL, Math.abs(multiblockPos.getX()))
                                    .with(MULTIBLOCK_OFFSET_HORIZONTAL_NEGATIVE, multiblockPos.getX() < 0)
                                    .with(MULTIBLOCK_OFFSET_HEIGHT, Math.abs(multiblockPos.getY()))
                                    .with(MULTIBLOCK_OFFSET_HEIGHT_NEGATIVE, multiblockPos.getY() < 0)
                                    .with(MULTIBLOCK_OFFSET_DEPTH, Math.abs(multiblockPos.getZ()))
                                    .with(MULTIBLOCK_OFFSET_DEPTH_NEGATIVE, multiblockPos.getZ() < 0)
                            , 3
                    );
                }

            }

        }

    }

    /**
     * Multiblock Destruction Routine
     */
    public void destroyMultiblock(World worldIn, BlockPos pos, BlockState sourceState) {
        BlockPos posOfCenter = getMultiblockCenter(worldIn, pos, sourceState);
        // Fill with Air
        fillMultiblock(worldIn, posOfCenter, worldIn.getBlockState(posOfCenter), true);
    }

    public boolean isRootOfMultiblock(BlockState state) {
        return
            state.get(MULTIBLOCK_OFFSET_HORIZONTAL) == 0 &&
            state.get(MULTIBLOCK_OFFSET_HEIGHT) == 0 &&
            state.get(MULTIBLOCK_OFFSET_DEPTH) == 0;
    }

    public BlockPos getMultiblockCenter(World worldIn, BlockPos pos, BlockState sourceState) {
        // Raw Data from BlockState
        int offsetX = sourceState.get(MULTIBLOCK_OFFSET_HORIZONTAL); if(sourceState.get(MULTIBLOCK_OFFSET_HORIZONTAL_NEGATIVE)) offsetX *= -1;
        int offsetY = sourceState.get(MULTIBLOCK_OFFSET_HEIGHT); if(sourceState.get(MULTIBLOCK_OFFSET_HEIGHT_NEGATIVE)) offsetY *= -1;
        int offsetZ = sourceState.get(MULTIBLOCK_OFFSET_DEPTH); if(sourceState.get(MULTIBLOCK_OFFSET_DEPTH_NEGATIVE)) offsetZ *= -1;
        // North (-z)
        if(sourceState.get(FACING).getDirectionVec().getZ() == -1) {
            offsetX *= -1;
        }
        // East (+x)
        if(sourceState.get(FACING).getDirectionVec().getX() == 1) {
            int temp = offsetX;
            offsetX = -offsetZ;
            offsetZ = -temp;
        }
        // South (+z)
        else if(sourceState.get(FACING).getDirectionVec().getZ() == 1) {
            offsetZ *= -1;
        }
        // West (-x)
        else if(sourceState.get(FACING).getDirectionVec().getX() == -1) {
            int temp = offsetX;
            offsetX = offsetZ;
            offsetZ = temp;
        }

        // Invert Offsets and Return
        return pos.add(-offsetX, -offsetY, -offsetZ);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return true;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return isRootOfMultiblock(state) ? BlockRenderType.MODEL : BlockRenderType.INVISIBLE;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
        return false;
    }


    /**
     * @return Array of Vector3i, each vector represents the horiz/height/depth offsets for all non-root multiblock components.
     * DO NOT INCLUDE THE ROOT (0, 0, 0)
     */
    public abstract Vector3i[] getMultiblockPositions();

}
