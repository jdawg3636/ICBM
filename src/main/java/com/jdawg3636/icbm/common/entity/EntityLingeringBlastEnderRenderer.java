package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.listener.ClientProxy;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class EntityLingeringBlastEnderRenderer extends EntityRenderer<EntityLingeringBlastEnder> {

    public EntityLingeringBlastEnderRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void render(@Nonnull EntityLingeringBlastEnder entity, float entityYaw, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer renderBuffer, int combinedLight) {

        IBakedModel MODEL_SPHERE = Minecraft.getInstance().getModelManager().getModel(ClientProxy.MODEL_ENDER_BLAST_SPHERE);

        // Outer Push
        matrixStack.pushPose();

        // Vertical Offset (should probably just adjust this in the model file)
        matrixStack.translate(0D, 0.5D, 0D);

        // Scale
        final float scale = 3F * (float)ICBMReference.COMMON_CONFIG.getBlastRadiusEnder() / 20F;
        matrixStack.scale(scale, scale, scale);

        // Render Sphere
        Minecraft.getInstance().getItemRenderer().render(Items.STONE.getDefaultInstance(), ItemCameraTransforms.TransformType.NONE, false, matrixStack, renderBuffer, combinedLight, OverlayTexture.NO_OVERLAY, MODEL_SPHERE);

        // Outer Pop
        matrixStack.popPose();

    }

    /**
     * Ideally get rid of this (or at least make it less aggressive).
     * Implemented preemptively to avoid issues with the hitbox not being properly aligned
     */
    @Override
    public boolean shouldRender(EntityLingeringBlastEnder livingEntityIn, ClippingHelper camera, double camX, double camY, double camZ) {
        return true;
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull EntityLingeringBlastEnder entity) {
        return PlayerContainer.BLOCK_ATLAS;
    }

}
