package com.jdawg3636.icbm.common.block.centrifugal_deposition_unit;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.machine.AbstractBlockMachine;
import com.jdawg3636.icbm.common.block.multiblock.AbstractBlockMultiTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fml.RegistryObject;

public class BlockCentrifugalDepositionUnit extends AbstractBlockMultiTile {

    public static final Vector3i ITEM_OUTLET_POSITION = new Vector3i(0,0,-1);
    public static final Vector3i LIQUID_INLET_POSITION = new Vector3i(-1,0,0);

    private static final Vector3i[] MULTIBLOCK_PASSTHROUGH_TILE_POSITIONS = {
            new Vector3i(-1, 0, -1),
            LIQUID_INLET_POSITION,
            new Vector3i(-1, 0, 1),
            ITEM_OUTLET_POSITION,
            new Vector3i(0, 0, 1),
            new Vector3i(1, 0, -1),
            new Vector3i(1, 0, 0),
            new Vector3i(1, 0, 1)
    };

    private static final Vector3i[] MULTIBLOCK_POSITIONS = {
            new Vector3i(-1, 0, -1),
            new Vector3i(-1, 0, 0),
            new Vector3i(-1, 0, 1),
            new Vector3i(-1, 1, -1),
            new Vector3i(-1, 1, 0),
            new Vector3i(-1, 1, 1),
            new Vector3i(-1, 2, -1),
            new Vector3i(-1, 2, 0),
            new Vector3i(-1, 2, 1),
            new Vector3i(0, 0, -1),
            /* omitting root pos */
            new Vector3i(0, 0, 1),
            new Vector3i(0, 1, -1),
            new Vector3i(0, 1, 0),
            new Vector3i(0, 1, 1),
            new Vector3i(0, 2, -1),
            new Vector3i(0, 2, 0),
            new Vector3i(0, 2, 1),
            new Vector3i(1, 0, -1),
            new Vector3i(1, 0, 0),
            new Vector3i(1, 0, 1),
            new Vector3i(1, 1, -1),
            new Vector3i(1, 1, 0),
            new Vector3i(1, 1, 1),
            new Vector3i(1, 2, -1),
            new Vector3i(1, 2, 0),
            new Vector3i(1, 2, 1)
    };

    public static VoxelShape SHAPE_FACING_NORTH = ICBMReference.combineVoxelShapes(
            Block.box(-16D, 0D, -16D, 32D, 12D, 32D), // base
            Block.box(24D, 12D, 24D, 32D, 44D, 32D), // pillar
            Block.box(-8D, 12D, -8D, 32D, 16D, 32D), // chamber bottom
            Block.box(-6D, 16D, -6D, 30D, 44D, 30D), // chamber glass
            Block.box(-8D, 44D, -8D, 32D, 48D, 32D) // chamber top
    );

    public static VoxelShape SHAPE_FACING_EAST = ICBMReference.combineVoxelShapes(
            Block.box(-16D, 0D, -16D, 32D, 12D, 32D), // base
            Block.box(-16D, 12D, 24D, -8D, 44D, 32D), // pillar
            Block.box(-16D, 12D, -8D, 24D, 16D, 32D), // chamber bottom
            Block.box(-14D, 16D, -6D, 22D, 44D, 30D), // chamber glass
            Block.box(-16D, 44D, -8D, 24D, 48D, 32D) // chamber top
    );

    public static VoxelShape SHAPE_FACING_SOUTH = ICBMReference.combineVoxelShapes(
            Block.box(-16D, 0D, -16D, 32D, 12D, 32D), // base
            Block.box(-16D, 12D, -16D, -8D, 44D, -8D), // pillar
            Block.box(-16D, 12D, -16D, 24D, 16D, 24D), // chamber bottom
            Block.box(-14D, 16D, -14D, 22D, 44D, 22D), // chamber glass
            Block.box(-16D, 44D, -16D, 24D, 48D, 24D) // chamber top
    );

    public static VoxelShape SHAPE_FACING_WEST = ICBMReference.combineVoxelShapes(
        Block.box(-16D, 0D, -16D, 32D, 12D, 32D), // base
        Block.box(24D, 12D, -16D, 32D, 44D, -8D), // pillar
        Block.box(-8D, 12D, -16D, 32D, 16D, 24D), // chamber bottom
        Block.box(-6D, 16D, -14D, 30D, 44D, 22D), // chamber glass
        Block.box(-8D, 44D, -16D, 32D, 48D, 24D) // chamber top
    );

    public BlockCentrifugalDepositionUnit(RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType) {
        this(tileEntityType, false);
    }

    public BlockCentrifugalDepositionUnit(RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType, boolean waterloggable) {
        super(tileEntityType, waterloggable);
    }

    public BlockCentrifugalDepositionUnit(Properties properties, RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType) {
        super(properties, tileEntityType, false);
    }

    @Override
    public Vector3i[] getMultiblockOffsets() {
        return MULTIBLOCK_POSITIONS;
    }

    @Override
    public Vector3i[] getMutiblockOffsetsWhichHavePassthroughTileEntity() {
        return MULTIBLOCK_PASSTHROUGH_TILE_POSITIONS;
    }

    public static boolean isBottomBlock(BlockState state) {
        return getMultiblockOffsetsFromBlockState(state).getY() == 0;
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext context) {
        Vector3i multiblockOffsetsForState = getMultiblockOffsetsFromBlockState(state);
        switch(state.getValue(AbstractBlockMachine.FACING)) {
            case NORTH:
                return SHAPE_FACING_NORTH.move(multiblockOffsetsForState.getX(), -multiblockOffsetsForState.getY(), multiblockOffsetsForState.getZ());
            case EAST:
                return SHAPE_FACING_EAST.move(-multiblockOffsetsForState.getZ(), -multiblockOffsetsForState.getY(), multiblockOffsetsForState.getX());
            case WEST:
                return SHAPE_FACING_WEST.move(multiblockOffsetsForState.getZ(), -multiblockOffsetsForState.getY(), -multiblockOffsetsForState.getX());
            default:
                return SHAPE_FACING_SOUTH.move(-multiblockOffsetsForState.getX(), -multiblockOffsetsForState.getY(), -multiblockOffsetsForState.getZ());
        }
    }

}
