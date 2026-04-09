package com.jdawg3636.icbm.mixin;

import com.jdawg3636.icbm.common.entity.EntityAcceleratingParticle;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class MixinCommonEntityAcceleratingParticleCollisionHook {

    @Inject(method = "collide", at = @At("RETURN"))
    private void icbm$mixin$onCollide(Vector3d motionVector, CallbackInfoReturnable<Vector3d> cir) {
        if ((Object) this instanceof EntityAcceleratingParticle) {
            Vector3d result = ((EntityAcceleratingParticle) (Object) this).postCollideHook(motionVector, cir.getReturnValue());
            cir.setReturnValue(result);
        }
    }

}
