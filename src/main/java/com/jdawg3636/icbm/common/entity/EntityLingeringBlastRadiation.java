package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.common.effect.EffectRadiation;
import com.jdawg3636.icbm.common.reg.EffectReg;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

import java.util.concurrent.atomic.AtomicReference;

public class EntityLingeringBlastRadiation extends EntityLingeringBlast {

    public double radius = 1;

    public EntityLingeringBlastRadiation(EntityType<?> entityType, World level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        // Check Lifetime
        if(ticksRemaining <= 0) {
            kill();
            return;
        }
        // Player Effects
        for(LivingEntity livingEntity : this.level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().contract(0,1,0).inflate(radius))) {
            if(livingEntity.isAffectedByPotions()) {
                AtomicReference<Float> percentageDamageReduction = new AtomicReference<>(0F);
                livingEntity.getArmorSlots().forEach((itemStack) -> {
                    if(itemStack.getItem().getTags().contains(EffectRadiation.RADIATION_PROTECTIVE_TAG)) {
                        percentageDamageReduction.updateAndGet(value -> value + .25F);
                    }
                });
                if(percentageDamageReduction.get() != 1F) {
                    livingEntity.addEffect(new EffectInstance(EffectReg.RADIATION.get(), 10 * 20, ticksRemaining <= 0 ? 270 : 1));
                }
            }
        }
        --ticksRemaining;
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putDouble("Radius", radius);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
        radius = nbt.getDouble("Radius");
    }

}
