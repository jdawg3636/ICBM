package com.jdawg3636.icbm.common.effect;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.reg.EffectReg;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EffectEngineeredPathogen extends Effect {

    public static final float ENGINEERED_PATHOGEN_DAMAGE_PER_TICK = 1F;
    public static final DamageSource ENGINEERED_PATHOGEN_DAMAGE_SOURCE = (new DamageSource("icbm.engineered_pathogen")).bypassArmor();

    public EffectEngineeredPathogen() {
        this(EffectType.HARMFUL, 0xD44222);
    }

    public EffectEngineeredPathogen(EffectType category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        World level = livingEntity.level;
        if (level instanceof ServerWorld) {
            ServerWorld serverLevel = (ServerWorld)level;

            // Skip if immune
            if(livingEntity.getActiveEffects().stream().map(EffectInstance::getEffect).collect(Collectors.toList()).contains(EffectReg.ENGINEERED_PATHOGEN_IMMUNITY.get())) {
                return;
            }

            // Skip if already converted
            if(livingEntity instanceof ZombieVillagerEntity || livingEntity instanceof ZombifiedPiglinEntity) {
                return;
            }

            // If villager, convert to zombie
            // Adapted from net.minecraft.entity.ZombieEntity::monsterkilled, MC 1.16.5, MCP Package/Class Names, Mojmap Method/Field Names
            if (serverLevel.getDifficulty() != Difficulty.PEACEFUL && livingEntity instanceof VillagerEntity && net.minecraftforge.event.ForgeEventFactory.canLivingConvert(livingEntity, EntityType.ZOMBIE_VILLAGER, (timer) -> {})) {
                if (serverLevel.getDifficulty() != Difficulty.HARD && livingEntity.getRandom().nextBoolean()) {
                    return;
                }

                VillagerEntity villagerentity = (VillagerEntity) livingEntity;
                ZombieVillagerEntity zombievillagerentity = villagerentity.convertTo(EntityType.ZOMBIE_VILLAGER, false);
                zombievillagerentity.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(zombievillagerentity.blockPosition()), SpawnReason.CONVERSION, new ZombieEntity.GroupData(false, true), (CompoundNBT) null);
                zombievillagerentity.setVillagerData(villagerentity.getVillagerData());
                zombievillagerentity.setGossips(villagerentity.getGossips().store(NBTDynamicOps.INSTANCE).getValue());
                zombievillagerentity.setTradeOffers(villagerentity.getOffers().createTag());
                zombievillagerentity.setVillagerXp(villagerentity.getVillagerXp());
                net.minecraftforge.event.ForgeEventFactory.onLivingConvert(livingEntity, zombievillagerentity);
                // Plays Zombie Effect Sound
                serverLevel.levelEvent((PlayerEntity) null, 1026, livingEntity.blockPosition(), 0);
                livingEntity.remove();
            }

            // If pig, convert to zombified piglin
            // Adapted from net.minecraft.entity.passive.PigEntity::thunderHit, MC 1.16.5, MCP Package/Class Names, Mojmap Method/Field Names
            else if (serverLevel.getDifficulty() != Difficulty.PEACEFUL && livingEntity instanceof PigEntity && net.minecraftforge.event.ForgeEventFactory.canLivingConvert(livingEntity, EntityType.ZOMBIFIED_PIGLIN, (timer) -> {})) {
                ZombifiedPiglinEntity zombifiedpiglinentity = EntityType.ZOMBIFIED_PIGLIN.create(serverLevel);
                zombifiedpiglinentity.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
                zombifiedpiglinentity.moveTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), livingEntity.yRot, livingEntity.xRot);
                zombifiedpiglinentity.setNoAi(((PigEntity)livingEntity).isNoAi());
                zombifiedpiglinentity.setBaby(livingEntity.isBaby());
                if (livingEntity.hasCustomName()) {
                    zombifiedpiglinentity.setCustomName(livingEntity.getCustomName());
                    zombifiedpiglinentity.setCustomNameVisible(livingEntity.isCustomNameVisible());
                }

                zombifiedpiglinentity.setPersistenceRequired();
                net.minecraftforge.event.ForgeEventFactory.onLivingConvert(livingEntity, zombifiedpiglinentity);
                serverLevel.addFreshEntity(zombifiedpiglinentity);
                livingEntity.remove();
            }

            // Not transforming - apply to this entity
            else {
                // Spread to nearby
                double spreadRadius = ICBMReference.COMMON_CONFIG.getEngineeredPathogenSpreadRadius();
                if (spreadRadius > 0) {
                    spreadRadius = Math.max(1, spreadRadius);
                    serverLevel.getEntities(
                            livingEntity,
                            AxisAlignedBB.unitCubeFromLowerCorner(livingEntity.position()).inflate(spreadRadius - 1), LivingEntity.class::isInstance
                    ).stream().map(LivingEntity.class::cast).forEach((LivingEntity otherNearbyLivingEntity) -> {

                    });
                }
                // Renew Pathogen effect
                if(livingEntity instanceof PlayerEntity && ICBMReference.COMMON_CONFIG.getEngineeredPathogenPerpetualForPlayers()) {
                    livingEntity.addEffect(new EffectInstance(EffectReg.ENGINEERED_PATHOGEN.get(), 45 * 20, 0));
                }
                // Apply side effects
                livingEntity.addEffect(new EffectInstance(Effects.BLINDNESS,                   15 * 20, 0));
                livingEntity.addEffect(new EffectInstance(Effects.DIG_SLOWDOWN,                60 * 20, 0));
                livingEntity.addEffect(new EffectInstance(Effects.WEAKNESS,                    35 * 20, 0));
                livingEntity.addEffect(new EffectInstance(Effects.HUNGER,                      30 * 20, 0));
                // Inflict Damage
                livingEntity.hurt(ENGINEERED_PATHOGEN_DAMAGE_SOURCE, ENGINEERED_PATHOGEN_DAMAGE_PER_TICK);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        // Return an empty list to prevent the milk bucket from clearing effect
        return new ArrayList<>();
    }

    @Override
    public String getOrCreateDescriptionId() {
        return Util.makeDescriptionId("effect", ForgeRegistries.POTIONS.getKey(this));
    }

}
