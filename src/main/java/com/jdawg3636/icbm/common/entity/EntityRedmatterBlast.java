package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.event.ICBMBlastEventUtil;
import com.jdawg3636.icbm.common.reg.EntityReg;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.fluid.Fluids;
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
    private final int maxBlocksDestroyedPerTick = 50; // TODO make configurable

    public EntityRedmatterBlast(EntityType<?> entityType, World level) {
        super(entityType, level, 20 * 20); // todo: make lifetime configurable
    }

    @Override
    public void tick() {
        if(level != null) {
            // Entity movement
            if(ticksRemaining % 5 == 0) {
                for (Entity entity : this.level.getEntitiesOfClass(Entity.class, new AxisAlignedBB(blockPosition()).inflate(ICBMReference.COMMON_CONFIG.getBlastRadiusRedmatter()))) {
                    if (!(entity instanceof EntityRedmatterBlast) && !(entity instanceof ItemEntity) && !(ICBMReference.entityIsAPlayerInCreativeOrSpectatorMode(entity))) {
                        applyBlackHoleMovementToEntity(entity);
                    }
                }
            }
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
                                    .filter(pos -> level.getFluidState(pos).getType() == Fluids.EMPTY || level.getFluidState(pos).isSource())
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
                    // If we're up to maximum radius and still not able to keep up with our desired destruction rate, then start depleting ticks remaining.
                    if(blocksToDestroy.size() < maxBlocksDestroyedPerTick) {
                        ticksRemaining -= 20;
                    }
                    // Explosion Sound/Particles
                    ICBMBlastEventUtil.doVanillaExplosionParticlesAndSound((ServerWorld) level, position().x, position().y, position().z);
                }
                // Break Blocks
                for(int i = 0; i < maxBlocksDestroyedPerTick; ++i) {
                    if(!blocksToDestroy.isEmpty()) {
                        BlockPos blockPos = blocksToDestroy.remove();
                        if(random.nextDouble() < 0.01 && EntityFancyFallingBlock.getBlockSafeToConvertToFallingEntity(level, blockPos)) {
                            EntityFancyFallingBlock fancyFallingBlockEntity = EntityReg.FANCY_FALLING_BLOCK.get().create(level);
                            if (fancyFallingBlockEntity != null) {
                                fancyFallingBlockEntity.setDeltaMovement(level.random.nextDouble() - 0.5, level.random.nextDouble() - 0.5, level.random.nextDouble() - 0.5);
                                fancyFallingBlockEntity.addEntityToLevel(blockPos, level.getBlockState(blockPos), -0.04f, 60 * 20);
                            }
                        }
                        level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
    }

    public void applyBlackHoleMovementToEntity(Entity entity) {

        // Get Constants
        final double radius = ICBMReference.COMMON_CONFIG.getBlastRadiusRedmatter();
        final Vector3d entityPosition = entity.getPosition(0f);
        final Vector3d blackHolePosition = this.getPosition(0f);

        // Kill Entity if Too Close
        double entityDistanceToBlackHole = entityPosition.distanceTo(blackHolePosition);
//            if(!entity.level.isClientSide() && entityDistanceToBlackHole < 6.0) {
//                entity.kill();
//                return;
//            }

        // Reduce cube to sphere
        double closenessPercentage = 1.0 - (entityDistanceToBlackHole / radius);
        if(closenessPercentage < 0.0) {
            return;
        }

        // Orbit Velocity
        Vector3d blackHoleToEntity = entityPosition.subtract(blackHolePosition);
        Vector3d blackHoleToEntityRotated = rotateVector(blackHoleToEntity, 0.000244140625); // 2^(-12)
        Vector3d entityToTarget = blackHoleToEntityRotated.subtract(blackHoleToEntity);
        entity.push(entityToTarget.x, entityToTarget.y, entityToTarget.z);

        // Gravity Velocity
        Vector3d gravityVector = entity.getPosition(0f).vectorTo(blackHolePosition).normalize().scale(0.125); // 2^(-4)
        entity.push(gravityVector.x, gravityVector.y, gravityVector.z);

        // Prevent Fall Damage
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
        return rotateVector(vec, 0.0, angleDeg, angleDeg);
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
