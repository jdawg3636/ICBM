package com.jdawg3636.icbm.mixin.accessors;

import net.minecraft.item.ArmorMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ArmorMaterial.class)
public interface IArmorMaterialAccessor {
    @Accessor("HEALTH_PER_SLOT")
    static int[] getHealthPerSlotLUT() {
        throw new AssertionError();
    }
}
