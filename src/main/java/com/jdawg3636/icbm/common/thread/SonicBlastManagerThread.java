package com.jdawg3636.icbm.common.thread;

import com.jdawg3636.icbm.common.entity.EntitySonicBlast;
import com.jdawg3636.icbm.common.reg.EntityReg;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public class SonicBlastManagerThread extends AbstractBlastManagerThread {

    public Supplier<Random> randomSupplier;
    public Function<BlockPos, BlockState> blockStateSupplier;
    public Function<BlockPos, TileEntity> tileEntitySupplier;

    public double explosionCenterPosX;
    public double explosionCenterPosY;
    public double explosionCenterPosZ;

    public float radius;
    public int blocksAffectedPerTick;

    private ArrayList<RaytracedBlastWorkerThread> threadPool;
    public int threadCount = 4;

    @Override
    public String getRegistryName() {
        return "icbm:sonic";
    }

    @Override
    public void initializeLevelCallbacks(ServerWorld level) {
        randomSupplier = () -> level.random;
        blockStateSupplier = level::getBlockState;
        tileEntitySupplier = level::getBlockEntity;
    }

    // Run on this thread once it is started
    @Override
    public void run() {

        // Initialize Workers
        threadPool = new ArrayList<>();
        for(int threadNumber = 0; threadNumber < threadCount; ++threadNumber) {
            RaytracedBlastWorkerThread worker = new RaytracedBlastWorkerThread();
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

            // Combine Worker Results
            ArrayList<BlockPos> workerResults = new ArrayList<>();
            for(RaytracedBlastWorkerThread worker : threadPool) {
                workerResults.addAll(worker.blocksToBeDestroyed);
            }

            // Sort Results from Closest to Furthest
            workerResults.sort((BlockPos a, BlockPos b) -> {
                final double aDist = (explosionCenterPosX - a.getX()) * (explosionCenterPosX - a.getX()) + (explosionCenterPosY - a.getY()) * (explosionCenterPosY - a.getY()) + (explosionCenterPosZ - a.getZ()) * (explosionCenterPosZ - a.getZ());
                final double bDist = (explosionCenterPosX - b.getX()) * (explosionCenterPosX - b.getX()) + (explosionCenterPosY - b.getY()) * (explosionCenterPosY - b.getY()) + (explosionCenterPosZ - b.getZ()) * (explosionCenterPosZ - b.getZ());
                return Double.compare(aDist, bDist);
            });

            // Spawn Blast Entity
            EntitySonicBlast entity = EntityReg.BLAST_SONIC.get().create(level);
            if(entity != null) {
                entity.setPos(explosionCenterPosX, explosionCenterPosY, explosionCenterPosZ);
                entity.targetBlocks = workerResults;
                entity.blocksAffectedPerTick = blocksAffectedPerTick;
                level.addFreshEntity(entity);
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
        nbt.putFloat ("blocksAffectedPerTick", blocksAffectedPerTick);
        return nbt;
    }

    // Deserialization
    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        explosionCenterPosX   = nbt.getDouble("explosion_center_pos_x");
        explosionCenterPosY   = nbt.getDouble("explosion_center_pos_y");
        explosionCenterPosZ   = nbt.getDouble("explosion_center_pos_z");
        radius                = nbt.getFloat ("radius");
        blocksAffectedPerTick = nbt.getInt   ("blocksAffectedPerTick");
    }

}
