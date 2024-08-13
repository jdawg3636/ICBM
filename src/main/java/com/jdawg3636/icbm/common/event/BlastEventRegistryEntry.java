package com.jdawg3636.icbm.common.event;

import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public class BlastEventRegistryEntry implements AbstractBlastEvent.BlastEventProvider, IForgeRegistryEntry<BlastEventRegistryEntry>  {

    private ResourceLocation registryName;
    private AbstractBlastEvent.BlastEventProvider blastEvent;

    public BlastEventRegistryEntry(AbstractBlastEvent.BlastEventProvider blastEvent) {
        this.blastEvent = blastEvent;
    }

    @Override
    public AbstractBlastEvent getBlastEvent(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType, Direction blastDirection) {
        return blastEvent.getBlastEvent(blastPosition, blastWorld, blastType, blastDirection);
    }

    @Override
    public BlastEventRegistryEntry setRegistryName(ResourceLocation name) {
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
    public Class<BlastEventRegistryEntry> getRegistryType() {
        return BlastEventRegistryEntry.class;
    }

}
