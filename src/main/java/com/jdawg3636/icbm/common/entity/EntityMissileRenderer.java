package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.common.block.launcher_platform.TileLauncherPlatform;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nonnull;

public class EntityMissileRenderer extends EntityRenderer<EntityMissile> {

    public final ItemStack missileItem;

    public EntityMissileRenderer(EntityRendererManager renderManagerIn, ItemStack missileItem) {
        super(renderManagerIn);
        this.missileItem = missileItem;
    }

    @Override
    public void render(@Nonnull EntityMissile entity, float entityYaw, float partialTick, @Nonnull MatrixStack matrix, @Nonnull IRenderTypeBuffer buffer, int light) {

        matrix.pushPose();

        // Constants
        TileEntity blockEntity = entity.level.getBlockEntity(entity.blockPosition());
        boolean isLaunched = entity.missileLaunchPhase == EntityMissile.MissileLaunchPhase.LAUNCHED;
        boolean isFromCruiseLauncher = entity.missileSourceType == EntityMissile.MissileSourceType.CRUISE_LAUNCHER;

        // Dirty Hack for Cruise Launcher Translation (This should really be defined by an overridable method in the BlockEntity)
        if(isFromCruiseLauncher) {
            matrix.translate(0, 9D/16D, 0);
        }

        // Rotation
        if(blockEntity instanceof TileLauncherPlatform && !isLaunched) {
            // Using Platform-Specified Rotation
            TileLauncherPlatform blockEntityLauncherPlatform = (TileLauncherPlatform) blockEntity;
            matrix.mulPose(Vector3f.YP.rotation((float)blockEntityLauncherPlatform.getYawRadians()));
            matrix.mulPose(Vector3f.XP.rotation((float)blockEntityLauncherPlatform.getPitchRadians()));
            if(isFromCruiseLauncher) matrix.mulPose(Vector3f.XP.rotation((float)(-1D * Math.PI / 2D)));
        }
        else {
            // Using Entity's Own Rotation
            matrix.mulPose(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTick, -entity.yRotO, -entity.yRot)));
            matrix.mulPose(Vector3f.XP.rotationDegrees(MathHelper.lerp(partialTick, entity.xRotO + 90, entity.xRot + 90)));
        }

        // Dirty Hack for Cruise Launcher Scale/Rotation (This should really be defined by an overridable method in the BlockEntity)
        if(isFromCruiseLauncher) {
            matrix.mulPose(Vector3f.YP.rotation((float)(Math.PI / 4D)));
            matrix.scale(0.5F, 0.5F, 0.5F);
        }

        matrix.translate(0, 0.5, 0);

        // Based on net.minecraft.client.renderer.entity.FireworkRocketRenderer
        Minecraft.getInstance().getItemRenderer().renderStatic(missileItem, ItemCameraTransforms.TransformType.NONE, light, OverlayTexture.NO_OVERLAY, matrix, buffer);

        matrix.popPose();

    }

    /**
     * Ideally get rid of this (or at least make it less aggressive).
     * Implemented preemptively as a missile's hitbox will frequently not line up with its render.
     */
    @Override
    public boolean shouldRender(EntityMissile livingEntityIn, ClippingHelper camera, double camX, double camY, double camZ) {
        return true;
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull EntityMissile entity) {
        return PlayerContainer.BLOCK_ATLAS;
    }

}
