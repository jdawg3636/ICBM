package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.particle_accelerator.BlockParticleAccelerator;
import com.jdawg3636.icbm.common.block.particle_accelerator.TileParticleAccelerator;
import com.jdawg3636.icbm.common.event.EventBlastAcceleratingParticle;
import com.jdawg3636.icbm.common.reg.EntityReg;
import com.jdawg3636.icbm.common.reg.ICBMTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.ArrayList;
import java.util.Optional;

import static com.jdawg3636.icbm.common.event.EventBlastAcceleratingParticle.ExplosionCause;

public class EntityAcceleratingParticle extends Entity {

    public static final DataParameter<Optional<BlockPos>> PARTICLE_ACCELERATOR_POSITION = EntityDataManager.defineId(EntityAcceleratingParticle.class, DataSerializers.OPTIONAL_BLOCK_POS);
    public static final DataParameter<Direction> PARTICLE_DIRECTION = EntityDataManager.defineId(EntityAcceleratingParticle.class, DataSerializers.DIRECTION);
    public static final DataParameter<Float> PARTICLE_SPEED = EntityDataManager.defineId(EntityAcceleratingParticle.class, DataSerializers.FLOAT);

    public boolean hasCollided = false;

    public static EntityAcceleratingParticle getNewInstanceForAccelerator(TileParticleAccelerator particleAccelerator) {
        World level = particleAccelerator.getLevel();
        assert level != null;
        EntityAcceleratingParticle particleEntity = EntityReg.ACCELERATING_PARTICLE.get().create(level);
        if(particleEntity != null) {
            particleEntity.getEntityData().set(PARTICLE_ACCELERATOR_POSITION, Optional.of(particleAccelerator.getBlockPos()));
            particleEntity.getEntityData().set(PARTICLE_DIRECTION, particleAccelerator.getBlockState().getValue(BlockParticleAccelerator.FACING).getOpposite());
            BlockPos particlePos = particleEntity.getEntityData().get(PARTICLE_ACCELERATOR_POSITION).orElse(BlockPos.ZERO).relative(particleEntity.getEntityData().get(PARTICLE_DIRECTION));
            particleEntity.setPos(particlePos.getX()+0.5, particlePos.getY(), particlePos.getZ()+0.5);
        }
        return particleEntity;
    }

    public static boolean isMagneticallySealed(IWorldReader level, BlockPos blockPos, int depth) {
        // Depth Cap
        if(depth == 0) return false;
        // Count Air Directions, return false if non-air neighbors aren't magnetic
        ArrayList<Direction> airDirections = new ArrayList<>(6);
        for(Direction direction : Direction.values()) {
            BlockState neighborBlockState = level.getBlockState(blockPos.relative(direction));
            if(neighborBlockState.isAir()) {
                airDirections.add(direction);
            }
            else if (!neighborBlockState.is(ICBMTags.Blocks.MAGNETIC)) {
                return false;
            }
        }
        // If air directions >2 then can't guarantee sealed, need to recurse.
        if (airDirections.size() > 2) {
            for (Direction airDirection : airDirections) {
                if (!isMagneticallySealed(level, blockPos.relative(airDirection), depth - 1)) {
                    return false;
                }
            }
        }
        // Else (air directions >=2), consider sealed.
        return true;
    }

    public static Direction getDirectionToGo(IWorldReader level, BlockPos blockPos, Direction bannedDirection) {
//        ICBMReference.broadcastToChat(level, "BlockPos = %s, Banned Direction = %s", blockPos, bannedDirection);
        for(Direction direction : Direction.values()) {
            if(direction == bannedDirection) continue;
            if(level.getBlockState(blockPos.relative(direction)).isAir()) {
//                ICBMReference.broadcastToChat(level, "Result = %s", direction);
                return direction;
            }
        }
//        ICBMReference.broadcastToChat(level, "Result = null");
        return null;
    }

