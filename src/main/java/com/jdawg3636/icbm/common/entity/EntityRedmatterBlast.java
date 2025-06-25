package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.event.ICBMBlastEventUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
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
            // Entity movement
//            if(!level.isClientSide() && ticksRemaining % 5 == 0) {
                for (Entity entity : this.level.getEntitiesOfClass(Entity.class, new AxisAlignedBB(blockPosition()).inflate(ICBMReference.COMMON_CONFIG.getBlastRadiusRedmatter()))) {
                    if (!(entity instanceof EntityRedmatterBlast) && !(entity instanceof ItemEntity) && !(ICBMReference.entityIsAPlayerInCreativeOrSpectatorMode(entity))) {
                        applyBlackHoleMovementToEntity(entity);
                    }
                }
//            }
            if(level.isClientSide()) {
                // Visual Animation
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
                        final double maxRadius = ICBMReference.COMMON_CONFIG.getBlastRadiusRedmatter();
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

    public void applyBlackHoleMovementToEntity(Entity entity) {

        double radius = ICBMReference.COMMON_CONFIG.getBlastRadiusRedmatter();
        Vector3d entityPosition = entity.getPosition(0f);
        Vector3d blackHolePosition = this.getPosition(0f);
        Vector3d deltaPosition = entityPosition.subtract(blackHolePosition);

        /** The percentage of the closeness of the entity. */
        double xPercentage = 1 - (deltaPosition.x / radius);
        double yPercentage = 1 - (deltaPosition.y / radius);
        double zPercentage = 1 - (deltaPosition.z / radius);
        double closenessPercentage = 1.0 - (entityPosition.distanceTo(blackHolePosition) / radius);

        if(closenessPercentage > 1.0) {
            return;
        }

        System.out.printf("%s, %s\n", deltaPosition, closenessPercentage);
//        Vector3d rotatedX = deltaPosition.xRot((float)(0.0261799 * closenessPercentage * 1.0));
//        Vector3d rotatedXY = rotatedX.yRot((float)(0.0261799 * closenessPercentage * 1.0));
//        Vector3d rotatedXYZ = rotatedXY.zRot((float)(0.0261799 * closenessPercentage * 1.0));
        Vector3d rotatedXYZ = rotateVector(deltaPosition, 0.0261799 * closenessPercentage);
        System.out.printf("%s, %s\n", rotatedXYZ, 1.0 - (rotatedXYZ.distanceTo(Vector3d.ZERO) / radius));
        Vector3d newPosition = blackHolePosition.add(rotatedXYZ);
        // Orbit Velocity
        entity.push(rotatedXYZ.x, rotatedXYZ.y, rotatedXYZ.z);

        // Gravity Velocity
//        double xGravity = -deltaPosition.x * 0.015 * xPercentage;
//        double yGravity = -deltaPosition.y * 0.015 * yPercentage;
//        double zGravity = -deltaPosition.z * 0.015 * zPercentage;
//        System.out.printf("%s, %s, %s\n", xGravity, yGravity, zGravity);
//        entity.push(xGravity, yGravity, zGravity);
        Vector3d gravityVector = entity.getPosition(0f).vectorTo(blackHolePosition).scale(0.015);
//        entity.push(gravityVector.x, gravityVector.y, gravityVector.z);

        if (entity instanceof LivingEntity)
        {
            entity.fallDistance = 0;
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

    public static Vector3d rotateVector(Vector3d vec, double angleDeg) {
        return rotateVector(vec, angleDeg, angleDeg, angleDeg);
    }

    public static Vector3d rotateVector(Vector3d vec, double rollDeg, double pitchDeg, double yawDeg) {

        // Apply pitch (X-axis rotation)
        double x1 = vec.x;
        double y1 = vec.y * Math.cos(pitchDeg) - vec.z * Math.sin(pitchDeg);
        double z1 = vec.y * Math.sin(pitchDeg) + vec.z * Math.cos(pitchDeg);

        // Apply yaw (Y-axis rotation)
        double x2 = x1 * Math.cos(yawDeg) + z1 * Math.sin(yawDeg);
        double y2 = y1;
        double z2 = -x1 * Math.sin(yawDeg) + z1 * Math.cos(yawDeg);

        // Apply roll (Z-axis rotation)
        double x3 = x2 * Math.cos(rollDeg) - y2 * Math.sin(rollDeg);
        double y3 = x2 * Math.sin(rollDeg) + y2 * Math.cos(rollDeg);
        double z3 = z2;

        return new Vector3d(x3, y3, z3);
    }

}
