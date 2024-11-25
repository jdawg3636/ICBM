package com.jdawg3636.icbm.common.particle;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@OnlyIn(Dist.CLIENT)
public class EnderParticle extends Particle {

    private float quadSizeX;
    private float quadSizeY;

    public EnderParticle(BasicParticleType basicParticleType, ClientWorld level, double xo, double yo, double zo, double xd, double yd, double zd) {
        super(level, xo, yo, zo, xd, yd, zd);
        this.hasPhysics = false;
        this.lifetime *= 2;
        Supplier<Float> quadSizeRNG = () -> 4F * (this.random.nextFloat() * 0.5F + 0.5F);
        quadSizeX = quadSizeRNG.get();
        quadSizeY = quadSizeRNG.get();
    }

    @Override
    public void render(IVertexBuilder ignored, ActiveRenderInfo renderInfo, float partialTicks) {

        // Seed RNG
        Random random = new Random(31100L);

        // Extract pose from parameters
        Vector3d position = renderInfo.getPosition();
        Quaternion rotation = renderInfo.rotation();

        // Get list of all 16 End Portal RenderTypes
        List<RenderType> renderTypes =
            IntStream.range(1, 17)
            .mapToObj(RenderType::endPortal)
            .collect(Collectors.toList());

        // Disable lighting
        Minecraft.getInstance().gameRenderer.lightTexture().turnOffLightLayer();

        // Render passes for the end portal effect
        for (int pass = 0; pass < renderTypes.size(); ++pass) {
            // Get applicable buffer for this pass
            IVertexBuilder buffer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(renderTypes.get(pass));
            // Randomize colors for each pass
            float r = (random.nextFloat() * 0.5F + 0.1F) * (2.0F / (18 - pass));
            float g = (random.nextFloat() * 0.5F + 0.4F) * (2.0F / (18 - pass));
            float b = (random.nextFloat() * 0.5F + 0.5F) * (2.0F / (18 - pass));
            // Render Quad
            renderTransformedQuad(buffer, (float)(this.x - position.x), (float)(this.y - position.y), (float)(this.z - position.z), rotation, r, g, b);
        }

        // Enable lighting
        Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();

    }

    private void renderTransformedQuad(IVertexBuilder buffer, float x, float y, float z, Quaternion rotation, float r, float g, float b) {
        // Instantiate Vertex Coordinates
        Vector3f[] vertexCoordinates = new Vector3f[] {
            new Vector3f(-1.0F, -1.0F, 0.0F),
            new Vector3f(-1.0F,  1.0F, 0.0F),
            new Vector3f( 1.0F,  1.0F, 0.0F),
            new Vector3f( 1.0F, -1.0F, 0.0F)
        };
        // Transform vertices
        for(Vector3f coordinate : vertexCoordinates) {
            coordinate.mul(quadSizeX, quadSizeY, 0);
            coordinate.mul((1F - (this.age / (float)this.lifetime)) / 2F + 0.5F);
            coordinate.transform(rotation);
            coordinate.add(x, y, z);
        }
        // Write vertices to buffer
        float u1 = 0.0F, v1 = 0.0F, u2 = 1.0F, v2 = 1.0F;
        buffer.vertex(vertexCoordinates[0].x(), vertexCoordinates[0].y(), vertexCoordinates[0].z()).uv(u2, v2).uv2(getLightColor(0)).color(r, g, b, 1.0F).endVertex();
        buffer.vertex(vertexCoordinates[1].x(), vertexCoordinates[1].y(), vertexCoordinates[1].z()).uv(u2, v1).uv2(getLightColor(0)).color(r, g, b, 1.0F).endVertex();
        buffer.vertex(vertexCoordinates[2].x(), vertexCoordinates[2].y(), vertexCoordinates[2].z()).uv(u1, v1).uv2(getLightColor(0)).color(r, g, b, 1.0F).endVertex();
        buffer.vertex(vertexCoordinates[3].x(), vertexCoordinates[3].y(), vertexCoordinates[3].z()).uv(u1, v2).uv2(getLightColor(0)).color(r, g, b, 1.0F).endVertex();
    }

    @Override
    public int getLightColor(float partialTicks) {
        return 15728880;
    }

    @Override
    public boolean shouldCull() {
        return false;
    }

    @Nonnull
    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.CUSTOM;
    }

}
