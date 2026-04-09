package com.jdawg3636.icbm.mixin.accessors;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.FallingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FallingBlockEntity.class)
public interface IFallingBlockEntityAccessor {
    @Accessor("blockState")
    void setBlockState(BlockState value);
    @Accessor("cancelDrop")
    void setCancelDrop(boolean value);
    @Accessor("fallDamageAmount")
    void setFallDamageAmount(float value);
}
