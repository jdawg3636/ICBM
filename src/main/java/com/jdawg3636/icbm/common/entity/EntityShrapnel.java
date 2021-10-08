package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.common.blast.event.BlastEvent;
import com.jdawg3636.icbm.common.reg.EntityReg;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityShrapnel extends AbstractArrowEntity {

    boolean isExplosive = false;
    BlastEvent.Type blastType = BlastEvent.Type.EXPLOSIVES;

    public EntityShrapnel(EntityType<? extends EntityShrapnel> entityType, World level) {
        super(entityType, level);
    }

    public EntityShrapnel(World level, double positionX, double positionY, double positionZ) {
        this(level, positionX, positionY, positionZ, false, BlastEvent.Type.EXPLOSIVES);
    }

    public EntityShrapnel(World level, double positionX, double positionY, double positionZ, boolean isExplosive, BlastEvent.Type blastType) {
        super(EntityReg.SHRAPNEL.get(), positionX, positionY, positionZ, level);
        this.isExplosive = isExplosive;
        this.blastType = blastType;
    }

    public EntityShrapnel(World level, LivingEntity entity) {
        super(EntityReg.SHRAPNEL.get(), entity, level);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.CHAIN_HIT;
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult blockRayTraceResult) {
        if(!level.isClientSide()) {
            this.playSound(this.getHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            if(isExplosive) MinecraftForge.EVENT_BUS.post(new BlastEvent.ShrapnelImpact(new BlockPos(this.getX(), this.getY(), this.getZ()), (ServerWorld) this.level, this.blastType));
            this.kill();
        }
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult entityRayTraceResult) {
        if(!level.isClientSide()) {
            if(isExplosive) MinecraftForge.EVENT_BUS.post(new BlastEvent.ShrapnelImpact(new BlockPos(this.getX(), this.getY(), this.getZ()), (ServerWorld) this.level, this.blastType));
        }
        super.onHitEntity(entityRayTraceResult);
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        compound.putBoolean("IsExplosive", isExplosive);
        compound.putInt("BlastType", blastType.ordinal());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        isExplosive = compound.getBoolean("IsExplosive");
        blastType = BlastEvent.Type.values()[compound.getInt("BlastType")];
    }

}
