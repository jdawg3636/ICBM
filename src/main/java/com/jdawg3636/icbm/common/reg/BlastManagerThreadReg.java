package com.jdawg3636.icbm.common.reg;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.blast.thread.AntimatterBlastManagerThread;
import com.jdawg3636.icbm.common.blast.thread.NuclearBlastManagerThread;
import com.jdawg3636.icbm.common.blast.thread.VanillaBlastManagerThread;
import com.jdawg3636.icbm.common.blast.threadbuilder.AbstractBlastManagerThreadBuilder;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

/**
 * Custom registry used for serializing (and more importantly deserializing) blast threads of varying types
 */
public class BlastManagerThreadReg {

    public static final DeferredRegister<AbstractBlastManagerThreadBuilder> BLAST_MANAGER_THREADS = DeferredRegister.create(AbstractBlastManagerThreadBuilder.class, ICBMReference.MODID);

    public static final RegistryObject<AbstractBlastManagerThreadBuilder> ANTIMATTER = BLAST_MANAGER_THREADS.register("antimatter", () -> new AbstractBlastManagerThreadBuilder(AntimatterBlastManagerThread::new));
    public static final RegistryObject<AbstractBlastManagerThreadBuilder> NUCLEAR    = BLAST_MANAGER_THREADS.register("nuclear",    () -> new AbstractBlastManagerThreadBuilder(NuclearBlastManagerThread::new));
    public static final RegistryObject<AbstractBlastManagerThreadBuilder> VANILLA    = BLAST_MANAGER_THREADS.register("vanilla",    () -> new AbstractBlastManagerThreadBuilder(VanillaBlastManagerThread::new));

}
