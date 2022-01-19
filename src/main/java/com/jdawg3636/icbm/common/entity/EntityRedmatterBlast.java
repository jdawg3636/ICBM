package com.jdawg3636.icbm.common.entity;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntityRedmatterBlast extends EntityLingeringBlast {

    public List<BlockPos> blocksToDestroy;
    private Iterator<BlockPos> iterator;

    double animationPercent = 0;

    public EntityRedmatterBlast(EntityType<?> entityType, World level) {
        super(entityType, level, 20 * 20); // todo: make lifetime configurable
    }

    @Override
    public void tick() {
        if(level != null) {
            if(level.isClientSide()) {
                addAnimationPercent(0.25D);
            }
            else {
                // Check Lifetime
                if(ticksRemaining <= 0) {
                    kill();
                    return;
                }
                // Confirm Iterator Exists
                if(iterator == null && blocksToDestroy != null) {
                    iterator = blocksToDestroy.iterator();
                }
                // Break Blocks
                for(int i = 0; i < 50; ++i) {
                    if(iterator != null && iterator.hasNext()) {
                        level.setBlockAndUpdate(iterator.next(), Blocks.AIR.defaultBlockState());
                    }
                }
                // Decrement Lifetime
                if(iterator == null || !iterator.hasNext()) {
                    --ticksRemaining;
                }
            }
        }
    }

    public void addAnimationPercent(double increment) {
        animationPercent += increment;
        while(animationPercent > 100) animationPercent -= 100D;
    }

    public float getAnimationRadians() {
        return (float)(animationPercent * 0.01 * 2 * Math.PI);
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt) {
        // Call Super
        super.addAdditionalSaveData(nbt);
        // Confirm Iterator Exists (if possible)
        if(iterator == null && blocksToDestroy != null) {
            iterator = blocksToDestroy.iterator();
        }
        // If Iterator Exists, Serialize Blocks
        if(iterator != null) {
            CompoundNBT nbtBlocks = new CompoundNBT();
            for(int i = 0; iterator.hasNext(); ++i) {
                BlockPos posToSerialize = iterator.next();
                nbtBlocks.putIntArray(Integer.toString(i), new int[]{posToSerialize.getX(), posToSerialize.getY(), posToSerialize.getZ()});
            }
            nbt.put("BlocksToDestroy", nbtBlocks);
        }
        // Hacky Fix to Preserve Data
        readAdditionalSaveData(nbt);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT nbt) {
        // Call Super
        super.readAdditionalSaveData(nbt);
        // Reinitialize List
        blocksToDestroy = new ArrayList<BlockPos>();
        // Read Into List
        if(nbt.contains("BlocksToDestroy")) {
            CompoundNBT nbtBlocks = nbt.getCompound("BlocksToDestroy");
            for(int i = 0; nbtBlocks.contains(Integer.toString(i)); ++i) {
                int[] currentBlockPosAsArray = nbtBlocks.getIntArray(Integer.toString(i));
                blocksToDestroy.add(new BlockPos(currentBlockPosAsArray[0], currentBlockPosAsArray[1], currentBlockPosAsArray[2]));
            }
        }
        // Reinitialize Iterator
        iterator = blocksToDestroy.iterator();
    }

}
