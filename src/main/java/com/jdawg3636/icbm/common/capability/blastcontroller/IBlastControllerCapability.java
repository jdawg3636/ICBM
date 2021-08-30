package com.jdawg3636.icbm.common.capability.blastcontroller;

import com.jdawg3636.icbm.common.blast.Blast;

import java.util.ArrayList;

// Attached to World, used to store data related to implementing blast events
public interface IBlastControllerCapability {

    void addBlast(Blast blast);

    void removeBlast(Blast blast);

    ArrayList<Blast> getCurrentBlasts();

}
