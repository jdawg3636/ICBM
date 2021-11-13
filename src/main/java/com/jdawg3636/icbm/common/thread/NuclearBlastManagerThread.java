package com.jdawg3636.icbm.common.thread;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public class NuclearBlastManagerThread extends AbstractBlastManagerThread {

    public Supplier<Random> randomSupplier;
    public Function<BlockPos, BlockState> blockStateSupplier;
    public Function<BlockPos, TileEntity> tileEntitySupplier;

    public double explosionCenterPosX;
    public double explosionCenterPosY;
    public double explosionCenterPosZ;

    public float radius;

    private ArrayList<NuclearBlastWorkerThread> threadPool;
    public int threadCount;

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

                // Remove Blocks and Calculate Items to Be Dropped
                ObjectArrayList<Pair<ItemStack, BlockPos>> itemStacksToBeDropped = new ObjectArrayList<>();
                for(BlockPos blockPos : workerResults) {
                    BlockState blockState = blockStateSupplier.apply(blockPos);
                    if (!blockState.isAir()) {

                        boolean shouldDrop = false;
                        try {
                            shouldDrop = blockState.getBlock().dropFromExplosion(null);
                        } catch (Exception ignored) {/* Using try/catch just in case the null-valued explosion parameter causes any issues */}

                        // Update ItemStacks to be dropped
                        if (shouldDrop) {

                            TileEntity tileEntity = tileEntitySupplier.apply(blockPos);
                            LootContext.Builder lootContextBuilder = (new LootContext.Builder(level)).withRandom(randomSupplier.get()).withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf(blockPos)).withParameter(LootParameters.TOOL, ItemStack.EMPTY).withOptionalParameter(LootParameters.BLOCK_ENTITY, tileEntity).withOptionalParameter(LootParameters.THIS_ENTITY, null);
                            lootContextBuilder.withParameter(LootParameters.EXPLOSION_RADIUS, this.radius);

                            blockState.getDrops(lootContextBuilder).forEach((itemStack) -> {
                                addBlockDrops(itemStacksToBeDropped, itemStack, blockPos.immutable());
                            });

                        }

                        // Destroy Block in World
                        try {
                            blockState.onBlockExploded(level, blockPos, null);
                        } catch (Exception ignored) {/* Using try/catch just in case the null-valued explosion parameter causes any issues */}

                    }
                }

                // Drop Items
                for(Pair<ItemStack, BlockPos> pair : itemStacksToBeDropped) {
                    Block.popResource(level, pair.getSecond(), pair.getFirst());
                }

            }
        };
    }

    // Utility Function Copied from Vanilla, used to merge ItemStacks prior to dropping them
    private static void addBlockDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> itemStacksToBeDropped, ItemStack newItemStack, BlockPos blockPos) {

        int i = itemStacksToBeDropped.size();

        for(int j = 0; j < i; ++j) {
            Pair<ItemStack, BlockPos> pair = itemStacksToBeDropped.get(j);
            ItemStack oldItemStack = pair.getFirst();
            if (ItemEntity.areMergable(oldItemStack, newItemStack)) {
                ItemStack mergedItemStack = ItemEntity.merge(oldItemStack, newItemStack, 16);
                itemStacksToBeDropped.set(j, Pair.of(mergedItemStack, pair.getSecond()));
                if (newItemStack.isEmpty()) {
                    return;
                }
            }
        }

        itemStacksToBeDropped.add(Pair.of(newItemStack, blockPos));

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
