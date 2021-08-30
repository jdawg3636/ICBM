package com.jdawg3636.icbm.common.capability.blastcontroller;

import com.jdawg3636.icbm.common.blast.Blast;
import net.minecraftforge.common.capabilities.CapabilityManager;

import java.util.ArrayList;

public class BlastControllerCapability implements IBlastControllerCapability {

    public static void register() {
        CapabilityManager.INSTANCE.register(IBlastControllerCapability.class, new BlastControllerCapabilityStorage(), BlastControllerCapability::new);
    }

    private final ArrayList<Blast> activeBlasts = new ArrayList<>();

    @Override
    public void addBlast(Blast blast) {
        activeBlasts.add(blast);
    }

    @Override
    public void removeBlast(Blast blast) {
        activeBlasts.remove(blast);
    }

    @Override
    public ArrayList<Blast> getCurrentBlasts() {
        return new ArrayList<>(activeBlasts);
    }

}
