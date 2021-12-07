package com.jdawg3636.icbm.common.thread;

public abstract class AbstractBlastWorkerThread extends Thread {

    public AbstractBlastWorkerThread() {
        super();
        this.setPriority(Thread.MAX_PRIORITY);
    }

}
