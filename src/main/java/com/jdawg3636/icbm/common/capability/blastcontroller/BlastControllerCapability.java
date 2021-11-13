package com.jdawg3636.icbm.common.capability.blastcontroller;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.thread.AbstractBlastManagerThread;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.TickEvent;

import java.util.ArrayList;

public class BlastControllerCapability implements IBlastControllerCapability {

    public static final int MAX_THREAD_COUNT = Integer.MAX_VALUE; // todo replace with a value from config file

    public static void register() {
        CapabilityManager.INSTANCE.register(IBlastControllerCapability.class, new BlastControllerCapabilityStorage(), BlastControllerCapability::new);
    }

    private final ArrayList<AbstractBlastManagerThread> activeBlastThreads = new ArrayList<>();
    private final ArrayList<AbstractBlastManagerThread> queuedBlastThreads = new ArrayList<>();

    @Override
    public void enqueueBlastThread(AbstractBlastManagerThread blast) {
        queuedBlastThreads.add(blast);
    }

    @Override
    public void dequeueBlastThread(AbstractBlastManagerThread blast) {
        queuedBlastThreads.remove(blast);
    }

    @Override
    public ArrayList<AbstractBlastManagerThread> getQueuedBlastThreads() {
        return new ArrayList<>(queuedBlastThreads);
    }

    @Override
    public void addBlastThread(AbstractBlastManagerThread blast) {
        activeBlastThreads.add(blast);
    }

    @Override
    public void removeBlastThread(AbstractBlastManagerThread blast) {
        blast.interrupt();
        activeBlastThreads.remove(blast);
    }

    @Override
    public ArrayList<AbstractBlastManagerThread> getActiveBlastThreads() {
        return new ArrayList<>(activeBlastThreads);
    }

    @Override
    public void onWorldTickEvent(TickEvent.WorldTickEvent event) {

        if(event.world instanceof ServerWorld) {

            // Start Enqueued Threads if a Slot is Available
            if (activeBlastThreads.size() < MAX_THREAD_COUNT && queuedBlastThreads.size() > 0) {
                AbstractBlastManagerThread thread = queuedBlastThreads.remove(0);
                thread.initializeLevelCallbacks((ServerWorld) event.world);
                thread.start();
                addBlastThread(thread);
                /*todo remove debug*/ ICBMReference.logger().info("Activated thread, count = " + activeBlastThreads.size());
            }

            // Remove Completed Threads
            ArrayList<AbstractBlastManagerThread> inactiveBlastThreads = new ArrayList<>();
            for (AbstractBlastManagerThread thread : activeBlastThreads) {
                if (!thread.isAlive()) {
                    Runnable postCompletionFunction = thread.getPostCompletionFunction((ServerWorld) event.world);
                    if(postCompletionFunction != null) postCompletionFunction.run();
                    inactiveBlastThreads.add(thread);
                }
            }
            for (AbstractBlastManagerThread thread : inactiveBlastThreads) {
                removeBlastThread(thread);
                /*todo remove debug*/ ICBMReference.logger().info("Deactivated thread, count = " + activeBlastThreads.size());
            }

        }

    }

}
