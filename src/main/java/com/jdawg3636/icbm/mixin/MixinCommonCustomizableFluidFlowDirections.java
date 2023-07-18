package com.jdawg3636.icbm.mixin;

import com.jdawg3636.icbm.common.block.IHasCustomWallPassThroughLogic;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FlowingFluid.class)
public class MixinCommonCustomizableFluidFlowDirections {

    // TODO MC 1.18.2+ switch to (from my understanding) more compatible MixinExtras alternative https://gist.github.com/LlamaLad7/ec597b6d02d39b8a2e35559f9fcce42f
    @Redirect(
            method = "canPassThroughWall",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/util/math/shapes/VoxelShapes;mergedFaceOccludes(Lnet/minecraft/util/math/shapes/VoxelShape;Lnet/minecraft/util/math/shapes/VoxelShape;Lnet/minecraft/util/Direction;)Z"
            )
    )
    public boolean icbm$wrapMergedFaceOccludes(VoxelShape shape, VoxelShape adjacentShape, Direction side, Direction sideDuplicate, IBlockReader level, BlockPos startPos, BlockState startState, BlockPos destPos, BlockState destState) {
        boolean result;
        if (startState.getBlock() instanceof IHasCustomWallPassThroughLogic) {
            result = !(((IHasCustomWallPassThroughLogic)startState.getBlock()).canPassThroughWall(sideDuplicate, level, startPos, startState, destPos, destState));
        }
        else if (destState.getBlock() instanceof IHasCustomWallPassThroughLogic) {
            result = !(((IHasCustomWallPassThroughLogic)destState.getBlock()).canPassThroughWall(sideDuplicate, level, startPos, startState, destPos, destState));
        }
        else {
            result = VoxelShapes.mergedFaceOccludes(shape, adjacentShape, side);
        }
        return result;
    }

}
