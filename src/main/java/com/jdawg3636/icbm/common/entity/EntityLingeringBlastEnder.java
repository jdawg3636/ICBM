package com.jdawg3636.icbm.common.entity;

import com.google.common.collect.Lists;
import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.reg.ParticleTypeReg;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;

public class EntityLingeringBlastEnder extends EntityLingeringBlast {

    private int totalTicksAlive;

    public EntityLingeringBlastEnder(EntityType<?> entityType, World level) {
        super(entityType, level);
    }

    public EntityLingeringBlastEnder(EntityType<?> entityType, World level, int ticksAlive) {
        super(entityType, level, ticksAlive);
        this.totalTicksAlive = ticksAlive;
    }

    @Override
    public void tick() {
        if(!level.isClientSide()) {
            // Check Lifetime
            if(ticksRemaining <= 0) {
                kill();
                return;
            }
            // Particles
            double radius = ICBMReference.COMMON_CONFIG.getBlastRadiusEnder();// * (ticksRemaining / (double)totalTicksAlive);
            for (BlockPos blockPos : getAffectedBlockPositions(level, getX(), getY(), getZ(), radius, true)) {
                if (level.random.nextDouble() < 0.0012d) {
                    ICBMReference.sendParticlesOverrideLimiter((ServerWorld) level, (BasicParticleType) ParticleTypeReg.ENDER_EFFECT.get(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1, level.random.nextDouble() * 2, level.random.nextDouble() * 2, level.random.nextDouble() * 2, 0.1);
                }
                else if (level.random.nextDouble() < 0.005d) {
                    ICBMReference.sendParticlesOverrideLimiter((ServerWorld) level, ParticleTypes.DRAGON_BREATH, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1, level.random.nextDouble() * 2, level.random.nextDouble() * 2, level.random.nextDouble() * 2, 0.1);
                }
            }
            // Player Teleport
            for(LivingEntity livingEntity : this.level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().contract(0,1,0).inflate(radius))) {
                if(livingEntity.distanceToSqr(this) <= radius * radius && !livingEntity.isPassenger() && !livingEntity.isVehicle() && livingEntity.canChangeDimensions()) {
                    // Have to schedule teleportation as a task, otherwise the game with throw "Removing entity while ticking!"
                    // https://discord.com/channels/313125603924639766/725850371834118214/895991091919192075
                    ((ServerWorld)level).getServer().tell(new TickDelayedTask(0, () -> {
                        RegistryKey<World> destinationDimensionRegistryKey = livingEntity.level.dimension() == World.END ? World.OVERWORLD : World.END;
                        ServerWorld destinationDimension = ((ServerWorld)level).getServer().getLevel(destinationDimensionRegistryKey);
                        if (destinationDimension == null) return;
                        livingEntity.changeDimension(destinationDimension);
                    }));
                }
            }
            --ticksRemaining;
        }
    }

    public static List<BlockPos> getAffectedBlockPositions(World world, double x, double y, double z, double radius, boolean edgesOnly){
        List<BlockPos> affectedBlockPositions = Lists.newArrayList();
        int radius_int = (int) Math.ceil(radius);
        for (int dx = -radius_int; dx < radius_int + 1; dx++) {
            // fast calculate affected blocks
            int y_lim = (int) (Math.sqrt(radius_int*radius_int-dx*dx));
            for (int dy = -y_lim; dy < y_lim + 1; dy++) {
                int z_lim = (int) Math.sqrt(radius_int*radius_int-dx*dx-dy*dy);
                if(edgesOnly && dx != -radius_int && dx != radius_int) {
                    affectedBlockPositions.add(new BlockPos(x + dx, y + dy, z - z_lim));
                    affectedBlockPositions.add(new BlockPos(x + dx, y + dy, z + z_lim));
                }
                else {
                    for (int dz = -z_lim; dz < z_lim + 1; dz++) {
                        BlockPos blockPos = new BlockPos(x + dx, y + dy, z + dz);
                        affectedBlockPositions.add(blockPos);
                    }
                }
            }
        }
        return affectedBlockPositions;
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("total_ticks_alive", totalTicksAlive);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
        totalTicksAlive = nbt.getInt("total_ticks_alive");
    }

}
