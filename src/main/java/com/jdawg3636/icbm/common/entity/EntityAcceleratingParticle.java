package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.particle_accelerator.BlockParticleAccelerator;
import com.jdawg3636.icbm.common.block.particle_accelerator.TileParticleAccelerator;
import com.jdawg3636.icbm.common.event.ICBMBlastEventUtil;
import com.jdawg3636.icbm.common.reg.EntityReg;
import com.jdawg3636.icbm.common.reg.ICBMTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.ArrayList;

public class EntityAcceleratingParticle extends Entity {

    public BlockPos particleAcceleratorPosition = null;
    public Direction particleDirection = null;
    public boolean hasCollided = false;
    public float particleSpeed = 0F;

    public static EntityAcceleratingParticle getNewInstanceForAccelerator(TileParticleAccelerator particleAccelerator) {
        World level = particleAccelerator.getLevel();
        assert level != null;
        EntityAcceleratingParticle particleEntity = EntityReg.ACCELERATING_PARTICLE.get().create(level);
        if(particleEntity != null) {
            particleEntity.particleAcceleratorPosition = particleAccelerator.getBlockPos();
            particleEntity.particleDirection = particleAccelerator.getBlockState().getValue(BlockParticleAccelerator.FACING).getOpposite();
            BlockPos particlePos = particleAccelerator.getBlockPos().relative(particleEntity.particleDirection);
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
        ICBMReference.broadcastToChat(level, "BlockPos = %s, Banned Direction = %s", blockPos, bannedDirection);
        for(Direction direction : Direction.values()) {
            if(direction == bannedDirection) continue;
            if(level.getBlockState(blockPos.relative(direction)).isAir()) {
                ICBMReference.broadcastToChat(level, "Result = %s", direction);
                return direction;
            }
        }
        ICBMReference.broadcastToChat(level, "Result = null");
        return null;
    }

    public EntityAcceleratingParticle(EntityType<?> entityType, World level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();
        if(!level.isClientSide()) {
            // If no accelerator is assigned, do nothing.
            if (particleAcceleratorPosition == null) {
                return;
            }
            // Kill particle if an accelerator is assigned but no longer exists
            TileEntity tileEntity = level.getBlockEntity(particleAcceleratorPosition);
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
                explode();
                return;
            }
            // Update Physics
            if (hasCollided) {
                particleSpeed = (float)Math.max(0, particleSpeed - ICBMReference.COMMON_CONFIG.getParticleAcceleratorSpeedPenaltyForCollision());
                Direction newParticleDirection = getDirectionToGo(level, blockPosition(), particleDirection.getOpposite());
                if (newParticleDirection != null) particleDirection = newParticleDirection;
                else explode();
                hasCollided = false;
            } else {
                particleSpeed += ICBMReference.COMMON_CONFIG.getParticleAcceleratorSpeedIncreasePerTick();
                Vector3i particleDirectionAsVecI = particleDirection.getNormal();
                Vector3d particleDirectionAsVecD = new Vector3d(particleDirectionAsVecI.getX(), particleDirectionAsVecI.getY(), particleDirectionAsVecI.getZ());
                particleDirectionAsVecD = particleDirectionAsVecD.scale(particleSpeed);
                moveRelative(particleSpeed, particleDirectionAsVecD);
                //setDeltaMovement(getDeltaMovement().add(particleDirectionAsVecD));
                //setPos(getX() + particleDirectionAsVecD.x, getY() + particleDirectionAsVecD.y, getZ() + particleDirectionAsVecD.z);
                //this.move(MoverType.SELF, this.getDeltaMovement());
            }
        }
    }

    public void explode() {
        if(level.isClientSide()) return;
        // TODO: Find nearby particles to determine if red matter should be generated
        if(level instanceof ServerWorld) {
            // TODO: *Maybe* add an event for this blast? Seems a bit excessive but may be useful for someone.
            ICBMBlastEventUtil.doVanillaExplosion((ServerWorld) level, blockPosition(), 1F);
            // TODO: If red matter wasn't generated, then generate antimatter
            kill();
        }
    }

    @Override
    public Vector3d collide(Vector3d motionVector) {
        Vector3d result = super.collide(motionVector);
        if(!result.equals(motionVector)) {
            ICBMReference.broadcastToChat(level, "Experienced Collision! Result = %s, Target = %s", result, motionVector);
            hasCollided = true;
        }
        return result;
    }

    @Override
    protected void defineSynchedData() {
        // todo: sync acceleration progress to display in GUI
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT nbt) {
        if(nbt.contains("particle_accelerator_position")) {
            CompoundNBT nbtParticleAcceleratorPosition = nbt.getCompound("particle_accelerator_position");
            particleAcceleratorPosition = new BlockPos(
                    nbtParticleAcceleratorPosition.getInt("x"),
                    nbtParticleAcceleratorPosition.getInt("y"),
                    nbtParticleAcceleratorPosition.getInt("z")
            );
        }
        if(nbt.contains("particle_direction")) {
            particleDirection = Direction.from3DDataValue(nbt.getInt("particle_direction"));
        }
        if(nbt.contains("has_collided")) {
            hasCollided = nbt.getBoolean("has_collided");
        }
        if(nbt.contains("particle_speed")) {
            particleSpeed = nbt.getFloat("particle_speed");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt) {
        if(particleAcceleratorPosition != null) {
            CompoundNBT nbtParticleAcceleratorPosition = new CompoundNBT();
            nbtParticleAcceleratorPosition.putInt("x", particleAcceleratorPosition.getX());
            nbtParticleAcceleratorPosition.putInt("y", particleAcceleratorPosition.getY());
            nbtParticleAcceleratorPosition.putInt("z", particleAcceleratorPosition.getZ());
            nbt.put("particle_accelerator_position", nbtParticleAcceleratorPosition);
        }
        if(particleDirection != null) {
            nbt.putInt("particle_direction", particleDirection.get3DDataValue());
        }
        nbt.putBoolean("has_collided", hasCollided);
        nbt.putFloat("particle_speed", particleSpeed);
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
