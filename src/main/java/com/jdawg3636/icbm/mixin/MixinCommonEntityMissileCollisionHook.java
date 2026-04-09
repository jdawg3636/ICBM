package com.jdawg3636.icbm.mixin;

import com.jdawg3636.icbm.common.entity.EntityMissile;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class MixinCommonEntityMissileCollisionHook {

    @Inject(
            method = "collide",
            at = @At("HEAD"),
            cancellable = true
    )
    private void icbm$mixin$onCollide(Vector3d vec, CallbackInfoReturnable<Vector3d> cir) {
        if ((Object) this instanceof EntityMissile) {
            Vector3d result = ((EntityMissile) (Object) this).collideOverride(vec);
            cir.setReturnValue(result);
        }
    }

}
