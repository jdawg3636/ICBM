package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.ICBMReference;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class EntityShrapnelRenderer extends ArrowRenderer<EntityShrapnel> {

    public EntityShrapnelRenderer(EntityRendererManager rendererManager) {
        super(rendererManager);
    }

    @Override
    public void render(EntityShrapnel entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityShrapnel entity) {
        return new ResourceLocation(ICBMReference.MODID, "textures/entity/shrapnel.png");
    }

}
