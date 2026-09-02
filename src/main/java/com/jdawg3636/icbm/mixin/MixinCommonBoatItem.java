package com.jdawg3636.icbm.mixin;

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
        return this.mixin$ballisticRedux$creatBoat(original, world);
    }

    public BoatEntity mixin$ballisticRedux$creatBoat(BoatEntity original, World world) {
        return original;
    }

}
