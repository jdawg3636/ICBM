package com.jdawg3636.icbm.mixin;

import com.jdawg3636.icbm.common.item.ItemRocketLauncher;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public abstract class MixinClientRocketLauncherPose extends LivingRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {

    // Forces the PlayerRenderer (third person) to use the player pose for a drawn bow and
    // arrow, even though the item is not in use. Mimics behavior of the MC 1.5.2 version.
    @Inject(method = "func_241741_a_", at = @At("HEAD"), cancellable = true)
    private static void onGetArmPose(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedModel.ArmPose> callback) {
        if(player.getHeldItem(hand).getItem() instanceof ItemRocketLauncher) {
            callback.setReturnValue(BipedModel.ArmPose.BOW_AND_ARROW);
        }
    }

    // Constructor to Trick the Java Compiler
    public MixinClientRocketLauncherPose(EntityRendererManager renderManager, boolean useSmallArms) {
        super(renderManager, new PlayerModel<>(0.0F, useSmallArms), 0.5F);
    }

}
