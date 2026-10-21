package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.common.event.AbstractBlastEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class EntityBlastLingering extends Entity {

    @Nullable
    private UUID ownerUUID;
    @Nullable
    private LivingEntity owner;

    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
        ownerUUID = owner == null ? null : owner.getUUID();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (owner == null && ownerUUID != null && level instanceof ServerWorld) {
            ServerWorld world = (ServerWorld) level;
            Entity entity = world.getEntity(ownerUUID);
            if (entity == null) entity = world.getServer().getPlayerList().getPlayer(ownerUUID);
            if (entity instanceof LivingEntity) owner = (LivingEntity) entity;
        }
        return owner;
    }

    public int ticksRemaining;
    // Note: blastType is never updated for EntityRedmatterBlast or EntityLingeringBlastRadiation
    public AbstractBlastEvent.Type blastType = AbstractBlastEvent.Type.EXPLOSIVES;

    public EntityBlastLingering(EntityType<?> entityType, World level) {
        this(entityType, level, 20);
    }

    public EntityBlastLingering(EntityType<?> entityType, World level, int ticksAlive) {
        super(entityType, level);
        this.ticksRemaining = ticksAlive;
    }

    @Override
    public abstract void tick();

    @Override
    protected void defineSynchedData() {}

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt) {
        if (ownerUUID != null) nbt.putUUID("Owner", ownerUUID);
        nbt.putInt("TicksRemaining", ticksRemaining);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT nbt) {
        ownerUUID = nbt.hasUUID("Owner") ? nbt.getUUID("Owner") : null;
        owner = null;
        ticksRemaining = nbt.getInt("TicksRemaining");
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldRender(double playerX, double playerY, double playerZ) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
