package com.jdawg3636.icbm.mixin;

import com.mojang.authlib.minecraft.OfflineSocialInteractions;
import com.mojang.authlib.minecraft.SocialInteractionsService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import net.minecraft.client.GameConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.util.concurrent.RecursiveEventLoop;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MixinClientForceOfflineSocialInteractions extends RecursiveEventLoop<Runnable> {

    // Some recent changes to Mojang's Authentication broke Multiplayer in older development environments
    // This is a brute-force solution that forces the client to behave as if the authentication servers are offline
    // Discussion in Legacy Modding Discord:
    // https://discord.gg/vFhf7acgMK
    // https://discord.com/channels/741043720241152001/1118442731979677776
    // Discussion in Fabric Discord:
    // https://discord.gg/v6v4pMv
    // https://discord.com/channels/507304429255393322/566418023372816394/1118559190466691212
    @Inject(method = "createSocialInteractions", at = @At("HEAD"), cancellable = true)
    public void mixin$offline_social_interactions$createSocialInteractions(YggdrasilAuthenticationService authService, GameConfiguration gameConfiguration, CallbackInfoReturnable<SocialInteractionsService> callback) {
        if(callback.getReturnValue() == null || !callback.getReturnValue().serversAllowed()) {
            LogManager.getLogger().warn("[offline_social_interactions] Detected Minecraft.socialInteractionsService.serversAllowed() == false; using Mixin to force use of OfflineSocialInteractions (what the vanilla game would use if authentication servers were offline or otherwise unreachable) instead of YggdrasilSocialInteractionsService");
            callback.setReturnValue(new OfflineSocialInteractions());
        }
    }

    public MixinClientForceOfflineSocialInteractions(String threadName) {
        super(threadName);
    }

}
