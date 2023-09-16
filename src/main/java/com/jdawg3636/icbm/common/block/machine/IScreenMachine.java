package com.jdawg3636.icbm.common.block.machine;

public interface IScreenMachine {

    // Called on client when server requests a GUI refresh
    default void updateGui() {}

    // Called on client when GUI is closed or save is manually triggered (ex. by a button in the GUI)
    default void sendUpdatePacketToServer() {}

}
