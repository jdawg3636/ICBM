package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.common.reg.EntityReg;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public class EntityMissile extends Entity {

    public static final DataParameter<Float> ROTATION_X = EntityDataManager.defineId(EntityMissile.class, DataSerializers.FLOAT);
    public static final DataParameter<Float> ROTATION_Y = EntityDataManager.defineId(EntityMissile.class, DataSerializers.FLOAT);
    public static final DataParameter<Float> ROTATION_Z = EntityDataManager.defineId(EntityMissile.class, DataSerializers.FLOAT);

    RegistryObject<Item> missileItem;

    public EntityMissile(EntityType<?> entityTypeIn, World worldIn, RegistryObject<Item> missileItem) {
        super(entityTypeIn, worldIn);
        this.missileItem = missileItem;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(ROTATION_X, 0F);
        entityData.define(ROTATION_Y, 0F);
        entityData.define(ROTATION_Z, 0F);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {
        entityData.set(ROTATION_X, compound.getFloat("MissileRotationX"));
        entityData.set(ROTATION_Y, compound.getFloat("MissileRotationY"));
        entityData.set(ROTATION_Z, compound.getFloat("MissileRotationZ"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {
        compound.putFloat("MissileRotationX", entityData.get(ROTATION_X));
        compound.putFloat("MissileRotationY", entityData.get(ROTATION_Y));
        compound.putFloat("MissileRotationZ", entityData.get(ROTATION_Z));
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    protected boolean isMovementNoisy() {
        return false;
    }

    @Override
    protected boolean canRide(Entity entityIn) {
        return true;
    }

    @Override
    public final ActionResultType interact(PlayerEntity player, Hand hand) {
        if (!this.isVehicle() && !player.isSecondaryUseActive()) {
            if (!this.level.isClientSide) {
                player.startRiding(this);
            }
            return ActionResultType.sidedSuccess(this.level.isClientSide);
        }
        else {
            return ActionResultType.PASS;
        }
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return missileItem.get().getDefaultInstance();
    }

}
