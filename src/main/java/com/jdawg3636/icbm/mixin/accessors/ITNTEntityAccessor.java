package com.jdawg3636.icbm.mixin.accessors;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.TNTEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TNTEntity.class)
public interface ITNTEntityAccessor {
    @Accessor("owner")
    void setOwner(LivingEntity value);
}