    public EntityAcceleratingParticle(EntityType<?> entityType, World level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        if(!level.isClientSide() && isAlive()) {
            // If no accelerator is assigned, do nothing.
            if (!getEntityData().get(PARTICLE_ACCELERATOR_POSITION).isPresent()) {
                return;
            }
            // Kill particle if an accelerator is assigned but no longer exists
            TileEntity tileEntity = level.getBlockEntity(getEntityData().get(PARTICLE_ACCELERATOR_POSITION).orElse(BlockPos.ZERO));
            if (!(tileEntity instanceof TileParticleAccelerator)) {
                kill();
                return;
            }
            // Kill particle if assigned accelerator is inactive
            TileParticleAccelerator particleAccelerator = (TileParticleAccelerator) tileEntity;
            if (!particleAccelerator.acceleratorIsActive) {
                kill();
                return;
            }
            // Explode particle if position is not magnetically sealed
            if (!isMagneticallySealed(level, blockPosition(), 3)) {
                explode(ExplosionCause.ELECTROMAGNETICALLY_UNSEALED);
                return;
            }
            // Explode particle if collided with another entity
            final EntityRayTraceResult entityRayTraceResult = findHitEntity();
            if(entityRayTraceResult != null) {
                if(entityRayTraceResult.getType() == RayTraceResult.Type.ENTITY) {
                    explode(ExplosionCause.COLLISION_WITH_ENTITY);
                    if (entityRayTraceResult.getEntity() instanceof EntityAcceleratingParticle) {
                        ((EntityAcceleratingParticle)entityRayTraceResult.getEntity()).explode(ExplosionCause.DESTROYED_BY_OTHER_PARTICLE, this);
                    }
                }
            }
            // Explode particle if maximum speed reached
            if (getEntityData().get(PARTICLE_SPEED) >= ICBMReference.COMMON_CONFIG.getParticleAcceleratorSpeedRequiredToGenerateAntimatter()) {
                explode(ExplosionCause.MAXIMUM_SPEED);
                return;
            }
        }
        // Update Physics (Runs on both client and server)
        if (hasCollided) {
            float previousSpeed = getEntityData().get(PARTICLE_SPEED);
            getEntityData().set(PARTICLE_SPEED, (float)Math.max(0, getEntityData().get(PARTICLE_SPEED) * (1 - ICBMReference.COMMON_CONFIG.getParticleAcceleratorSpeedPenaltyForCollision())));
//            ICBMReference.broadcastToChat(level, "Imparting speed penalty for collision, '%s' -> '%s'", previousSpeed, getEntityData().get(PARTICLE_SPEED));
            Direction newParticleDirection = getDirectionToGo(level, blockPosition(), getEntityData().get(PARTICLE_DIRECTION).getOpposite());
            if (newParticleDirection != null) getEntityData().set(PARTICLE_DIRECTION, newParticleDirection);
            else explode(ExplosionCause.COLLISION_WITH_BLOCK);
            hasCollided = false;
        } else {
            getEntityData().set(PARTICLE_SPEED, getEntityData().get(PARTICLE_SPEED) + (float)ICBMReference.COMMON_CONFIG.getParticleAcceleratorSpeedIncreasePerTick());
            Vector3i particleDirectionAsVecI = getEntityData().get(PARTICLE_DIRECTION).getNormal();
            Vector3d particleDirectionAsVecD = new Vector3d(particleDirectionAsVecI.getX(), particleDirectionAsVecI.getY(), particleDirectionAsVecI.getZ());
            moveRelative(getEntityData().get(PARTICLE_SPEED), particleDirectionAsVecD);
            this.move(MoverType.SELF, this.getDeltaMovement());
        }
    }

    public void explode(ExplosionCause explosionCause) {
        explode(explosionCause, null);
    }

