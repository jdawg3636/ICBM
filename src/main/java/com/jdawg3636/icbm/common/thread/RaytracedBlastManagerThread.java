package com.jdawg3636.icbm.common.thread;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class RaytracedBlastManagerThread extends AbstractBlastManagerThread {

    public Supplier<Random> randomSupplier;
    public Function<BlockPos, BlockState> blockStateSupplier;
    public Function<BlockPos, TileEntity> tileEntitySupplier;
    public Consumer<BlockPos> decorationCallback;

    public double explosionCenterPosX;
    public double explosionCenterPosY;
    public double explosionCenterPosZ;

    public float radius;

    private ArrayList<RaytracedBlastWorkerThread> threadPool;
    public int threadCount = 4;

    @Override
    public String getRegistryName() {
        return "icbm:raytraced";
    }

    @Override
    public void initializeLevelCallbacks(ServerWorld level) {
        randomSupplier = () -> level.random;
        blockStateSupplier = level::getBlockState;
        tileEntitySupplier = level::getBlockEntity;
        decorationCallback = (BlockPos blockPos) -> decorate(level, blockPos);
    }

    public RaytracedBlastWorkerThread getNewWorkerThread() {
        return new RaytracedBlastWorkerThread();
    }

    public void decorate(World level, BlockPos blockPos) {}

    // Run on this thread once it is started
    @Override
    public void run() {

        // Initialize Workers
        threadPool = new ArrayList<>();
        for(int threadNumber = 0; threadNumber < threadCount; ++threadNumber) {
            RaytracedBlastWorkerThread worker = getNewWorkerThread();
            worker.randomSupplier = randomSupplier;
            worker.blockStateSupplier = blockStateSupplier;
            worker.explosionCenterPosX = explosionCenterPosX;
            worker.explosionCenterPosY = explosionCenterPosY;
            worker.explosionCenterPosZ = explosionCenterPosZ;
            worker.radius = radius;
            worker.threadCount = threadCount;
            worker.threadNumber = threadNumber;
            threadPool.add(worker);
        }

        // Begin Calculation
        boolean calculating = true;
        threadPool.forEach(RaytracedBlastWorkerThread::start);

        // Keep Thread Alive until All Workers have Completed
        while(calculating) {
            if(interrupted()) break;
            calculating = false;
            for(RaytracedBlastWorkerThread worker : threadPool) {
                if (worker.isAlive()) {
                    calculating = true;
                    break;
                }
            }
        }

    }

    @Override
    public Runnable getPostCompletionFunction(final ServerWorld level) {

        return () -> {
            for(RaytracedBlastWorkerThread worker : threadPool) {

                // Remove Blocks in World
                for (BlockPos blockPos : worker.blocksToBeDestroyed) {
                    try {
                        blockStateSupplier.apply(blockPos).onBlockExploded(level, blockPos, null);
                    } catch (Exception ignored) {/* Using try/catch just in case the null-valued explosion parameter causes any issues */}
                }

                // Decorate Blast Crater
                for (BlockPos blockPos : worker.blocksToBeDecorated) {
                    decorationCallback.accept(blockPos);
                }

            }
        };

    }

    // Serialization
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putDouble("explosion_center_pos_x", explosionCenterPosX);
        nbt.putDouble("explosion_center_pos_y", explosionCenterPosY);
        nbt.putDouble("explosion_center_pos_z", explosionCenterPosZ);
        nbt.putFloat ("radius", radius);
        return nbt;
    }

    // Deserialization
    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        explosionCenterPosX = nbt.getDouble("explosion_center_pos_x");
        explosionCenterPosY = nbt.getDouble("explosion_center_pos_y");
        explosionCenterPosZ = nbt.getDouble("explosion_center_pos_z");
        radius              = nbt.getFloat ("radius");
    };

}
