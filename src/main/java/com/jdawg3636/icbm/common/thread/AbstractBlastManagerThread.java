package com.jdawg3636.icbm.common.thread;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Represents a "manager" thread for performing an asynchronous task.
 * May spawn "worker" threads to complete its tasks.
 */
public abstract class AbstractBlastManagerThread extends AbstractBlastWorkerThread implements INBTSerializable<CompoundNBT> {

    @Nullable
    private UUID ownerUUID;
    @Nullable
    private LivingEntity owner;

    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
        ownerUUID = owner == null ? null : owner.getUUID();
    }

    @Nullable
    public LivingEntity getOwner(ServerWorld level) {
        if (owner == null && ownerUUID != null) {
            Entity entity = level.getEntity(ownerUUID);
            if (entity == null) entity = level.getServer().getPlayerList().getPlayer(ownerUUID);
            if (entity instanceof LivingEntity) owner = (LivingEntity) entity;
        }
        return owner;
    }

    public AbstractBlastManagerThread() {
        super();
        this.setPriority(Thread.MAX_PRIORITY);
    }

    /**
     * @return null or a function to be executed by the calling thread after this thread's completion
     */
    public abstract Runnable getPostCompletionFunction(ServerWorld level);

    public abstract void initializeLevelCallbacks(ServerWorld level);

    public abstract String getRegistryName();

    @Override
    public abstract void run();

    /*
     * INBTSerializable
     */

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        if (ownerUUID != null) nbt.putUUID("Owner", ownerUUID);
        nbt.putString("manager_thread_type", getRegistryName());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ownerUUID = nbt.hasUUID("Owner") ? nbt.getUUID("Owner") : null;
        owner = null;
    }

}
