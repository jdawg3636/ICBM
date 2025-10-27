package com.jdawg3636.icbm.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EntityBlastNightmare extends EntityBlastRedmatter {

    public static final List<EntityType<? extends LivingEntity>> entitiesThatCanBeSpawned = Arrays.asList(
            EntityType.CREEPER, EntityType.ENDERMAN, EntityType.SKELETON, EntityType.SKELETON_HORSE,
            EntityType.ZOMBIE, EntityType.ZOMBIE_HORSE, EntityType.ZOMBIFIED_PIGLIN
    );

    public EntityBlastNightmare(EntityType<?> entityType, World level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        final Random random = this.level.random;
        // Approx. once per second
        if(random.nextInt() % 20 == 0) {
            // Spawn 0-3 new entities
            for(int i = 0; i < random.nextInt(3 + 1); ++i) {
                // Select random entity from list
                final int idx = random.nextInt(entitiesThatCanBeSpawned.size());
                // Select random position offset
                final double xOffset = 3 * random.nextDouble();
                final double yOffset = 3 * random.nextDouble();
                final double zOffset = 3 * random.nextDouble();
                // Create entity
                LivingEntity newEntity = entitiesThatCanBeSpawned.get(idx).create(level);
                if(newEntity == null) {
                    break;
                }
                // Set entity position and add potion effects
                newEntity.setPos(getX() + xOffset, getY() + yOffset, getZ() + zOffset);
                newEntity.addEffect(new EffectInstance(Effects.ABSORPTION, Integer.MAX_VALUE, 25));
                newEntity.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, Integer.MAX_VALUE, 5));
                // Spawn entity into the level
                level.addFreshEntity(newEntity);
            }
        }
        // Call super
        super.tick();
    }

    @Override
    public void killEntityInCenter(Entity entity) {
        // Immunize LivingEntities from damage
        if(entity instanceof LivingEntity) {
            return;
        }
        super.killEntityInCenter(entity);
    }

    @Override
    public Vector3d adjustPhysics(Entity entity, Vector3d gravityVector) {
        // Reverse gravity for LivingEntities
        if(entity instanceof LivingEntity) {
            gravityVector = gravityVector.scale(-1d);
        }
        return gravityVector;
    }

}
