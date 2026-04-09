package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.reg.SoundEventReg;
import com.jdawg3636.icbm.mixin.accessors.IAbstractBlockAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.server.ServerWorld;

public class EventBlastAntimatter extends AbstractBlastEvent {

    public EventBlastAntimatter(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType, Direction blastDirection) {
        super(blastPosition, blastWorld, blastType, blastDirection, SoundEventReg.EXPLOSION_ANTIMATTER, 8.0F);
    }

    @Override
    public boolean executeBlast() {

        ICBMBlastEventUtil.doBlastSoundAndParticles(this);

        final ServerWorld level = getBlastWorld();
        final double radius = ICBMReference.COMMON_CONFIG.getBlastRadiusAntimatter();
        final double fuzzinessPercentage = ICBMReference.COMMON_CONFIG.getAntimatterFuzzinessPercentage();
        final boolean canDestroyIndestructible = ICBMReference.COMMON_CONFIG.getAntimatterCanDestroyBedrock();

        // Block Effects
        for(BlockPos pos : ICBMBlastEventUtil.getBlockPositionsWithinFuzzySphere(getBlastPosition().getX() + 0.5, getBlastPosition().getY() + 0.5, getBlastPosition().getZ() + 0.5, radius, level.random, fuzzinessPercentage)) {
            BlockState blockState = level.getBlockState(pos);
            if(!blockState.isAir(level, pos) && (canDestroyIndestructible || ((IAbstractBlockAccessor)blockState.getBlock()).getExplosionResistance() < 3_600_000F)) {
                level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            }
        }

        // Entity/Player Effects
        for(Entity targetEntity : level.getEntitiesOfClass(Entity.class, new AxisAlignedBB(getBlastPosition()).contract(0,1,0).inflate(radius, radius / 5.0, radius))) {
            // Refine Cube to Sphere
            double entityDistanceToEpicenter = targetEntity.position().distanceTo(new Vector3d(getBlastPosition().getX() + 0.5, getBlastPosition().getY() + 0.5, getBlastPosition().getZ() + 0.5));
            if(!targetEntity.level.isClientSide() && entityDistanceToEpicenter < radius) {
                // If living, deal explosion damage
                if(targetEntity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) targetEntity;
                    livingEntity.hurt(DamageSource.explosion((Explosion) null /* todo - chain of blame */), Float.MAX_VALUE);
                }
                // Otherwise, just kill
                else {
                    targetEntity.kill();
                }
            }
        }

        return true;

    }

}
