package com.jdawg3636.icbm.common.thread;

import com.jdawg3636.icbm.common.block.multiblock.AbstractBlockMulti;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IClearable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.CapabilityItemHandler;

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
    public String getRegistryName() {
        return "icbm:antimatter";
    }

    @Override
    public Runnable getPostCompletionFunction(ServerWorld level) {
        return () -> {
            ArrayList<BlockPos> results = new ArrayList<>();
            for(AntimatterBlastWorkerThread worker : threadPool) {
                results.addAll(worker.blocksToBeDestroyed);
            }
            for(BlockPos result : results) {
                // Clear BlockEntity Contents
                BlockState existingBlockState = level.getBlockState(result);
                TileEntity tileentity = (existingBlockState.getBlock() instanceof AbstractBlockMulti) // TODO: Look into making this compatible with other mods' multiblocks
                        ? level.getBlockEntity(((AbstractBlockMulti)existingBlockState.getBlock()).getMultiblockCenter(level, result, existingBlockState))
                        : level.getBlockEntity(result);
                if(tileentity != null) {
                    // Ensure contents generated before clearing (otherwise vanilla will automatically generate contents later when broken)
                    if (tileentity instanceof LockableLootTileEntity) {
                        ((LockableLootTileEntity) tileentity).unpackLootTable((PlayerEntity) null);
                    }
                    // Clear using Forge Item Handler Capability
                    tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
                        for (int i = 0; i < itemHandler.getSlots(); ++i) {
                            itemHandler.extractItem(i, itemHandler.getStackInSlot(i).getCount(), false);
                        }
                    });
                    // Clear using Vanilla Interface Method (redundant, but may catch some non-API inventory implementations from other mods)
                    IClearable.tryClear(tileentity);
                }
                // Break Block and Update Neighbors - could probably optimize this by minimizing block updates
                level.setBlock(result, Blocks.AIR.defaultBlockState(), 2);
                level.blockUpdated(result, Blocks.AIR);
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
        nbt.putString("manager_thread_type", getRegistryName());
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
