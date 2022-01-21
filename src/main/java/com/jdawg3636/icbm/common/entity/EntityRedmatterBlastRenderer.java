package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.common.listener.ClientProxy;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nonnull;

public class EntityRedmatterBlastRenderer extends EntityRenderer<EntityRedmatterBlast> {

    public EntityRedmatterBlastRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void render(@Nonnull EntityRedmatterBlast entity, float entityYaw, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer renderBuffer, int combinedLight) {

        IBakedModel MODEL_ACCRETION_DISK = Minecraft.getInstance().getModelManager().getModel(ClientProxy.MODEL_REDMATTER_BLAST_ACCRETION_DISK);
        IBakedModel MODEL_SPHERE = Minecraft.getInstance().getModelManager().getModel(ClientProxy.MODEL_REDMATTER_BLAST_SPHERE);

        float animationRadians = ((float)(entity.getAnimationRadians() + (partialTicks * 0.01 * 0.25 * 2 * Math.PI)));

        // Outer Push
        matrixStack.pushPose();

        // Vertical Offset (should probably just adjust this in the model file)
        matrixStack.translate(0D, 0.5D, 0D);

        // Render Sphere
        Minecraft.getInstance().getItemRenderer().render(Items.STONE.getDefaultInstance(), ItemCameraTransforms.TransformType.NONE, false, matrixStack, renderBuffer, combinedLight, OverlayTexture.NO_OVERLAY, MODEL_SPHERE);

        // Clockwise Rotation
        matrixStack.mulPose(new Quaternion(new Vector3f(0, 1, 0), -animationRadians, false));

        // Render Accretion Disk
        Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(matrixStack.last(), renderBuffer.getBuffer(Atlases.translucentItemSheet()), Blocks.BLACK_STAINED_GLASS.defaultBlockState(), MODEL_ACCRETION_DISK, 1F, 1F, 1F, combinedLight, OverlayTexture.NO_OVERLAY, net.minecraftforge.client.model.data.EmptyModelData.INSTANCE);

        // Outer Pop
        matrixStack.popPose();

    }

    /**
     * Ideally get rid of this (or at least make it less aggressive).
     * Implemented preemptively to avoid issues with the hitbox not being properly aligned
     */
    @Override
    public boolean shouldRender(EntityRedmatterBlast livingEntityIn, ClippingHelper camera, double camX, double camY, double camZ) {
        return true;
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull EntityRedmatterBlast entity) {
        return AtlasTexture.LOCATION_BLOCKS;
    }

}
