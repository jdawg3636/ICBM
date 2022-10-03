package com.jdawg3636.icbm.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityAcceleratingParticle extends Entity {

    public EntityAcceleratingParticle(EntityType<?> entityType, World level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        // todo
    }

    @Override
    protected void defineSynchedData() {
        // todo: sync acceleration progress
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT pCompound) {
        // todo
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT pCompound) {
        // todo
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
