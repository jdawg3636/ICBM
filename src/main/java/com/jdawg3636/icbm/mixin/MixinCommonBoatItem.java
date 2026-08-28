package com.jdawg3636.icbm.mixin;

import com.jdawg3636.icbm.common.entity.EntityRubberWoodBoat;
import com.jdawg3636.icbm.common.reg.ItemReg;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.item.BoatItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BoatItem.class)
public abstract class MixinCommonBoatItem {

    @ModifyExpressionValue(method = "use", at = @At(value = "NEW", target = "net/minecraft/entity/item/BoatEntity"))
    private BoatEntity mixin$ballisticRedux$createBoat(BoatEntity original, World world) {
        if ((Object) this == ItemReg.RUBBER_WOOD_BOAT.get()) {
            return new EntityRubberWoodBoat(world, original.getX(), original.getY(), original.getZ());
        }
        return original;
    }

}
