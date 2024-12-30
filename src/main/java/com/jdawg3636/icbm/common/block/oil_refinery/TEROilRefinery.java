package com.jdawg3636.icbm.common.block.oil_refinery;

import com.jdawg3636.icbm.common.block.machine.AbstractBlockMachine;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.function.BiConsumer;

public class TEROilRefinery extends TileEntityRenderer<TileOilRefinery> {

    public TEROilRefinery(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
    }

    @Override
    public boolean shouldRenderOffScreen(TileOilRefinery tileEntity) {
        // TODO: this doesn't appear to be working. Need to fix to avoid overzealous frustum culling.
        return true;
    }

    @Override
    public void render(TileOilRefinery tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, int combinedLight, int combinedOverlay) {

        // Outer Push
        matrixStack.pushPose();

        // Calculate Normal and Tangent Vectors (very loose definition of "tangent" here - just need a way to translate left/right relative to the multiblock).
        final Direction facingDir = tileEntity.getBlockState().getValue(AbstractBlockMachine.FACING);
        final Vector3i normal = facingDir.getNormal();
        final int tangentSign = (facingDir.getAxis() == Direction.Axis.X) ? -1 : 1;
        final Vector3i tangent = new Vector3i(tangentSign * normal.getZ(), 0, tangentSign * normal.getX());

        // Loop over tanks
        for(int i = 0; i < tileEntity.fluidTanks.size(); ++i) {

            // Extract fluid amount and tank capacity
            final LazyOptional<FluidTank> fluidTank = tileEntity.fluidTanks.get(i);
            final FluidStack fluidStack = fluidTank.map(FluidTank::getFluid).orElse(FluidStack.EMPTY);
            final int capacity = fluidTank.map(FluidTank::getCapacity).filter(a -> (a > 0)).orElse(1);
            final double fractionFilled = Math.min(1.0D, fluidStack.getAmount() / (double)capacity);

            // Extract fluid texture coordinates and color
            FluidAttributes attributes = fluidStack.getFluid().getAttributes();
            //noinspection deprecation
            TextureAtlasSprite textureAtlasSprite = Minecraft.getInstance().getTextureAtlas(AtlasTexture.LOCATION_BLOCKS).apply(attributes.getStillTexture());
            int colorPacked = attributes.getColor(fluidStack);
            float colorAlpha = (colorPacked >> 24 & 0xFF) / 256F;
            float colorRed   = (colorPacked >> 16 & 0xFF) / 256F;
            float colorGreen = (colorPacked >>  8 & 0xFF) / 256F;
            float colorBlue  = (colorPacked >>  0 & 0xFF) / 256F;
            float uMin = textureAtlasSprite.getU0();
            float vMin = textureAtlasSprite.getV0();
            float uMax = textureAtlasSprite.getU1();
            float vMax = textureAtlasSprite.getV1();

            // Push for tank
            matrixStack.pushPose();

            // Translate up and back (same for both tanks)
            matrixStack.translate(-normal.getX(), 4.0D / 16.0D, -normal.getZ());
            // Translate left or right (direction depends on which tank)
            if(i == 0) matrixStack.translate(-tangent.getX(), 0, -tangent.getZ());
            else matrixStack.translate(tangent.getX(), 0, tangent.getZ());

            // Generate AABB of fluid given current fraction of tank filled
            AxisAlignedBB fluidAABBForTank = new AxisAlignedBB(
                    (2.0D / 16.0D) + 0.01D,
                    (0.0D / 16.0D) + 0.01D,
                    (2.0D / 16.0D) + 0.01D,
                    (14.0D / 16.0D) - 0.01D,
                    ((28.0D / 16.0D) - 0.01D) * fractionFilled,
                    (14.0D / 16.0D) - 0.01D
            );

            // Render
            IVertexBuilder vertexBuilder = renderBuffer.getBuffer(Atlases.translucentCullBlockSheet());
            Matrix4f pose = matrixStack.last().pose();
            renderTiledAABB(vertexBuilder, pose, fluidAABBForTank, colorRed, colorGreen, colorBlue, colorAlpha, uMin, uMax, vMin, vMax, combinedOverlay, combinedLight);

            // Pop for tank
            matrixStack.popPose();

        }

        // Outer Pop
        matrixStack.popPose();

    }

