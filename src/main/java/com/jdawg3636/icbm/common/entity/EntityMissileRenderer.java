package com.jdawg3636.icbm.common.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nonnull;

public class EntityMissileRenderer extends EntityRenderer<EntityMissile> {

    public ItemStack missileItem;

    public EntityMissileRenderer(EntityRendererManager renderManagerIn, ItemStack missileItem) {
        super(renderManagerIn);
        this.missileItem = missileItem;
    }

    @Override
    public void render(@Nonnull EntityMissile entity, float entityYaw, float partialTick, @Nonnull MatrixStack matrix, @Nonnull IRenderTypeBuffer buffer, int light) {

        matrix.pushPose();

        // Usage: MathHelper.lerp(scalar, start, dest)
        matrix.mulPose(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTick, -entity.yRotO, -entity.yRot)));
        matrix.mulPose(Vector3f.XP.rotationDegrees(MathHelper.lerp(partialTick, entity.xRotO + 90, entity.xRot + 90)));

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
        return AtlasTexture.LOCATION_BLOCKS;
    }

}