    public void explode(ExplosionCause explosionCause, EntityAcceleratingParticle otherParticle) {
        if(!level.isClientSide() && level instanceof ServerWorld) {
            TileParticleAccelerator particleAccelerator = (TileParticleAccelerator)level.getBlockEntity(getEntityData().get(PARTICLE_ACCELERATOR_POSITION).orElse(BlockPos.ZERO));
            if(particleAccelerator != null) EventBlastAcceleratingParticle.fire(blockPosition(), (ServerWorld)level, explosionCause, this.getEntityData().get(PARTICLE_SPEED), otherParticle == null ? Optional.empty() : Optional.of(otherParticle.getEntityData().get(PARTICLE_SPEED)), particleAccelerator);
            kill();
        }
    }

    protected EntityRayTraceResult findHitEntity() {
        return ProjectileHelper.getEntityHitResult(this.level, this, position(), position().add(getDeltaMovement()), this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), (otherEntity) -> !otherEntity.isSpectator());
    }

    @Override
    public Vector3d collide(Vector3d motionVector) {
        Vector3d result = super.collide(motionVector);
        if(!result.equals(motionVector)) {
//            ICBMReference.broadcastToChat(level, "Experienced Collision! Result = %s, Target = %s", result, motionVector);
            hasCollided = true;
        }
        return result;
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(PARTICLE_ACCELERATOR_POSITION, Optional.of(BlockPos.ZERO));
        entityData.define(PARTICLE_DIRECTION, Direction.DOWN);
        entityData.define(PARTICLE_SPEED, 0F);
    }

    @Override
    public void onSyncedDataUpdated(DataParameter<?> dataParameter) {
//        if(PARTICLE_ACCELERATOR_POSITION.equals(dataParameter)) this.particleAcceleratorPosition = (BlockPos) this.getEntityData().get(dataParameter);
//        if(PARTICLE_DIRECTION.equals(dataParameter)) this.particleDirection = (Direction) this.getEntityData().get(dataParameter);
//        if(PARTICLE_SPEED.equals(dataParameter)) this.particleSpeed = (float) this.getEntityData().get(dataParameter);
        super.onSyncedDataUpdated(dataParameter);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT nbt) {
        if(nbt.contains("particle_accelerator_position")) {
            CompoundNBT nbtParticleAcceleratorPosition = nbt.getCompound("particle_accelerator_position");
            getEntityData().set(PARTICLE_ACCELERATOR_POSITION, Optional.of(new BlockPos(
                    nbtParticleAcceleratorPosition.getInt("x"),
                    nbtParticleAcceleratorPosition.getInt("y"),
                    nbtParticleAcceleratorPosition.getInt("z")
            )));
        }
        if(nbt.contains("particle_direction")) {
            getEntityData().set(PARTICLE_DIRECTION, Direction.from3DDataValue(nbt.getInt("particle_direction")));
        }
        if(nbt.contains("has_collided")) {
            hasCollided = nbt.getBoolean("has_collided");
        }
        if(nbt.contains("particle_speed")) {
            getEntityData().set(PARTICLE_SPEED, nbt.getFloat("particle_speed"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt) {
        if(getEntityData().get(PARTICLE_ACCELERATOR_POSITION).isPresent()) {
            BlockPos particleAcceleratorPosition = getEntityData().get(PARTICLE_ACCELERATOR_POSITION).orElse(BlockPos.ZERO);
            CompoundNBT nbtParticleAcceleratorPosition = new CompoundNBT();
            nbtParticleAcceleratorPosition.putInt("x", particleAcceleratorPosition.getX());
            nbtParticleAcceleratorPosition.putInt("y", particleAcceleratorPosition.getY());
            nbtParticleAcceleratorPosition.putInt("z", particleAcceleratorPosition.getZ());
            nbt.put("particle_accelerator_position", nbtParticleAcceleratorPosition);
        }
        nbt.putInt("particle_direction", getEntityData().get(PARTICLE_DIRECTION).get3DDataValue());
        nbt.putBoolean("has_collided", hasCollided);
        nbt.putFloat("particle_speed", getEntityData().get(PARTICLE_SPEED));
    }

    @Override
    public boolean isSilent() {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        return true;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
