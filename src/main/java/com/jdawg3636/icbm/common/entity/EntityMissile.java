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

    public static final DataParameter<Float> ROTATION_X = EntityDataManager.createKey(EntityMissile.class, DataSerializers.FLOAT);
    public static final DataParameter<Float> ROTATION_Y = EntityDataManager.createKey(EntityMissile.class, DataSerializers.FLOAT);
    public static final DataParameter<Float> ROTATION_Z = EntityDataManager.createKey(EntityMissile.class, DataSerializers.FLOAT);

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
    protected void registerData() {
        dataManager.register(ROTATION_X, 0F);
        dataManager.register(ROTATION_Y, 0F);
        dataManager.register(ROTATION_Z, 0F);
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        dataManager.set(ROTATION_X, compound.getFloat("MissileRotationX"));
        dataManager.set(ROTATION_Y, compound.getFloat("MissileRotationY"));
        dataManager.set(ROTATION_Z, compound.getFloat("MissileRotationZ"));
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putFloat("MissileRotationX", dataManager.get(ROTATION_X));
        compound.putFloat("MissileRotationY", dataManager.get(ROTATION_Y));
        compound.putFloat("MissileRotationZ", dataManager.get(ROTATION_Z));
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected boolean canBeRidden(Entity entityIn) {
        return true;
    }

    @Override
    public final ActionResultType processInitialInteract(PlayerEntity player, Hand hand) {
        if (!this.isBeingRidden() && !player.isSecondaryUseActive()) {
            if (!this.world.isRemote) {
                player.startRiding(this);
            }
            return ActionResultType.func_233537_a_(this.world.isRemote);
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
