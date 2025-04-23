package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.event.ICBMBlastEventUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.*;
import java.util.stream.Collectors;

public class EntityRedmatterBlast extends EntityLingeringBlast {

    private double animationPercent = 0;
    private final Queue<BlockPos> blocksToDestroy = new ArrayDeque<>();
    private final int maxBlocksDestroyedPerTick = 50;

    public EntityRedmatterBlast(EntityType<?> entityType, World level) {
        super(entityType, level, 20 * 20); // todo: make lifetime configurable
    }

    @Override
    public void tick() {
        // TODO: Implement player movement for Red Matter (black hole)
        if(level != null) {
            if(level.isClientSide()) {
                addAnimationPercent(0.25D);
            }
            else if (level instanceof ServerWorld) {
                ServerWorld serverLevel = (ServerWorld) level;
                // Check Lifetime
                if(ticksRemaining <= 0) {
                    kill();
                    return;
                }
                // Only perform calculation logic once/second
                if(tickCount % 20 == 0) {
                    // If we're out of blocks to destroy, try to find more.
                    if(blocksToDestroy.isEmpty()) {
                        // Config values
                        final int maxRadius = 50; // todo make configurable
                        final double fuzzinessPercentage = ICBMReference.COMMON_CONFIG.getAntimatterFuzzinessPercentage(); // todo make configurable separate from antimatter
                        final boolean canDestroyIndestructible = ICBMReference.COMMON_CONFIG.getAntimatterCanDestroyBedrock(); // todo make configurable separate from antimatter
                        // Calculate current blocks that are candidates for deletion this tick. Radius is increased as-necessary to give illusion of blocks from the center being affected first
                        List<BlockPos> candidates = null;
                        for (int currentRadius = 8; currentRadius < maxRadius; ++currentRadius) {
                            // Reset RNG each iteration so that repeat passes have deterministic noise patterns
                            Random currentRandom = new Random(serverLevel.getSeed());
                            // Use antimatter algorithm to calculate a sphere of blocks
                            candidates = ICBMBlastEventUtil.getBlockPositionsWithinFuzzySphere(getX(), getY(), getZ(), currentRadius, currentRandom, fuzzinessPercentage).stream()
                                    .filter(pos -> !level.getBlockState(pos).isAir(level, pos))
                                    .filter(pos -> canDestroyIndestructible || level.getBlockState(pos).getBlock().explosionResistance < 3_600_000F)
                                    .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                                        Collections.shuffle(list, currentRandom);
                                        return list;
                                    }));
                            // If candidates are sufficient to satiate, break out of loop.
                            if (candidates.size() >= 20 * maxBlocksDestroyedPerTick) {
                                break;
                            }
                        }
                        // Transfer local list into field queue
                        blocksToDestroy.addAll(candidates);
                    }
                    // If we're up to maximum radius and still out of blocks, start depleting ticks remaining.
                    if(blocksToDestroy.isEmpty()) {
                        ticksRemaining -= 20;
                    }
                }
                // Break Blocks
                for(int i = 0; i < maxBlocksDestroyedPerTick; ++i) {
                    if(!blocksToDestroy.isEmpty()) {
                        // TODO sometimes make flying block instead of destroy
                        level.setBlockAndUpdate(blocksToDestroy.remove(), Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
    }

    public void addAnimationPercent(double increment) {
        animationPercent += increment;
        while(animationPercent > 100) {
            animationPercent -= 100D;
        }
    }

    public float getAnimationRadians() {
        return (float)(animationPercent * 0.01 * 2 * Math.PI);
    }

}
