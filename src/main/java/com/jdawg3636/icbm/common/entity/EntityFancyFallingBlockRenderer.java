package com.jdawg3636.icbm.common.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.util.math.vector.Quaternion;

import javax.annotation.Nonnull;

public class EntityFancyFallingBlockRenderer extends FallingBlockRenderer {

    public EntityFancyFallingBlockRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void render(@Nonnull FallingBlockEntity entity, float entityYaw, float partialTick, @Nonnull MatrixStack poseStack, @Nonnull IRenderTypeBuffer buffer, int light) {
        poseStack.pushPose();
        poseStack.mulPose(new Quaternion(entity.xRot, entity.yRot, 0, true));
        super.render(entity, entityYaw, partialTick, poseStack, buffer, light);
        poseStack.popPose();
    }

}
