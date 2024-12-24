package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.event.AbstractBlastEvent;
import com.jdawg3636.icbm.common.reg.EffectReg;
import com.jdawg3636.icbm.common.reg.ParticleTypeReg;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class EntityLingeringBlastContagion extends EntityLingeringBlast {

    public EntityLingeringBlastContagion(EntityType<?> entityType, World level) {
        super(entityType, level);
    }

    public EntityLingeringBlastContagion(EntityType<?> entityType, World level, int ticksAlive) {
        super(entityType, level, ticksAlive);
    }

    @Override
    public void tick() {
        if(!level.isClientSide()) {
            // Check Lifetime
            if(ticksRemaining <= 0) {
                kill();
                return;
            }
            // Calculate radius
            double radius = ICBMReference.COMMON_CONFIG.getBlastRadiusContagion();
            if(blastType == AbstractBlastEvent.Type.GRENADE) radius *= 2.0/3.0;
            int radiusInt = (int)radius;
            // Particles
            for(int i = -radiusInt; i <= radiusInt; i+=3) {
                for(int j = -radiusInt; j <= radiusInt; j+=3) {
                    for(int k = -radiusInt; k <= radiusInt; k+=3) {
                        if(level.random.nextFloat() < 0.3F) {
                            ((ServerWorld)level).sendParticles(
                                    getParticleType(level.random.nextDouble()),
                                    getX() + i + level.random.nextDouble(),
                                    getY() + j + level.random.nextDouble(),
                                    getZ() + k + level.random.nextDouble(),
                                    1,
                                    level.random.nextDouble() * 2,
                                    level.random.nextDouble() * 2,
                                    level.random.nextDouble() * 2,
                                    0.0D
                            );
                        }
                    }
                }
            }
            // Player Effects
            for(LivingEntity livingEntity : this.level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().contract(0,1,0).inflate(radius))) {
                if(livingEntity.isAffectedByPotions() && !ICBMReference.entityIsAPlayerInCreativeOrSpectatorMode(livingEntity)) {
                    livingEntity.addEffect(new EffectInstance(EffectReg.ENGINEERED_PATHOGEN.get(), 45 * 20, 0));
                }
            }
            --ticksRemaining;
        }
    }

    public BasicParticleType getParticleType(double seed) {
        ParticleType<BasicParticleType> particle;
        switch((int)(5*seed) % 5) {
            case 0:
                particle = ParticleTypeReg.SMOKE_CONTAGION_A.get();
                break;
            case 1:
                particle = ParticleTypeReg.SMOKE_CONTAGION_B.get();
                break;
            case 2:
                particle = ParticleTypeReg.SMOKE_CONTAGION_C.get();
                break;
            case 3:
                particle = ParticleTypeReg.SMOKE_CONTAGION_D.get();
                break;
            default: // case 4
                particle = ParticleTypeReg.SMOKE_CONTAGION_E.get();
        }
        return (BasicParticleType)particle;
    }

}
