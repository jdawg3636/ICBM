package com.jdawg3636.icbm.common.entity;

import com.google.common.collect.Lists;
import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.reg.EntityReg;
import com.jdawg3636.icbm.common.reg.SoundEventReg;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.World;

import java.util.List;

public class EntityBlastAntigravitational extends EntityBlastLingering {

    public double radius = 1;

    public EntityBlastAntigravitational(EntityType<?> entityType, World level) {
        super(entityType, level);
    }

    public void addEntityToLevel(double radius) {
        this.radius = radius;
        this.level.addFreshEntity(this);
    }

    @Override
    public void tick() {

        // Manage Lifetime
        if(ticksRemaining <= 0) {
            kill();
            return;
        }
        --ticksRemaining;

        // Entity/Player Effects
        for(LivingEntity livingEntity : this.level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().contract(0,1,0).inflate(radius, radius / 5.0, radius))) {
            if(livingEntity.isAffectedByPotions() && !ICBMReference.entityIsAPlayerInCreativeOrSpectatorMode(livingEntity)) {
                livingEntity.addEffect(new EffectInstance(Effects.LEVITATION, 21, 1, false, false));
            }
        }

        // Rate Limit for All Remaining Effects
        if(ticksRemaining % 20 != 0) {
            return;
        }
        // Ambient Sound
        if (!this.level.isClientSide() && this.isAlive()) {
            SoundEvent soundEvent = SoundEventReg.EXPLOSION_ANTIGRAVITATIONAL.get();
            float soundRadiusInChunks = (float)this.radius * 1.5f / 16.0f;
            this.level.playSound((PlayerEntity) null, getX() + 0.5, getY() + 0.5, getZ() + 0.5, soundEvent, SoundCategory.BLOCKS, soundRadiusInChunks, 1.0F);
        }
        // Block Effects
        for(BlockPos blockPos : getAntigravityEligiblePositionsWithinSphere(level, getX(), getY(), getZ(), radius)) {
            // Valid Block + Random chance to affect
            if(EntityFancyFallingBlock.getBlockSafeToConvertToFallingEntity(level, blockPos) && level.getBlockState(blockPos).getBlock().getExplosionResistance() <= 9.0f && level.getRandom().nextDouble() < 0.01) {
                // Affect Current Target
                EntityFancyFallingBlock fancyFallingBlockEntity = EntityReg.FANCY_FALLING_BLOCK.get().create(level);
                if (fancyFallingBlockEntity != null) {
                    fancyFallingBlockEntity.addEntityToLevel(blockPos, level.getBlockState(blockPos), 0.02f, ticksRemaining + (random.nextInt() % 20));
                }
                level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putDouble("Radius", radius);
    }

    public static List<BlockPos> getAntigravityEligiblePositionsWithinSphere(World level, double x, double y, double z, double radius) {
        // Create list to return
        List<BlockPos> affectedBlockPositions = Lists.newArrayList();
        // Round radius up to next integer for the algorithm to work
        int radius_int = (int) Math.ceil(radius);
        // Iterate over all x values in radius
        for (int dx = -radius_int; dx < radius_int + 1; dx++) {
            // Use pythagorean theorem to iterate over only the potentially-valid y values for this x-constrained plane.
            int y_lim = (int) (Math.sqrt(radius_int*radius_int-dx*dx));
            // Reduce effective radius on y-axis
            y_lim = (int) Math.ceil(y_lim / 5.0);
            for (int dy = -y_lim; dy < y_lim + 1; dy++) {
                // Use pythagorean theorem to iterate over only the valid z values within the xy-constrained line.
                int z_lim = (int) Math.sqrt(radius_int*radius_int-dx*dx-dy*dy);
                for (int dz = -z_lim; dz < z_lim + 1; dz++) {
                    BlockPos blockPos = new BlockPos(x + dx, y + dy, z + dz);
                    // Filter to antigravity eligible
                    if(!level.getBlockState(blockPos).isAir() && level.getBlockState(blockPos.above()).getCollisionShape(level, blockPos).equals(VoxelShapes.empty())) {
                        affectedBlockPositions.add(blockPos);
                    }
                }
            }
        }
        // Return
        return affectedBlockPositions;
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
        radius = nbt.getDouble("Radius");
    }

}
