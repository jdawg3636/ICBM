package com.jdawg3636.icbm.common.thread;

import com.jdawg3636.icbm.common.reg.BlockReg;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class NuclearBlastManagerThread extends AbstractBlastManagerThread {

    public Supplier<Random> randomSupplier;
    public Function<BlockPos, BlockState> blockStateSupplier;
    public Function<BlockPos, TileEntity> tileEntitySupplier;
    public Consumer<BlockPos> decorationCallback;

    public double explosionCenterPosX;
    public double explosionCenterPosY;
    public double explosionCenterPosZ;

    public float radius;

    private ArrayList<NuclearBlastWorkerThread> threadPool;
    public int threadCount = 4;

    @Override
    public void initializeLevelCallbacks(ServerWorld level) {
        randomSupplier = () -> level.random;
        blockStateSupplier = level::getBlockState;
        tileEntitySupplier = level::getBlockEntity;
        decorationCallback = (BlockPos blockPos) -> level.setBlockAndUpdate(blockPos, BlockReg.RADIOACTIVE_MATERIAL.get().defaultBlockState());
    }

    // Run on this thread once it is started
    @Override
    public void run() {

        // Initialize Workers
        threadPool = new ArrayList<>();
        for(int threadNumber = 0; threadNumber < threadCount; ++threadNumber) {
            NuclearBlastWorkerThread worker = new NuclearBlastWorkerThread();
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
        threadPool.forEach(NuclearBlastWorkerThread::start);

        // Keep Thread Alive until All Workers have Completed
        while(calculating) {
            if(interrupted()) break;
            calculating = false;
            for(NuclearBlastWorkerThread worker : threadPool) {
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
            System.out.println("Post-Completion Nuclear");
            for(NuclearBlastWorkerThread worker : threadPool) {

                ArrayList<BlockPos> workerResults = new ArrayList<>(worker.blocksToBeDestroyed);
                Collections.shuffle(workerResults, randomSupplier.get());
                System.out.println("Worker Found " + workerResults.size() + " Results!");

                // Remove Blocks in World
                ObjectArrayList<Pair<ItemStack, BlockPos>> itemStacksToBeDropped = new ObjectArrayList<>();
                for(Iterator<BlockPos> it = worker.blocksToBeDestroyed.iterator(); it.hasNext(); /**/) {
                    BlockPos blockPos = it.next();
                    try {
                        blockStateSupplier.apply(blockPos).onBlockExploded(level, blockPos, null);
                    } catch (Exception ignored) {/* Using try/catch just in case the null-valued explosion parameter causes any issues */}
                }

                // Decorate Blast Crater
                for(Iterator<BlockPos> it = worker.blocksToBeDecorated.iterator(); it.hasNext(); /**/) {
                    decorationCallback.accept(it.next());
                }

            }
        };
    }

    // Serialization
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("manager_thread_type", "icbm:nuclear");
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
