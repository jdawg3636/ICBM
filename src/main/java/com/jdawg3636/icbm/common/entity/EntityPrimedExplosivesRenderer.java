package com.jdawg3636.icbm.common.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.TNTMinecartRenderer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import javax.annotation.Nonnull;

// Copied almost one-to-one from net.minecraft.client.renderer.entity.TNTRenderer
public class EntityPrimedExplosivesRenderer extends EntityRenderer<EntityPrimedExplosives> {

    public BlockState blockState;

    public EntityPrimedExplosivesRenderer(EntityRendererManager renderManagerIn, BlockState blockState) {
        super(renderManagerIn);
        this.blockState = blockState;
    }

    @Override
    public void render(@Nonnull EntityPrimedExplosives entity, float entityYaw, float partialTick, @Nonnull MatrixStack matrix, @Nonnull IRenderTypeBuffer renderer, int light) {
        matrix.pushPose();
        matrix.translate(0, 0.5, 0);
        if (entity.getLife() - partialTick + 1.0F < 10.0F) {
            float f = 1.0F - (entity.getLife() - partialTick + 1.0F) / 10.0F;
            f = MathHelper.clamp(f, 0.0F, 1.0F);
            f = f * f;
            f = f * f;
            float f1 = 1.0F + f * 0.3F;
            matrix.scale(f1, f1, f1);
        }

        matrix.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
        matrix.translate(-0.5, -0.5, 0.5);
        matrix.mulPose(Vector3f.YP.rotationDegrees(90.0F));
        TNTMinecartRenderer.renderWhiteSolidBlock(blockState, matrix, renderer, light, entity.getLife() / 5 % 2 == 0);
        matrix.popPose();
        super.render(entity, entityYaw, partialTick, matrix, renderer, light);
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull EntityPrimedExplosives entity) {
        return AtlasTexture.LOCATION_BLOCKS;
    }

}
