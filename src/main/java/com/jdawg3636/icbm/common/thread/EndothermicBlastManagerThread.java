package com.jdawg3636.icbm.common.thread;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class EndothermicBlastManagerThread extends RaytracedBlastManagerThread {

    @Override
    public String getRegistryName() {
        return "icbm:endothermic";
    }

    @Override
    public RaytracedBlastWorkerThread getNewWorkerThread() {
        return new EndothermicBlastWorkerThread();
    }

    @Override
    public void decorate(World level, BlockPos blockPos) {
        if(level.random.nextFloat() < 0.3) {
            level.setBlockAndUpdate(blockPos, Blocks.SNOW_BLOCK.defaultBlockState());
        }
        else {
            level.setBlockAndUpdate(blockPos, Blocks.ICE.defaultBlockState());
        }
    }

    @Override
    public Runnable getPostCompletionFunction(final ServerWorld level) {

        return () -> {
            for(RaytracedBlastWorkerThread worker : threadPool) {

                // Decorate Blast Crater
                for (BlockPos blockPos : worker.blocksToBeDecorated) {
                    decorationCallback.accept(blockPos);
                }

                // Replace Target Blocks with Snow/Air/Ice
                for (BlockPos blockPos : worker.blocksToBeDestroyed) {
                    try {
                        blockStateSupplier.apply(blockPos).onBlockExploded(level, blockPos, null);
                        if(level.random.nextFloat() < 0.80) {
                            decorationCallback.accept(blockPos);
                        }
                        else {
                            level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                        }
                    } catch (Exception ignored) {/* Using try/catch just in case the null-valued explosion parameter causes any issues */}
                }

            }
        };

    }

}
