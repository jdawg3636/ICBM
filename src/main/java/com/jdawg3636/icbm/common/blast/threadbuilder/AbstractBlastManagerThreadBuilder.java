package com.jdawg3636.icbm.common.blast.threadbuilder;

import com.jdawg3636.icbm.common.blast.thread.AbstractBlastManagerThread;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Registry Object for Blast Manager Threads
 */
public class AbstractBlastManagerThreadBuilder implements IForgeRegistryEntry<AbstractBlastManagerThreadBuilder> {

    private ResourceLocation registryName;
    private final Supplier<AbstractBlastManagerThread> blastManagerThreadSupplier;

    public AbstractBlastManagerThreadBuilder(Supplier<AbstractBlastManagerThread> blastManagerThreadSupplier) {
        this.blastManagerThreadSupplier = blastManagerThreadSupplier;
    }

    public AbstractBlastManagerThread build() {
        return blastManagerThreadSupplier.get();
    }

    @Override
    public AbstractBlastManagerThreadBuilder setRegistryName(ResourceLocation name) {
        if (getRegistryName() != null)
            throw new IllegalStateException("Attempted to set registry name with existing registry name! New: " + name + " Old: " + getRegistryName());
        this.registryName = GameData.checkPrefix(name.toString(), true);
        return this;
    }

    @Override
    @Nullable
    public ResourceLocation getRegistryName() {
        return registryName;
    }

    @Override
    public Class<AbstractBlastManagerThreadBuilder> getRegistryType() {
        return AbstractBlastManagerThreadBuilder.class;
    }

}
