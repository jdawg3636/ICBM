package com.jdawg3636.icbm.common.thread;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Represents a "manager" thread for performing an asynchronous task.
 * May spawn "worker" threads to complete its tasks.
 */
public abstract class AbstractBlastManagerThread extends Thread implements INBTSerializable<CompoundNBT> {

    /**
     * @return null or a function to be executed by the calling thread after this thread's completion
     */
    public abstract Runnable getPostCompletionFunction(ServerWorld level);

    public abstract void initializeLevelCallbacks(ServerWorld level);

    @Override
    public abstract void run();

    /*
     * INBTSerializable
     */

    @Override
    public abstract CompoundNBT serializeNBT();

    @Override
    public abstract void deserializeNBT(CompoundNBT nbt);

}
