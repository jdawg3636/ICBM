package com.jdawg3636.icbm.common.thread;

import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class RaytracedBlastWorkerThread extends AbstractBlastWorkerThread {

    public Supplier<Random> randomSupplier;
    public Function<BlockPos, BlockState> blockStateSupplier;

    public double explosionCenterPosX;
    public double explosionCenterPosY;
    public double explosionCenterPosZ;

    public int threadCount;
    public int threadNumber;

    public double radius; // used to calculate power, effective radius will be less when encountering block resistances > 1

    public Set<BlockPos> blocksToBeDestroyed = Sets.newHashSet();
    public Set<BlockPos> blocksToBeDecorated = Sets.newHashSet();

    public boolean shouldDecorate(BlockPos blockPos) {
        return false;
    }

    @Override
    public void run() {

        final int batchSize = (int) Math.ceil((2*radius + 1) / (double)threadCount);
        final int radiusInt = (int) Math.ceil(radius);

        // Iterate Over Every Ray to Be Cast (1 ray per block on faces of surrounding cube)
        for(int edgeBlockX = -radiusInt + batchSize * threadNumber; edgeBlockX < -radiusInt + batchSize * (threadNumber+1); ++edgeBlockX) {
            for (int edgeBlockY = -radiusInt; edgeBlockY < radiusInt; ++edgeBlockY) {
                for (int edgeBlockZ = -radiusInt; edgeBlockZ < radiusInt; ++edgeBlockZ) {
                    if(edgeBlockX == -radiusInt || edgeBlockX == radiusInt - 1 || edgeBlockY == -radiusInt || edgeBlockY == radiusInt - 1 || edgeBlockZ == -radiusInt || edgeBlockZ == radiusInt - 1) {

                        // Calculate Ray Deltas
                        double deltaX = edgeBlockX;
                        double deltaY = edgeBlockY;
                        double deltaZ = edgeBlockZ;

                        // Normalize Ray
                        double unnormalizedRayMagnitude = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
                        deltaX /= unnormalizedRayMagnitude;
                        deltaY /= unnormalizedRayMagnitude;
                        deltaZ /= unnormalizedRayMagnitude;

                        // Process Ray
                        double rayPower = radius - 3 * randomSupplier.get().nextFloat();
                        Vector3d currentPos = new Vector3d(explosionCenterPosX, explosionCenterPosY, explosionCenterPosZ);
                        BlockPos currentBlockPos = new BlockPos(currentPos);
                        while (rayPower > 0) {

                            float currentBlockPosExplosionResistance = blockStateSupplier.apply(currentBlockPos).getBlock() instanceof FlowingFluidBlock ? 0 : blockStateSupplier.apply(currentBlockPos).getBlock().getExplosionResistance();

                            double rayPowerReduction = Math.max(1F, (currentBlockPosExplosionResistance + 0.3F) * 0.3F);
                            rayPower -= rayPowerReduction;

                            if ((!blockStateSupplier.apply(currentBlockPos).isAir()) && rayPower > 0) {
                                blocksToBeDestroyed.add(currentBlockPos);
                            }

                            currentPos = currentPos.add(deltaX, deltaY, deltaZ);
                            currentBlockPos = new BlockPos(currentPos);

                        }

                        // Decorate after ray dies
                        if(shouldDecorate(currentBlockPos)) {
                            blocksToBeDecorated.add(currentBlockPos);
                        }

                    }
                }
            }
        }

    }

}
