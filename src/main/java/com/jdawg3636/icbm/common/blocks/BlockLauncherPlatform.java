package com.jdawg3636.icbm.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockLauncherPlatform extends Block {

    /**
     * Properties for Positioning Within Multiblock
     */
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final IntegerProperty MULTIBLOCK_OFFSET_HORIZONTAL            = IntegerProperty.create("multiblock_offset_horizontal", 0, 3);
    public static final BooleanProperty MULTIBLOCK_OFFSET_HORIZONTAL_NEGATIVE   = BooleanProperty.create("multiblock_offset_horizontal_negative");
    public static final IntegerProperty MULTIBLOCK_OFFSET_HEIGHT                = IntegerProperty.create("multiblock_offset_height", 0, 3);
    public static final BooleanProperty MULTIBLOCK_OFFSET_HEIGHT_NEGATIVE       = BooleanProperty.create("multiblock_offset_height_negative");
    public static final IntegerProperty MULTIBLOCK_OFFSET_DEPTH                 = IntegerProperty.create("multiblock_offset_depth", 0, 3);
    public static final BooleanProperty MULTIBLOCK_OFFSET_DEPTH_NEGATIVE        = BooleanProperty.create("multiblock_offset_depth_negative");

    /**
     * Constructor - Sets Default State for Multiblock Positioning Properties
     */
    public BlockLauncherPlatform() {
        super(Block.Properties.create(Material.IRON));
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
     * Sets {@link this.FACING} to opposite of player
     * Also using to prevent placement in invalid locations (ex. near world height), return null BlockState if invalid
     */
    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        int multiblockHeight = 3;
        BlockPos blockpos = context.getPos();
        return blockpos.getY() <= context.getWorld().getHeight()-multiblockHeight && context.getWorld().getBlockState(blockpos.up()).isReplaceable(context) ? super.getStateForPlacement(context).with(FACING, context.getPlacementHorizontalFacing().getOpposite()) : null;
    }

    /**
     * Multiblock Placement Routine
     *
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {

        Vector3i[] multiblockPositions = {new Vector3i(1,0,0), new Vector3i(1,1,0), new Vector3i(1,2,0), new Vector3i(-1,0,0), new Vector3i(-1,1,0), new Vector3i(-1,2,0)};

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

            worldIn.setBlockState(
                    // Use rotated offset + base coords for world placement
                    pos.add(multiblockPosRotated.getX(), multiblockPosRotated.getY(), multiblockPosRotated.getZ()),
                    // Encode unrotated offset into BlockState
                    this.getDefaultState()
                            .with(FACING, state.get(FACING))
                            .with(MULTIBLOCK_OFFSET_HORIZONTAL, Math.abs(multiblockPos.getX()))
                            .with(MULTIBLOCK_OFFSET_HORIZONTAL_NEGATIVE, multiblockPos.getX() < 0)
                            .with(MULTIBLOCK_OFFSET_HEIGHT, Math.abs(multiblockPos.getY()))
                            .with(MULTIBLOCK_OFFSET_HEIGHT_NEGATIVE, multiblockPos.getY() < 0)
                            .with(MULTIBLOCK_OFFSET_DEPTH, Math.abs(multiblockPos.getZ()))
                            .with(MULTIBLOCK_OFFSET_DEPTH_NEGATIVE, multiblockPos.getZ() < 0)
                    , 3);
        }

    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        //TODO temp bypass, need to implement
        if (true /*TODO|| state.get(HALF) != DoubleBlockHalf.UPPER*/) {
            return super.isValidPosition(state, worldIn, pos);
        } else {
            BlockState blockstate = worldIn.getBlockState(pos.down());
            if (state.getBlock() != this) return super.isValidPosition(state, worldIn, pos); //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
            return blockstate.isIn(this) /*TODO&& blockstate.get(HALF) == DoubleBlockHalf.LOWER*/;
        }
    }

    /**
     * Initiates Multiblock Destruction Propagation
     *
     * Called before the Block is set to air in the world. Called regardless of if the player's tool can actually collect
     * this block
     */
    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!worldIn.isRemote) {
            if (player.isCreative()) {
                removeBottomHalf(worldIn, pos, state, player);
            } else {
                spawnDrops(state, worldIn, pos, (TileEntity)null, player, player.getHeldItemMainhand());
            }
        }

        super.onBlockHarvested(worldIn, pos, state, player);
    }

    /**
     * Multiblock Destruction Impl Util
     *
     * Only called if broken in Creative Mode, otherwise uses spawnDrops method
     */
    protected static void removeBottomHalf(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        /*TODO
        DoubleBlockHalf doubleblockhalf = state.get(HALF);
        if (doubleblockhalf == DoubleBlockHalf.UPPER) {
            BlockPos blockpos = pos.down();
            BlockState blockstate = world.getBlockState(blockpos);
            if (blockstate.getBlock() == state.getBlock() && blockstate.get(HALF) == DoubleBlockHalf.LOWER) {
                world.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 35);
                world.playEvent(player, 2001, blockpos, Block.getStateId(blockstate));
            }
        }
         */
    }

    /**
     * Currently using this to propagate multiblock destruction. This might be a bad idea.
     *
     * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
     * returns its solidified counterpart.
     * Note that this method should ideally consider only the specific face passed in.
     */
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        //todo temp bypass
        /*if(1==1)*/ return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        /*DoubleBlockHalf doubleblockhalf = stateIn.get(HALF);
        if (facing.getAxis() != Direction.Axis.Y || doubleblockhalf == DoubleBlockHalf.LOWER != (facing == Direction.UP) || facingState.isIn(this) && facingState.get(HALF) != doubleblockhalf) {
            return doubleblockhalf == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        } else {
            return Blocks.AIR.getDefaultState();
        }*/
    }

    /**
     * TODO Implement Custom Drop Routine
     *
     * Spawns the block's drops in the world. By the time this is called the Block has possibly been set to air via
     * Block.removedByPlayer
     */
    @Override
    public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, Blocks.AIR.getDefaultState(), te, stack);
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
     * Override to Block Interaction with Pistons
     */
    @Override
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

}
