package com.jdawg3636.icbm.mixin.accessors;

import net.minecraft.entity.item.minecart.TNTMinecartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TNTMinecartEntity.class)
public interface ITNTMinecartEntityAccessor {
    @Accessor("fuse") int getFuse();
    @Accessor("fuse") void setFuse(int value);
}
