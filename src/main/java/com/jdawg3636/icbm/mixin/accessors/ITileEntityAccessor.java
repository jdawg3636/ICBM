package com.jdawg3636.icbm.mixin.accessors;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TileEntity.class)
public interface ITileEntityAccessor {
    @Mutable @Accessor("type") void icbm$setType(TileEntityType<?> type);
}
