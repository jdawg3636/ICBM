package com.jdawg3636.icbm.client.render.entity;

import com.jdawg3636.icbm.common.entity.EntityMissileIncendiary;
import com.jdawg3636.icbm.common.reg.ItemReg;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class EntityMissileIncendiaryRenderer extends EntityRenderer<EntityMissileIncendiary> {

    public EntityMissileIncendiaryRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void render(@Nonnull EntityMissileIncendiary entity, float entityYaw, float partialTick, @Nonnull MatrixStack matrix, @Nonnull IRenderTypeBuffer buffer, int light) {
        // Based on net.minecraft.client.renderer.entity.FireworkRocketRenderer
        Minecraft.getInstance().getItemRenderer().renderItem(ItemReg.MISSILE_INCENDIARY.get().getDefaultInstance(), ItemCameraTransforms.TransformType.NONE, light, OverlayTexture.NO_OVERLAY, matrix, buffer);
    }

    /**
     * Ideally get rid of this (or at least make it less aggressive)
     * */
    @Override
    public boolean shouldRender(EntityMissileIncendiary livingEntityIn, ClippingHelper camera, double camX, double camY, double camZ) {
        return true;
    }

    @Nonnull
    @Override
    public ResourceLocation getEntityTexture(@Nonnull EntityMissileIncendiary entity) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }

}
