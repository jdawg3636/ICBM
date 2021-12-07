package com.jdawg3636.icbm.common.thread;

import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class AntimatterBlastWorkerThread extends AbstractBlastWorkerThread {

    public Supplier<Random> randomSupplier;
    public Function<BlockPos, BlockState> blockStateSupplier;

    public double explosionCenterPosX;
    public double explosionCenterPosY;
    public double explosionCenterPosZ;

    public int threadCountX;
    public int threadCountY;
    public int threadCountZ;

    public int threadNumberX;
    public int threadNumberY;
    public int threadNumberZ;

    public int radius;
    public int fuzzyEdgeThickness;

    public Set<BlockPos> blocksToBeDestroyed = Sets.newHashSet();

    @Override
    public void run() {

        final int blockSizeX = (int) Math.ceil((double)(2 * radius / threadCountX));
        final int blockSizeY = (int) Math.ceil((double)(2 * radius / threadCountY));
        final int blockSizeZ = (int) Math.ceil((double)(2 * radius / threadCountZ));

        for(int offsetX = -radius + blockSizeX * threadNumberX; offsetX < -radius + blockSizeX * (threadNumberX+1); ++offsetX) {
            for(int offsetY = -radius + blockSizeY * threadNumberY; offsetY < -radius + blockSizeY * (threadNumberY+1); ++offsetY) {
                for(int offsetZ = -radius + blockSizeZ * threadNumberZ; offsetZ < -radius + blockSizeZ * (threadNumberZ+1); ++offsetZ) {
                    BlockPos blockPos = new BlockPos(offsetX + explosionCenterPosX, offsetY + explosionCenterPosY, offsetZ + explosionCenterPosZ);
                    if(!blockStateSupplier.apply(blockPos).isAir()) {
                        int distanceFromCenterSquared = offsetX * offsetX + offsetY * offsetY + offsetZ * offsetZ;
                        if(distanceFromCenterSquared < (radius - fuzzyEdgeThickness) * (radius - fuzzyEdgeThickness)) {
                            blocksToBeDestroyed.add(blockPos);
                        }
                        else if(distanceFromCenterSquared < radius * radius && randomSupplier.get().nextFloat() < 0.4F/*todo make this percentage configurable*/) {
                            blocksToBeDestroyed.add(blockPos);
                        }
                    }
                }
            }
        }

    }

}
