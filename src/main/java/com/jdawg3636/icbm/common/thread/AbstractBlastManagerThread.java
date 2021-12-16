package com.jdawg3636.icbm.common.thread;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Represents a "manager" thread for performing an asynchronous task.
 * May spawn "worker" threads to complete its tasks.
 */
public abstract class AbstractBlastManagerThread extends AbstractBlastWorkerThread implements INBTSerializable<CompoundNBT> {

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
        nbt.putString("manager_thread_type", getRegistryName());
        return nbt;
    }

    @Override
    public abstract void deserializeNBT(CompoundNBT nbt);

}
