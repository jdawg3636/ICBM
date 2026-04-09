package com.jdawg3636.icbm.mixin.accessors;

import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataParameter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface IEntityAccessor {
    @Accessor("DATA_NO_GRAVITY")
    static DataParameter<Boolean> getDATA_NO_GRAVITY() {
        throw new AssertionError();
    }
}
