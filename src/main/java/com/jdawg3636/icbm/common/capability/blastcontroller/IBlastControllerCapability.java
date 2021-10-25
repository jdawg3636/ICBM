package com.jdawg3636.icbm.common.capability.blastcontroller;

import com.jdawg3636.icbm.common.blast.thread.AbstractBlastManagerThread;
import net.minecraftforge.event.TickEvent;

import java.util.ArrayList;

// Attached to World, used to store data related to implementing blast events
public interface IBlastControllerCapability {

    void enqueueBlastThread(AbstractBlastManagerThread blast);

    void dequeueBlastThread(AbstractBlastManagerThread blast);

    ArrayList<AbstractBlastManagerThread> getQueuedBlastThreads();

    void addBlastThread(AbstractBlastManagerThread blast);

    void removeBlastThread(AbstractBlastManagerThread blast);

    ArrayList<AbstractBlastManagerThread> getActiveBlastThreads();

    void onWorldTickEvent(TickEvent.WorldTickEvent event);

}
