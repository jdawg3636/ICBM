package com.jdawg3636.icbm.common.particle;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@OnlyIn(Dist.CLIENT)
public class EnderParticle extends Particle {

    public EnderParticle(ClientWorld level, double xo, double yo, double zo) {
        super(level, xo, yo, zo);
        this.hasPhysics = false;
    }

    public EnderParticle(ClientWorld level, double xo, double yo, double zo, double xd, double yd, double zd) {
        super(level, xo, yo, zo, xd, yd, zd);
        this.hasPhysics = false;
    }

    public EnderParticle(BasicParticleType basicParticleType, ClientWorld level, double xo, double yo, double zo, double xd, double yd, double zd) {
        this(level, xo, yo, zo, xd, yd, zd);
    }

//    public void renderTexturedParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks) {
//        Vector3d position = renderInfo.getPosition();
//        float lerpedX = (float)(MathHelper.lerp((double)partialTicks, this.xo, this.x) - position.x());
//        float lerpedY = (float)(MathHelper.lerp((double)partialTicks, this.yo, this.y) - position.y());
//        float lerpedZ = (float)(MathHelper.lerp((double)partialTicks, this.zo, this.z) - position.z());
//        Quaternion rotation;
//        if (this.roll == 0.0F) {
//            rotation = renderInfo.rotation();
//        } else {
//            rotation = new Quaternion(renderInfo.rotation());
//            float lvt_9_1_ = MathHelper.lerp(partialTicks, this.oRoll, this.roll);
//            rotation.mul(Vector3f.ZP.rotation(lvt_9_1_));
//        }
//
//        Vector3f UNKNOWN_VALUE = new Vector3f(-1.0F, -1.0F, 0.0F);
//        UNKNOWN_VALUE.transform(rotation);
//        Vector3f[] pointList = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
//        float quadSize = this.getQuadSize(partialTicks);
//
//        for(int i = 0; i < 4; ++i) {
//            Vector3f lvt_13_1_ = pointList[i];
//            lvt_13_1_.transform(rotation);
//            lvt_13_1_.mul(quadSize);
//            lvt_13_1_.add(lerpedX, lerpedY, lerpedZ);
//        }
//
//        float u0 = this.getU0();
//        float u1 = this.getU1();
//        float v0 = this.getV0();
//        float v1 = this.getV1();
//        int lightColor = this.getLightColor(partialTicks);
//        buffer.vertex((double)pointList[0].x(), (double)pointList[0].y(), (double)pointList[0].z()).uv(u1, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(lightColor).endVertex();
//        buffer.vertex((double)pointList[1].x(), (double)pointList[1].y(), (double)pointList[1].z()).uv(u1, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(lightColor).endVertex();
//        buffer.vertex((double)pointList[2].x(), (double)pointList[2].y(), (double)pointList[2].z()).uv(u0, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(lightColor).endVertex();
//        buffer.vertex((double)pointList[3].x(), (double)pointList[3].y(), (double)pointList[3].z()).uv(u0, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(lightColor).endVertex();
//    }

    @Override
    public void render(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks) {
        // Load the textures for end portal effects
//        ResourceLocation endSkyTexture = new ResourceLocation("textures/environment/end_sky.png");
//        ResourceLocation endPortalTexture = new ResourceLocation("textures/entity/end_portal.png");

        // Prepare rendering layers for the effect
        List<RenderType> renderTypes = IntStream.range(0, 16)
                .mapToObj(RenderType::endPortal)
                .collect(Collectors.toList());

        MatrixStack matrixStack = new MatrixStack();
        Random random = new Random(31100L);

        // Apply transformations for positioning the particle
        matrixStack.pushPose();
        Vector3d position = renderInfo.getPosition();
        matrixStack.translate(this.x - position.x, this.y - position.y, this.z - position.z);

//        Quaternion rotation;
//        if (this.roll == 0.0F) {
//            rotation = renderInfo.rotation();
//        } else {
//            rotation = new Quaternion(renderInfo.rotation());
//            float lerpedRoll = MathHelper.lerp(partialTicks, this.oRoll, this.roll);
//            rotation.mul(Vector3f.ZP.rotation(lerpedRoll));
//        }
//        matrixStack.mulPose(rotation);

        // Get the initial transformation matrix
        Matrix4f matrix4f = matrixStack.last().pose();

        // Render passes for the end portal effect
        IRenderTypeBuffer.Impl bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        for (int pass = 0; pass < renderTypes.size(); ++pass) {
            IVertexBuilder passBuffer = bufferSource.getBuffer(renderTypes.get(pass));

            // Randomize colors for each pass
            float r = (random.nextFloat() * 0.5F + 0.1F) * (2.0F / (18 - pass));
            float g = (random.nextFloat() * 0.5F + 0.4F) * (2.0F / (18 - pass));
            float b = (random.nextFloat() * 0.5F + 0.5F) * (2.0F / (18 - pass));

            // Add vertices for the particle's shape
            renderParticleShape(matrix4f, passBuffer, r, g, b);
        }

        matrixStack.popPose();
    }

    // Helper method to render the particle's quad
    private void renderParticleShape(Matrix4f matrix4f, IVertexBuilder buffer, float r, float g, float b) {
        float size = 1F; // Particle size
        float u1 = 0.0F, v1 = 0.0F, u2 = 1.0F, v2 = 1.0F;

//        Vector3f[] pointList = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
//        float quadSize = this.getQuadSize(partialTicks);
//        for(int i = 0; i < 4; ++i) {
//            Vector3f lvt_13_1_ = pointList[i];
//            lvt_13_1_.transform(rotation);
//            lvt_13_1_.mul(quadSize);
//            lvt_13_1_.add(lerpedX, lerpedY, lerpedZ);
//        }
//        buffer.vertex((double)pointList[0].x(), (double)pointList[0].y(), (double)pointList[0].z()).uv(u1, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(lightColor).endVertex();
//        buffer.vertex((double)pointList[1].x(), (double)pointList[1].y(), (double)pointList[1].z()).uv(u1, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(lightColor).endVertex();
//        buffer.vertex((double)pointList[2].x(), (double)pointList[2].y(), (double)pointList[2].z()).uv(u0, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(lightColor).endVertex();
//        buffer.vertex((double)pointList[3].x(), (double)pointList[3].y(), (double)pointList[3].z()).uv(u0, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(lightColor).endVertex();

        // Define the particle's quad
        buffer.vertex(matrix4f, -size, -size, 0.0F).color(r, g, b, 1.0F).uv(u1, v1).uv2(15728880).endVertex();
        buffer.vertex(matrix4f, size, -size, 0.0F).color(r, g, b, 1.0F).uv(u2, v1).uv2(15728880).endVertex();
        buffer.vertex(matrix4f, size, size, 0.0F).color(r, g, b, 1.0F).uv(u2, v2).uv2(15728880).endVertex();
        buffer.vertex(matrix4f, -size, size, 0.0F).color(r, g, b, 1.0F).uv(u1, v2).uv2(15728880).endVertex();
    }


    @Override
    public IParticleRenderType getRenderType() {
//        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
        return IParticleRenderType.CUSTOM;
    }

}
