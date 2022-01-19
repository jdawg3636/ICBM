package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.common.event.AbstractBlastEvent;
import com.jdawg3636.icbm.common.reg.ParticleTypeReg;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class EntityLingeringBlastDebilitation extends EntityLingeringBlast {

    public EntityLingeringBlastDebilitation(EntityType<?> entityType, World level) {
        super(entityType, level);
    }

    public EntityLingeringBlastDebilitation(EntityType<?> entityType, World level, int ticksAlive) {
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
            // Particles
            int radius = blastType == AbstractBlastEvent.Type.GRENADE ? 6 : 9;
            for(int i = -radius; i <= radius; i+=3) {
                for(int j = -radius; j <= radius; j+=3) {
                    for(int k = -radius; k <= radius; k+=3) {
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
                if(livingEntity.isAffectedByPotions()) {
                    livingEntity.addEffect(new EffectInstance(Effects.CONFUSION,         18 * 20, 0));
                    livingEntity.addEffect(new EffectInstance(Effects.DIG_SLOWDOWN,      60 * 20, 0));
                    livingEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 60 * 20, 2));
                }
            }
            --ticksRemaining;
        }
    }

    public BasicParticleType getParticleType(double seed) {
        ParticleType<BasicParticleType> particle;
        switch((int)(5*seed) % 5) {
            case 0:
                particle = ParticleTypeReg.SMOKE_DEBILITATION_A.get();
                break;
            case 1:
                particle = ParticleTypeReg.SMOKE_DEBILITATION_B.get();
                break;
            case 2:
                particle = ParticleTypeReg.SMOKE_DEBILITATION_C.get();
                break;
            case 3:
                particle = ParticleTypeReg.SMOKE_DEBILITATION_D.get();
                break;
            default: // case 4
                particle = ParticleTypeReg.SMOKE_DEBILITATION_E.get();
        }
        return (BasicParticleType)particle;
    }

}
