package com.jdawg3636.icbm.common.thread;

import com.jdawg3636.icbm.common.event.AbstractBlastEvent;
import com.jdawg3636.icbm.common.event.EventBlastIncendiary;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ExothermicBlastManagerThread extends RaytracedBlastManagerThread {

    @Override
    public String getRegistryName() {
        return "icbm:exothermic";
    }

    @Override
    public RaytracedBlastWorkerThread getNewWorkerThread() {
        return new ExothermicBlastWorkerThread();
    }

    @Override
    public void decorate(World level, BlockPos blockPos) {
        if(level.random.nextFloat() < 0.3) {
            level.setBlockAndUpdate(blockPos, Blocks.LAVA.defaultBlockState());
        }
        else {
            level.setBlockAndUpdate(blockPos, Blocks.NETHERRACK.defaultBlockState());
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

                // Replace Target Blocks with Fire
                for (BlockPos blockPos : worker.blocksToBeDestroyed) {
                    try {
                        blockStateSupplier.apply(blockPos).onBlockExploded(level, blockPos, null);
                        if(level.random.nextFloat() < 0.15) {
                            decorationCallback.accept(blockPos);
                        }
                        else {
                            level.setBlockAndUpdate(blockPos, Blocks.FIRE.defaultBlockState());
                        }
                    } catch (Exception ignored) {/* Using try/catch just in case the null-valued explosion parameter causes any issues */}
                }

                // Run Incendiary Blast Routine (No event firing - call directly)
                (new EventBlastIncendiary(new BlockPos(explosionCenterPosX, explosionCenterPosY, explosionCenterPosZ), level, AbstractBlastEvent.Type.EXPLOSIVES, Direction.DOWN)).executeBlast();

            }
        };

    }

}
