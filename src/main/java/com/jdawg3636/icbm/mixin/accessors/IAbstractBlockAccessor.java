package com.jdawg3636.icbm.mixin.accessors;

import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractBlock.class)
public interface IAbstractBlockAccessor {
    @Accessor("explosionResistance")
    float getExplosionResistance();
    @Accessor("explosionResistance")
    void setExplosionResistance(float value);
}
