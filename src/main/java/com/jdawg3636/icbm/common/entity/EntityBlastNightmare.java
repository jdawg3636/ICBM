package com.jdawg3636.icbm.common.entity;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

public class EntityBlastNightmare extends EntityBlastRedmatter {

    public final List<Consumer<World>> entitySpawnFunctions = Arrays.asList(
        level -> this.prepareAndSpawnEntity(level, EntityType.CREEPER::create, false), // No status buffs for creeper - would spread to nearby entities (including the player) upon explosion
        level -> this.prepareAndSpawnEntity(level, EntityType.ENDERMAN::create),
        level -> this.prepareAndSpawnEntity(level, EntityType.SPIDER::create),
        level -> this.prepareAndSpawnEntity(level, EntityType.ZOMBIE::create),
        level -> this.prepareAndSpawnEntity(level, this::createZombifiedPiglinHoldingSword),
        level -> this.prepareAndSpawnEntity(level, this::createSkeletonHoldingBow),
        level -> this.prepareAndSpawnEntity(level, this::createSkeletonHoldingBow).startRiding(this.prepareAndSpawnEntity(level, pLevel -> this.createTamedHorse(pLevel, EntityType.SKELETON_HORSE::create))),
        level -> this.prepareAndSpawnEntity(level, this::createSkeletonHoldingBow).startRiding(this.prepareAndSpawnEntity(level, pLevel -> this.createTamedHorse(pLevel, EntityType.ZOMBIE_HORSE::create)))
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
                final int idx = random.nextInt(entitySpawnFunctions.size());
                // Create + Spawn Entity
                entitySpawnFunctions.get(idx).accept(level);
            }
        }
        // Call super
        super.tick();
    }

    public LivingEntity prepareAndSpawnEntity(World level, Function<World, LivingEntity> constructor) {
        return prepareAndSpawnEntity(level, constructor, true);
    }

    public LivingEntity prepareAndSpawnEntity(World level, Function<World, LivingEntity> constructor, boolean applyStatusBuffs) {
        // Create
        LivingEntity newEntity = constructor.apply(level);
        // Select random position offset
        final double xOffset = 3 * random.nextDouble();
        final double yOffset = 3 * random.nextDouble();
        final double zOffset = 3 * random.nextDouble();
        // Set entity position and add potion effects
        newEntity.setPos(getX() + xOffset, getY() + yOffset, getZ() + zOffset);
        if(applyStatusBuffs) {
            newEntity.addEffect(new EffectInstance(Effects.ABSORPTION, Integer.MAX_VALUE, 25));
            newEntity.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, Integer.MAX_VALUE, 5));
        }
        // Spawn entity into the level
        level.addFreshEntity(newEntity);
        // Return
        return newEntity;
    }

    public LivingEntity createSkeletonHoldingBow(World level) {
        LivingEntity livingEntity = EntityType.SKELETON.create(level);
        if(livingEntity == null) {
            return null;
        }
        ItemStack bowItem = Items.BOW.getDefaultInstance();
        bowItem.enchant(Enchantments.POWER_ARROWS, 5);
        bowItem.enchant(Enchantments.PUNCH_ARROWS, 2);
        bowItem.enchant(Enchantments.INFINITY_ARROWS, 1);
        livingEntity.setItemInHand(Hand.MAIN_HAND, bowItem);
        return livingEntity;
    }

    public LivingEntity createZombifiedPiglinHoldingSword(World level) {
        ZombifiedPiglinEntity livingEntity = EntityType.ZOMBIFIED_PIGLIN.create(level);
        if(livingEntity == null) {
            return null;
        }

        ItemStack swordItem = Items.GOLDEN_SWORD.getDefaultInstance();
        swordItem.enchant(Enchantments.SHARPNESS, 5);
        swordItem.enchant(Enchantments.FIRE_ASPECT, 2);
        livingEntity.setItemInHand(Hand.MAIN_HAND, swordItem);
        return livingEntity;
    }

    public LivingEntity createTamedHorse(World level, Function<World, AbstractHorseEntity> horseConstructor) {
        AbstractHorseEntity horse = horseConstructor.apply(level);
        horse.setTamed(true);
        return horse;
    }

    @Override
    public void tickAffectNearbyEntities(Entity entity) {
        // Don't affect the player
        if(entity instanceof PlayerEntity) {
            return;
        }
        super.tickAffectNearbyEntities(entity);
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
