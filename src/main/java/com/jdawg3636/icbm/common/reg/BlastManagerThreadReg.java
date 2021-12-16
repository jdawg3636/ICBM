package com.jdawg3636.icbm.common.reg;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.thread.AntimatterBlastManagerThread;
import com.jdawg3636.icbm.common.thread.NuclearBlastManagerThread;
import com.jdawg3636.icbm.common.thread.RaytracedBlastManagerThread;
import com.jdawg3636.icbm.common.thread.SonicBlastManagerThread;
import com.jdawg3636.icbm.common.thread.builder.AbstractBlastManagerThreadBuilder;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

/**
 * Custom registry used for serializing (and more importantly deserializing) blast threads of varying types
 */
public class BlastManagerThreadReg {

    public static final DeferredRegister<AbstractBlastManagerThreadBuilder> BLAST_MANAGER_THREADS = DeferredRegister.create(AbstractBlastManagerThreadBuilder.class, ICBMReference.MODID);

    public static AbstractBlastManagerThreadBuilder getBuilderFromID(String id) {
        for(RegistryObject<AbstractBlastManagerThreadBuilder> blastManagerThreadBuilder : BlastManagerThreadReg.BLAST_MANAGER_THREADS.getEntries()) {
            if(blastManagerThreadBuilder.getId().toString().equals(id)) {
                return blastManagerThreadBuilder.get();
            }
        }
        return null;
    }

    public static final RegistryObject<AbstractBlastManagerThreadBuilder> ANTIMATTER = BLAST_MANAGER_THREADS.register("antimatter", () -> new AbstractBlastManagerThreadBuilder(AntimatterBlastManagerThread::new));
    public static final RegistryObject<AbstractBlastManagerThreadBuilder> HYPERSONIC = BLAST_MANAGER_THREADS.register("hypersonic", () -> new AbstractBlastManagerThreadBuilder(() -> new SonicBlastManagerThread() { public String getRegistryName() { return "icbm:hypersonic"; } }));
    public static final RegistryObject<AbstractBlastManagerThreadBuilder> NUCLEAR    = BLAST_MANAGER_THREADS.register("nuclear",    () -> new AbstractBlastManagerThreadBuilder(NuclearBlastManagerThread::new));
    public static final RegistryObject<AbstractBlastManagerThreadBuilder> RAYTRACED  = BLAST_MANAGER_THREADS.register("raytraced",  () -> new AbstractBlastManagerThreadBuilder(RaytracedBlastManagerThread::new));
    public static final RegistryObject<AbstractBlastManagerThreadBuilder> SONIC      = BLAST_MANAGER_THREADS.register("sonic",      () -> new AbstractBlastManagerThreadBuilder(SonicBlastManagerThread::new));

}
