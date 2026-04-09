package com.jdawg3636.icbm.mixin.accessors;

import net.minecraft.block.SlabBlock;
import net.minecraft.util.math.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SlabBlock.class)
public interface ISlabBlockAccessor {
    @Accessor("BOTTOM_AABB")
    static VoxelShape getBOTTOM_AABB() {
        throw new AssertionError();
    };
}