    // Pain, suffering, and ChatGPT.
    public void renderTiledAABB(IVertexBuilder builder, Matrix4f pose, AxisAlignedBB box, float r, float g, float b, float a, float uMin, float uMax, float vMin, float vMax, int overlay, int light) {
        float minX = (float) box.minX;
        float maxX = (float) box.maxX;
        float minY = (float) box.minY;
        float maxY = (float) box.maxY;
        float minZ = (float) box.minZ;
        float maxZ = (float) box.maxZ;

        // Helper function to render a single quad with counterclockwise winding
        BiConsumer<float[], float[]> renderQuad = (positions, normal) -> {
            builder.vertex(pose, positions[0], positions[1], positions[2]).color(r, g, b, a).uv(uMin, vMin).overlayCoords(overlay).uv2(light).normal(normal[0], normal[1], normal[2]).endVertex();
            builder.vertex(pose, positions[9], positions[10], positions[11]).color(r, g, b, a).uv(uMin, vMax).overlayCoords(overlay).uv2(light).normal(normal[0], normal[1], normal[2]).endVertex();
            builder.vertex(pose, positions[6], positions[7], positions[8]).color(r, g, b, a).uv(uMax, vMax).overlayCoords(overlay).uv2(light).normal(normal[0], normal[1], normal[2]).endVertex();
            builder.vertex(pose, positions[3], positions[4], positions[5]).color(r, g, b, a).uv(uMax, vMin).overlayCoords(overlay).uv2(light).normal(normal[0], normal[1], normal[2]).endVertex();
        };

        // Bottom and top faces
        for (float x = minX; x < maxX; x += 1.0f) {
            for (float z = minZ; z < maxZ; z += 1.0f) {
                float xEnd = Math.min(x + 1.0f, maxX);
                float zEnd = Math.min(z + 1.0f, maxZ);
                // Bottom face (counterclockwise when viewed from below)
                renderQuad.accept(new float[]{
                        x, minY, zEnd,
                        xEnd, minY, zEnd,
                        xEnd, minY, z,
                        x, minY, z
                }, new float[]{0, -1, 0});
                // Top face (counterclockwise when viewed from above)
                renderQuad.accept(new float[]{
                        x, maxY, z,
                        xEnd, maxY, z,
                        xEnd, maxY, zEnd,
                        x, maxY, zEnd
                }, new float[]{0, 1, 0});
            }
        }

        // North and South faces
        for (float x = minX; x < maxX; x += 1.0f) {
            for (float y = minY; y < maxY; y += 1.0f) {
                float xEnd = Math.min(x + 1.0f, maxX);
                float yEnd = Math.min(y + 1.0f, maxY);
                // North face (counterclockwise when viewed from the south)
                renderQuad.accept(new float[]{
                        x, y, minZ,
                        xEnd, y, minZ,
                        xEnd, yEnd, minZ,
                        x, yEnd, minZ
                }, new float[]{0, 0, -1});
                // South face (counterclockwise when viewed from the north)
                renderQuad.accept(new float[]{
                        x, yEnd, maxZ,
                        xEnd, yEnd, maxZ,
                        xEnd, y, maxZ,
                        x, y, maxZ
                }, new float[]{0, 0, 1});
            }
        }

        // East and West faces
        for (float z = minZ; z < maxZ; z += 1.0f) {
            for (float y = minY; y < maxY; y += 1.0f) {
                float zEnd = Math.min(z + 1.0f, maxZ);
                float yEnd = Math.min(y + 1.0f, maxY);
                // East face (counterclockwise when viewed from the west)
                renderQuad.accept(new float[]{
                        maxX, y, zEnd,
                        maxX, yEnd, zEnd,
                        maxX, yEnd, z,
                        maxX, y, z
                }, new float[]{1, 0, 0});
                // West face (counterclockwise when viewed from the east)
                renderQuad.accept(new float[]{
                        minX, y, z,
                        minX, yEnd, z,
                        minX, yEnd, zEnd,
                        minX, y, zEnd
                }, new float[]{-1, 0, 0});
            }
        }
    }

}
