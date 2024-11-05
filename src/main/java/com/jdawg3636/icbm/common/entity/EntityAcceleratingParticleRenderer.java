package com.jdawg3636.icbm.common.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nonnull;
import java.util.Random;

public class EntityAcceleratingParticleRenderer extends EntityRenderer<EntityAcceleratingParticle> {

    private static final float HALF_SQRT_3 = (float)(Math.sqrt(3.0D) / 2.0D);

    public EntityAcceleratingParticleRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void render(@Nonnull EntityAcceleratingParticle entity, float entityYaw, float partialTick, @Nonnull MatrixStack poseStack, @Nonnull IRenderTypeBuffer buffer, int light) {

        final float animationProgress = entity.tickCount / 100F;
        Random random = new Random(432L);
        IVertexBuilder vertexBuilder = buffer.getBuffer(RenderType.lightning());

        // Outer Push
        poseStack.pushPose();
        // Offset reference frame to match the center of the entity's hitbox
        poseStack.translate(0.0D, 0.5D, 0D);
        // Iterate through rays and generate vertices for each
        for(int i = 0; i < 35; ++i) {
            // Rotation - "random.nextFloat" calls are deterministic due to fixed seed, so only frame-to-frame variation is from animationProgress
            poseStack.mulPose(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F));
            poseStack.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F));
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F));
            poseStack.mulPose(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F));
            poseStack.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F));
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F + animationProgress * 90.0F));
            // Side parameter constants (once again, "random.nextFloat" calls are deterministic due to fixed seed)
            Matrix4f latestPose = poseStack.last().pose();
            final float length = random.nextFloat() * 1.25F + 0.75F;
            final float width = random.nextFloat() * 0.65F + 0.1F;
            // Side 1
            vertex1(vertexBuilder, latestPose);
            vertex2(vertexBuilder, latestPose, length, width);
            vertex3(vertexBuilder, latestPose, length, width);
            // Side 2
            vertex1(vertexBuilder, latestPose);
            vertex3(vertexBuilder, latestPose, length, width);
            vertex4(vertexBuilder, latestPose, length, width);
            // Side 3
            vertex1(vertexBuilder, latestPose);
            vertex4(vertexBuilder, latestPose, length, width);
            vertex2(vertexBuilder, latestPose, length, width);
        }

        // Outer Pop
        poseStack.popPose();

    }

    private static void vertex1(IVertexBuilder vertexBuilder, Matrix4f pose) {
        vertexBuilder.vertex(pose, 0.0F, 0.0F, 0.0F).color(255, 255, 255, 128).endVertex();
        vertexBuilder.vertex(pose, 0.0F, 0.0F, 0.0F).color(255, 255, 255, 128).endVertex();
    }

    private static void vertex2(IVertexBuilder vertexBuilder, Matrix4f pose, float length, float width) {
        vertexBuilder.vertex(pose, -HALF_SQRT_3 * width, length, -0.5F * width).color(255, 0, 255, 0).endVertex();
    }

    private static void vertex3(IVertexBuilder vertexBuilder, Matrix4f pose, float length, float width) {
        vertexBuilder.vertex(pose, HALF_SQRT_3 * width, length, -0.5F * width).color(255, 0, 255, 0).endVertex();
    }

    private static void vertex4(IVertexBuilder vertexBuilder, Matrix4f pose, float length, float width) {
        vertexBuilder.vertex(pose, 0.0F, length, width).color(255, 0, 255, 0).endVertex();
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull EntityAcceleratingParticle entity) {
        return PlayerContainer.BLOCK_ATLAS;
    }

    /**
     * Rendered rays extend beyond the hitbox - don't want them to be culled when outside of frustum
     */
    @Override
    public boolean shouldRender(EntityAcceleratingParticle entityAcceleratingParticle, ClippingHelper camera, double camX, double camY, double camZ) {
        return true;
    }


}
