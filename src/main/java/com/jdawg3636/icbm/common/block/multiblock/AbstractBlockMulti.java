package com.jdawg3636.icbm.common.block.multiblock;

import com.jdawg3636.icbm.common.block.machine.AbstractBlockMachine;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

/**
 * Superclass for Multiblocks
 * Multiblock Mechanics are handled through BlockState Properties, not TileEntities
 */
public abstract class AbstractBlockMulti extends AbstractBlockMachine {

    public static final int absHorizMax = 1;
    public static final int absHeightMax = 2;
    public static final int absDepthMax = 1;

    public static final IntegerProperty MULTIBLOCK_OFFSET_HORIZONTAL            = IntegerProperty.create("multiblock_offset_horizontal", 0, absHorizMax);
    public static final BooleanProperty MULTIBLOCK_OFFSET_HORIZONTAL_NEGATIVE   = BooleanProperty.create("multiblock_offset_horizontal_negative");
    public static final IntegerProperty MULTIBLOCK_OFFSET_HEIGHT                = IntegerProperty.create("multiblock_offset_height", 0, absHeightMax);
    public static final BooleanProperty MULTIBLOCK_OFFSET_HEIGHT_NEGATIVE       = BooleanProperty.create("multiblock_offset_height_negative");
    public static final IntegerProperty MULTIBLOCK_OFFSET_DEPTH                 = IntegerProperty.create("multiblock_offset_depth", 0, absDepthMax);
    public static final BooleanProperty MULTIBLOCK_OFFSET_DEPTH_NEGATIVE        = BooleanProperty.create("multiblock_offset_depth_negative");

    public AbstractBlockMulti() {
        this(true);
    }

    public AbstractBlockMulti(boolean waterloggable) {
        this(getMultiblockMachineBlockProperties(), waterloggable);
    }

