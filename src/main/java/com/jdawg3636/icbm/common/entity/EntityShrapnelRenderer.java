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
    public void render(EntityShrapnel p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
        super.render(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityShrapnel entity) {
        return new ResourceLocation(ICBMReference.MODID, "textures/entity/shrapnel.png");
    }

}
