package com.jdawg3636.icbm.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public interface IHasCustomWallPassThroughLogic {

    default VoxelShape getShapeForFluidBlocking(BlockState blockState, IBlockReader level, BlockPos blockPos) {
        return VoxelShapes.empty();
    }

    // Note: it is best not to override this method since the check is run from both directions
    default boolean canPassThroughWall(Direction side, IBlockReader level, BlockPos startPos, BlockState startState, BlockPos destPos, BlockState destState) {
        VoxelShape startShape = startState.getCollisionShape(level, startPos);
        VoxelShape destShape = destState.getCollisionShape(level, destPos);
        if (startState.getBlock() instanceof IHasCustomWallPassThroughLogic) {
            startShape = ((IHasCustomWallPassThroughLogic)startState.getBlock()).getShapeForFluidBlocking(startState, level, startPos);
        }
        if (destState.getBlock() instanceof IHasCustomWallPassThroughLogic) {
            destShape = ((IHasCustomWallPassThroughLogic)destState.getBlock()).getShapeForFluidBlocking(destState, level, destPos);
        }
        return !VoxelShapes.mergedFaceOccludes(startShape, destShape, side);
    }

}