    public AbstractBlockMulti(AbstractBlock.Properties properties, boolean waterloggable) {
        super(properties, waterloggable);
        this.registerDefaultState(
                this.defaultBlockState() // We know this isn't null, set in super.
                .setValue(MULTIBLOCK_OFFSET_HORIZONTAL, 0)
                .setValue(MULTIBLOCK_OFFSET_HORIZONTAL_NEGATIVE, false)
                .setValue(MULTIBLOCK_OFFSET_HEIGHT, 0)
                .setValue(MULTIBLOCK_OFFSET_HEIGHT_NEGATIVE, false)
                .setValue(MULTIBLOCK_OFFSET_DEPTH, 0)
                .setValue(MULTIBLOCK_OFFSET_DEPTH_NEGATIVE, false)
        );
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
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MULTIBLOCK_OFFSET_HORIZONTAL);
        builder.add(MULTIBLOCK_OFFSET_HORIZONTAL_NEGATIVE);
        builder.add(MULTIBLOCK_OFFSET_HEIGHT);
        builder.add(MULTIBLOCK_OFFSET_HEIGHT_NEGATIVE);
        builder.add(MULTIBLOCK_OFFSET_DEPTH);
        builder.add(MULTIBLOCK_OFFSET_DEPTH_NEGATIVE);
    }

    public BlockState getStateWithOffset(Vector3i offset, Direction facing, boolean waterlogged) {
        return this.defaultBlockState()
            .setValue(FACING, facing)
            .setValue(WATERLOGGED, waterlogged)
            .setValue(MULTIBLOCK_OFFSET_HORIZONTAL, Math.abs(offset.getX()))
            .setValue(MULTIBLOCK_OFFSET_HORIZONTAL_NEGATIVE, offset.getX() < 0)
            .setValue(MULTIBLOCK_OFFSET_HEIGHT, Math.abs(offset.getY()))
            .setValue(MULTIBLOCK_OFFSET_HEIGHT_NEGATIVE, offset.getY() < 0)
            .setValue(MULTIBLOCK_OFFSET_DEPTH, Math.abs(offset.getZ()))
            .setValue(MULTIBLOCK_OFFSET_DEPTH_NEGATIVE, offset.getZ() < 0);
    }

    /**
     * Prevents placement in invalid locations (ex. near world height), return null BlockState if invalid
     */
    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = super.getStateForPlacement(context);
        // Prevent placing too high
        if (context.getClickedPos().getY() > context.getLevel().getMaxBuildHeight() - getMultiblockHeight()) {
            return null;
        }
        // Prevent if something is in the way
        for (BlockPos toCheck : getMultiblockWorldPositions(context.getClickedPos(), state)) {
            if(!context.getLevel().getBlockState(toCheck).canBeReplaced(context)) {
                return null;
            }
        }
        // All clear send it
        return state;
    }

    /**
     * Initiates Multiblock Placement Routine
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    @Override
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemInHandOfPlacer) {
        super.setPlacedBy(worldIn, pos, state, placer, itemInHandOfPlacer);
        placeMultiblock(worldIn, pos, state);
    }

    /**
     * Initiates Multiblock Destruction Routine
     * Called before the Block is set to air in the world. Called regardless of if the player's tool can actually collect
     * this block
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState originalState, World level, BlockPos blockPos, BlockState newState, boolean flag) {
        if (!originalState.is(newState.getBlock())) {
            destroyMultiblock(level, blockPos, originalState);
            super.onRemove(originalState, level, blockPos, newState, flag);
        }
    }

    @Override
    public boolean canDropFromExplosion(BlockState state, IBlockReader level, BlockPos pos, Explosion explosion) {
        return isRootOfMultiblock(state) && super.canDropFromExplosion(state, level, pos, explosion);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean dropFromExplosion(Explosion explosion) {
        return false;
    }

    public void placeMultiblock(World world, BlockPos rootPos, BlockState rootState) {

        // Checks that the root position actually contains a root BlockState, otherwise could cause NPEs
        if(!((rootState.getBlock() instanceof AbstractBlockMulti) && isRootOfMultiblock(rootState))) return;

        Vector3i[] multiblockOffsets = getMultiblockOffsets();
        BlockPos[] multiblockWorldPositions = getMultiblockWorldPositions(rootPos, rootState);

        for(int i = 0; i < multiblockOffsets.length; i++) {

            Vector3i offset = multiblockOffsets[i];
            BlockPos worldPos = multiblockWorldPositions[i];

            if(world.getBlockState(new BlockPos(worldPos)).getMaterial().isReplaceable()) {
                world.setBlock(worldPos, getStateWithOffset(offset, rootState.getValue(FACING), world.getFluidState(worldPos).getType() == Fluids.WATER), 3);
            }

        }

    }

    public void destroyMultiblock(World world, BlockPos breakSourcePos, BlockState breakSourceState) {
        BlockPos rootPos = getMultiblockCenter(world, breakSourcePos, breakSourceState);
        destroyMultiblockInternal(world, rootPos, breakSourcePos, breakSourceState);
    }

    /**
     * @param exclude Excluded from destruction, cannot be null.
     * @param sampleBlockState Used for calculating what direction the multiblock is facing, can be any member node.
     */
    private void destroyMultiblockInternal(World world, BlockPos rootPos, BlockPos exclude, BlockState sampleBlockState) {
        if(!exclude.equals(rootPos)) {
            world.setBlockAndUpdate(rootPos, world.getBlockState(rootPos).getFluidState().createLegacyBlock());
        }
        for(BlockPos pos : getMultiblockWorldPositions(rootPos, sampleBlockState)) {
            if ((!exclude.equals(pos)) && world.getBlockState(new BlockPos(pos)).getBlock().defaultBlockState().equals(defaultBlockState())) {
                world.setBlockAndUpdate(pos, world.getBlockState(pos).getFluidState().createLegacyBlock());
            }
        }
    }

    public static boolean doesStateMatchPosition(BlockState state, Vector3i multiblockPosition) {
        return state.getValue(MULTIBLOCK_OFFSET_HORIZONTAL) * (state.getValue(MULTIBLOCK_OFFSET_HORIZONTAL_NEGATIVE) ? -1 : 1) == multiblockPosition.getX() &&
               state.getValue(MULTIBLOCK_OFFSET_HEIGHT) * (state.getValue(MULTIBLOCK_OFFSET_HEIGHT_NEGATIVE) ? -1 : 1) == multiblockPosition.getY() &&
               state.getValue(MULTIBLOCK_OFFSET_DEPTH) * (state.getValue(MULTIBLOCK_OFFSET_DEPTH_NEGATIVE) ? -1 : 1) == multiblockPosition.getZ();
    }

    public boolean isRootOfMultiblock(BlockState state) {
        return doesStateMatchPosition(state, new Vector3i(0,0,0));
    }

    public BlockPos getMultiblockCenter(IBlockReader worldIn, BlockPos pos, BlockState sourceState) {
        // Raw Data from BlockState
        int offsetX = sourceState.getValue(MULTIBLOCK_OFFSET_HORIZONTAL); if(sourceState.getValue(MULTIBLOCK_OFFSET_HORIZONTAL_NEGATIVE)) offsetX *= -1;
        int offsetY = sourceState.getValue(MULTIBLOCK_OFFSET_HEIGHT); if(sourceState.getValue(MULTIBLOCK_OFFSET_HEIGHT_NEGATIVE)) offsetY *= -1;
        int offsetZ = sourceState.getValue(MULTIBLOCK_OFFSET_DEPTH); if(sourceState.getValue(MULTIBLOCK_OFFSET_DEPTH_NEGATIVE)) offsetZ *= -1;
        // North (-z)
        if(sourceState.getValue(FACING).getNormal().getZ() == -1) {
            offsetX *= -1;
            offsetZ *= -1;
        }
        // East (+x)
        if(sourceState.getValue(FACING).getNormal().getX() == 1) {
            int temp = offsetX;
            offsetX = offsetZ;
            offsetZ = -temp;
        }
        // South (+z)
        else if(sourceState.getValue(FACING).getNormal().getZ() == 1) {
            offsetZ *= -1;
            offsetZ *= -1;
        }
        // West (-x)
        else if(sourceState.getValue(FACING).getNormal().getX() == -1) {
            int temp = offsetX;
            offsetX = -offsetZ;
            offsetZ = temp;
        }

        // Invert Offsets and Return
        return pos.offset(-offsetX, -offsetY, -offsetZ);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return isRootOfMultiblock(state) ? BlockRenderType.MODEL : BlockRenderType.INVISIBLE;
    }

    @SuppressWarnings("deprecation")
    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        return false;
    }

    /**
     * @return Array of Vector3i, each vector represents the horiz/height/depth offsets for all non-root multiblock components.
     * DOES NOT INCLUDE THE ROOT (0, 0, 0)
     */
    public abstract Vector3i[] getMultiblockOffsets();

    /**
     * @return Array of Vector3i, each vector represents the horiz/height/depth offsets for multiblock components which
     * should be given a {@link TileMultiblockPassthrough} to allow that position to implement capabilities on behalf
     * of the root TileEntity.
     */
    public Vector3i[] getMutiblockOffsetsWhichHavePassthroughTileEntity() {
        return new Vector3i[]{};
    }

    public BlockPos getBlockPosFromOffset(Vector3i offset, BlockPos rootPos, Direction facing) {

        // Calculate Rotation based on "facing" property
        // NOTE: Using opposite of "facing", so all offsets are from pov of the player
        Vector3i rotatedOffset;

        // North (-z) (Default)
        rotatedOffset = offset;
        // East (+x)
        if (facing.getOpposite().getNormal().getX() == 1) {
            rotatedOffset = new Vector3i(-offset.getZ(), offset.getY(), +offset.getX());
        }
        // South (+z)
        else if (facing.getOpposite().getNormal().getZ() == 1) {
            rotatedOffset = new Vector3i(-offset.getX(), offset.getY(), -offset.getZ());
        }
        // West (-x)
        else if (facing.getOpposite().getNormal().getX() == -1) {
            rotatedOffset = new Vector3i(+offset.getZ(), offset.getY(), -offset.getX());
        }

        return rootPos.offset(rotatedOffset.getX(), rotatedOffset.getY(), rotatedOffset.getZ());

    }

    /**
     * Takes in the BlockPos of the root node and the BlockState of any given node of this multiblock in the
     * world and returns the full set of BlockPos that comprise the full structure, EXCLUDING THE ROOT
     *
     * Guaranteed to return in the same order as the offsets in {@link AbstractBlockMulti#getMultiblockOffsets}
     *
     * See {@link AbstractBlockMulti#getMultiblockCenter} for acquiring the root's BlockPos
     *
     * @return Array of all BlockPos that comprise this multiblock, EXCLUDING THE ROOT
     */
    public BlockPos[] getMultiblockWorldPositions(BlockPos rootPos, BlockState state) {

        Vector3i[] offsets = getMultiblockOffsets();
        BlockPos[] positions = new BlockPos[offsets.length];
        Direction facing = state.getValue(FACING);

        for(int i = 0; i < offsets.length; i++) {
            positions[i] = getBlockPosFromOffset(offsets[i], rootPos, facing);
        }

        return positions;

    }

    @Override
    public VoxelShape getShapeForFluidBlocking(BlockState blockState, IBlockReader level, BlockPos blockPos) {
        if(!waterloggable)
            return VoxelShapes.block();
        if(isRootOfMultiblock(blockState))
            return SlabBlock.BOTTOM_AABB;
        return VoxelShapes.empty();
    }

}
