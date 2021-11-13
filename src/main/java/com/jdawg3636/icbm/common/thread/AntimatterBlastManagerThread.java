package com.jdawg3636.icbm.common.thread;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public class AntimatterBlastManagerThread extends AbstractBlastManagerThread {

    public Supplier<Random> randomSupplier;
    public Function<BlockPos, BlockState> blockStateSupplier;
    public Function<BlockPos, TileEntity> tileEntitySupplier;

    public double explosionCenterPosX;
    public double explosionCenterPosY;
    public double explosionCenterPosZ;

    public static final int threadCountX = 2;
    public static final int threadCountY = 1;
    public static final int threadCountZ = 2;

    public int radius;
    public int fuzzyEdgeThickness;

    ArrayList<AntimatterBlastWorkerThread> threadPool;

    @Override
    public Runnable getPostCompletionFunction(ServerWorld level) {
        return () -> {
            ArrayList<BlockPos> results = new ArrayList<>();
            for(AntimatterBlastWorkerThread worker : threadPool) {
                results.addAll(worker.blocksToBeDestroyed);
            }
            /*todo remove debug*/System.out.println("Pulled " + results.size() + " total results from Antimatter Worker Threads");
            for(BlockPos result : results) {
                level.setBlockAndUpdate(result, Blocks.AIR.defaultBlockState());
            }
        };
    }

    @Override
    public void initializeLevelCallbacks(ServerWorld level) {
        randomSupplier = () -> level.random;
        blockStateSupplier = level::getBlockState;
        tileEntitySupplier = level::getBlockEntity;
    }

    @Override
    public void run() {
        // Initialize Workers
        threadPool = new ArrayList<>();
        for(int i = 0; i < threadCountX; ++i) {
            for(int j = 0; j < threadCountY; ++j) {
                for(int k = 0; k < threadCountZ; ++k) {
                    AntimatterBlastWorkerThread worker = new AntimatterBlastWorkerThread();
                    worker.randomSupplier = randomSupplier;
                    worker.blockStateSupplier = blockStateSupplier;
                    worker.explosionCenterPosX = explosionCenterPosX;
                    worker.explosionCenterPosY = explosionCenterPosY;
                    worker.explosionCenterPosZ = explosionCenterPosZ;
                    worker.radius = radius;
                    worker.fuzzyEdgeThickness = fuzzyEdgeThickness;
                    worker.threadCountX = threadCountX;
                    worker.threadCountY = threadCountY;
                    worker.threadCountZ = threadCountZ;
                    worker.threadNumberX = i;
                    worker.threadNumberY = j;
                    worker.threadNumberZ = k;
                    threadPool.add(worker);
                }
            }
        }

        // Begin Calculation
        boolean calculating = true;
        threadPool.forEach(AntimatterBlastWorkerThread::start);

        // Keep Thread Alive until All Workers have Completed
        while(calculating) {
            if(interrupted()) break;
            calculating = false;
            for(AntimatterBlastWorkerThread worker : threadPool) {
                if (worker.isAlive()) {
                    calculating = true;
                    break;
                }
            }
        }
    }

    // Serialization
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("manager_thread_type", "icbm:antimatter");
        nbt.putDouble("explosion_center_pos_x", explosionCenterPosX);
        nbt.putDouble("explosion_center_pos_y", explosionCenterPosY);
        nbt.putDouble("explosion_center_pos_z", explosionCenterPosZ);
        nbt.putInt   ("radius", radius);
        nbt.putInt   ("fuzzy_edge_thickness", fuzzyEdgeThickness);
        return nbt;
    }

    // Deserialization
    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        explosionCenterPosX = nbt.getDouble("explosion_center_pos_x");
        explosionCenterPosY = nbt.getDouble("explosion_center_pos_y");
        explosionCenterPosZ = nbt.getDouble("explosion_center_pos_z");
        radius              = nbt.getInt("radius");
        fuzzyEdgeThickness  = nbt.getInt("fuzzy_edge_thickness");
    };

}
