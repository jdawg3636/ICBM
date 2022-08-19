package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.common.event.AbstractBlastEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public abstract class EntityLingeringBlast extends Entity {

    public int ticksRemaining;
    // Note: blastType is never updated for EntityRedmatterBlast or EntityLingeringBlastRadiation
    public AbstractBlastEvent.Type blastType = AbstractBlastEvent.Type.EXPLOSIVES;

    public EntityLingeringBlast(EntityType<?> entityType, World level) {
        this(entityType, level, 20);
    }

    public EntityLingeringBlast(EntityType<?> entityType, World level, int ticksAlive) {
        super(entityType, level);
        this.ticksRemaining = ticksAlive;
    }

    @Override
    public abstract void tick();

    @Override
    protected void defineSynchedData() {}

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt) {
        nbt.putInt("TicksRemaining", ticksRemaining);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT nbt) {
        ticksRemaining = nbt.getInt("TicksRemaining");
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
