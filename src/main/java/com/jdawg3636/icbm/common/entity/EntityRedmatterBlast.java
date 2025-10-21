package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.event.ICBMBlastEventUtil;
import com.jdawg3636.icbm.common.reg.EntityReg;
import com.jdawg3636.icbm.common.reg.SoundEventReg;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.*;
import java.util.stream.Collectors;

public class EntityRedmatterBlast extends EntityLingeringBlast {

    private double animationPercent = 0;
    private final Queue<BlockPos> blocksToDestroy = new ArrayDeque<>();
    private static final int maxBlocksDestroyedPerTick = 50; // TODO make configurable
    private static final int entityMovementTickFrequency = 1;

    public EntityRedmatterBlast(EntityType<?> entityType, World level) {
        super(entityType, level, 20 * 20); // todo: make lifetime configurable
    }

    @Override
    public void tick() {
        if(level != null) {

            // Entity Movement (Client + Server)
            if(tickCount % entityMovementTickFrequency == 0) {
                for (Entity entity : this.level.getEntitiesOfClass(Entity.class, new AxisAlignedBB(blockPosition()).inflate(ICBMReference.COMMON_CONFIG.getBlastRadiusRedmatter()))) {
                    if (!(entity instanceof EntityRedmatterBlast) && !(entity instanceof ItemEntity) && !(ICBMReference.entityIsAPlayerInCreativeOrSpectatorMode(entity))) {
                        applyBlackHoleMovementToEntity(entity);
                    }
                }
            }

            // Client-Side Only
            if(level.isClientSide()) {
                // Visual Animation
                addAnimationPercent(0.25D);
            }

            // Server-Side Only
            else if (level instanceof ServerWorld) {
                ServerWorld serverLevel = (ServerWorld) level;

                // Check Lifetime
                if(ticksRemaining <= 0) {
                    kill();
                    return;
                }

                final int soundRandInt = this.level.random.nextInt() % 8;
                // Block Crumble Sound
                if (soundRandInt == 0) {
                    this.level.playSound((PlayerEntity) null, this.getX() + 0.5, this.getY() + 1.5, this.getZ() + 0.5, SoundEventReg.EFFECT_BLOCK_CRUMBLE.get(), SoundCategory.BLOCKS, 50F, 1.0F);
                }
                // Explosion Sound
                if (soundRandInt == 1) {
                    ICBMBlastEventUtil.doVanillaExplosionParticlesAndSound((ServerWorld) level, this.getX() + 0.5, this.getY() + 0.5, this.getZ() + 0.5, 5F);
                }
                // Effect Sound
                this.level.playSound((PlayerEntity) null, this.getX() + 0.5, this.getY() + 1.5, this.getZ() + 0.5, SoundEventReg.EFFECT_REDMATTER.get(), SoundCategory.BLOCKS, 2.5F, 1.0F);

                // Only perform calculation logic once/second
                if(tickCount % 20 == 0) {
                    // Keep track of this for later
                    boolean unableToFindEnoughBlocks = false;
                    // If we're out of blocks to destroy, try to find more.
                    if(blocksToDestroy.isEmpty()) {
                        // Assume failure condition - will flip this back if we find enough
                        unableToFindEnoughBlocks = true;
                        // Config values
                        final double maxRadius = ICBMReference.COMMON_CONFIG.getBlastRadiusRedmatter();
                        final double fuzzinessPercentage = ICBMReference.COMMON_CONFIG.getAntimatterFuzzinessPercentage(); // todo make configurable separate from antimatter
                        final boolean canDestroyIndestructible = ICBMReference.COMMON_CONFIG.getAntimatterCanDestroyBedrock(); // todo make configurable separate from antimatter
                        // Calculate current blocks that are candidates for deletion this tick. Radius is increased as-necessary to give illusion of blocks from the center being affected first
                        List<BlockPos> candidates = Collections.emptyList();
                        for (int currentRadius = 8; currentRadius < maxRadius; ++currentRadius) {
                            // Reset RNG each iteration so that repeat passes have deterministic noise patterns
                            Random currentRandom = new Random(serverLevel.getSeed());
                            // Use antimatter algorithm to calculate a sphere of blocks
                            candidates = ICBMBlastEventUtil.getBlockPositionsWithinFuzzySphere(getX(), getY(), getZ(), currentRadius, currentRandom, fuzzinessPercentage).stream()
                                    .filter(pos -> !level.getBlockState(pos).isAir(level, pos))
                                    .filter(pos -> canDestroyIndestructible || level.getBlockState(pos).getBlock().explosionResistance < 3_600_000F)
                                    .filter(pos -> level.getFluidState(pos).getType() == Fluids.EMPTY || level.getFluidState(pos).isSource())
                                    .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                                        // Scramble first
                                        Collections.shuffle(list, currentRandom);
                                        // Sort fluid source blocks to mitigate their self-regeneration
                                        list.sort((aPos, bPos) -> {
                                            final boolean aIsFluidSource = level.getFluidState(aPos).isSource();
                                            final boolean bIsFluidSource = level.getFluidState(bPos).isSource();
                                            // Handle cases where one or both of the blocks are not fluid sources
                                            if(aIsFluidSource && !bIsFluidSource) {
                                                return -1;
                                            }
                                            if(!aIsFluidSource && !bIsFluidSource) {
                                                return 0;
                                            }
                                            if(!aIsFluidSource && bIsFluidSource) {
                                                return 1;
                                            }
                                            // If both blocks are fluid sources, sort by y-axis and break tie with other axes.
                                            return bPos.compareTo(aPos);
                                        });
                                        return list;
                                    }));
                            // If candidates are sufficient to satiate, break out of loop.
                            if (candidates.size() >= maxBlocksDestroyedPerTick && candidates.stream().anyMatch(pos -> level.getFluidState(pos).getType() == Fluids.EMPTY)) {
                                unableToFindEnoughBlocks = false;
                                break;
                            }
                        }
                        // Transfer local list into field queue
                        blocksToDestroy.addAll(candidates);
                    }
                    // If we're up to maximum radius and still not able to keep up with our desired destruction rate, then start depleting ticks remaining.
                    if(unableToFindEnoughBlocks) {
                        ticksRemaining -= 20;
                    }
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
        if(!entity.level.isClientSide() && entityDistanceToBlackHole < 6.0) {
            if(entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                livingEntity.hurt(DamageSource.explosion((Explosion) null /* todo - chain of blame */), 20F);
            }
            else {
                entity.kill();
            }
        }

        // Reduce cube to sphere
        double closenessPercentage = 1.0 - (entityDistanceToBlackHole / radius);
        if(closenessPercentage < 0.0) {
            return;
        }

        // Orbit Velocity
        final Vector3d blackHoleToEntity = entityPosition.subtract(blackHolePosition);
        final Vector3d blackHoleToTarget = rotateVector(blackHoleToEntity, -1d/20d * entityMovementTickFrequency);
        final Vector3d entityToTarget = blackHoleToTarget.subtract(blackHoleToEntity);

        // Gravity Velocity
        final Vector3d gravityVector = entity.getPosition(0f).vectorTo(blackHolePosition).normalize().scale(0.25 * entityMovementTickFrequency);

        // Apply Velocities to Entity (emulating Entity::push, but with capability to set instead of add).
        entity.setDeltaMovement(entityToTarget.add(gravityVector));
        entity.hasImpulse = true;

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
        return rotateVector(vec, 0.0, 0.0, angleDeg);
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
