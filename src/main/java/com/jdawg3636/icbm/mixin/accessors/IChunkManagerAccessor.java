package com.jdawg3636.icbm.mixin.accessors;

import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ChunkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChunkManager.class)
public interface IChunkManagerAccessor {

    @Invoker("getVisibleChunkIfPresent")
    ChunkHolder invokeGetVisibleChunkIfPresent(long chunkPosAsLong);

}
