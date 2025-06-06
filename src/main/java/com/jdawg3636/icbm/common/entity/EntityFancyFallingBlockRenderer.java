package com.jdawg3636.icbm.common.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.entity.item.FallingBlockEntity;

import javax.annotation.Nonnull;

public class EntityFancyFallingBlockRenderer extends FallingBlockRenderer {

    public EntityFancyFallingBlockRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void render(@Nonnull FallingBlockEntity entity, float entityYaw, float partialTick, @Nonnull MatrixStack poseStack, @Nonnull IRenderTypeBuffer buffer, int light) {
        super.render(entity, entityYaw, partialTick, poseStack, buffer, light);
    }

}
