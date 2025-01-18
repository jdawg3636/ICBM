package com.jdawg3636.icbm.common.block.oil_refinery;

import com.jdawg3636.icbm.common.block.machine.AbstractBlockMachine;
import com.jdawg3636.icbm.common.listener.ClientProxy;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class TEROilRefinery extends TileEntityRenderer<TileOilRefinery> {

    public final LazyOptional<IBakedModel> MODEL_STATIC;

    public TEROilRefinery(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        this(
                tileEntityRendererDispatcher,
                LazyOptional.of(() -> Minecraft.getInstance().getModelManager().getModel(ClientProxy.MODEL_OIL_REFINERY))
        );
    }

    public TEROilRefinery(TileEntityRendererDispatcher tileEntityRendererDispatcher, LazyOptional<IBakedModel> modelStatic) {
        super(tileEntityRendererDispatcher);
        this.MODEL_STATIC = modelStatic;
    }

    @Override
    public boolean shouldRenderOffScreen(TileOilRefinery tileEntity) {
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

        // Render the static model
        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.5, 0.5);
        if (facingDir == Direction.EAST)   matrixStack.mulPose(new Quaternion(0, 270, 0, true));
        else if (facingDir == Direction.SOUTH)  matrixStack.mulPose(new Quaternion(0, 180, 0, true));
        else if (facingDir == Direction.WEST)   matrixStack.mulPose(new Quaternion(0,  90, 0, true));
        MODEL_STATIC.ifPresent(model -> Minecraft.getInstance().getItemRenderer().render(Blocks.STONE.asItem().getDefaultInstance(), ItemCameraTransforms.TransformType.NONE, false, matrixStack, renderBuffer, combinedLight, combinedOverlay, model));
        matrixStack.popPose();

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
            float colorAlpha = (colorPacked >> 24 & 0xFF) / 255F;
            float colorRed   = (colorPacked >> 16 & 0xFF) / 255F;
            float colorGreen = (colorPacked >>  8 & 0xFF) / 255F;
            float colorBlue  = (colorPacked >>  0 & 0xFF) / 255F;
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
            renderTiledAABB(vertexBuilder, pose, fluidAABBForTank, colorRed, colorGreen, colorBlue, colorAlpha, uMin, uMax, vMin, vMax, combinedLight, combinedOverlay);

            // Pop for tank
            matrixStack.popPose();

        }

        // Outer Pop
        matrixStack.popPose();

    }

    // Helper function to render a single quad with counterclockwise winding
    public static void renderQuad(float[] positions, float[] normal, float width, float height, IVertexBuilder builder, Matrix4f pose, float r, float g, float b, float a, float uMin, float uMax, float vMin, float vMax, int combinedLight, int combinedOverlay) {
        // Adjust UVs to match scale of width/height. Width will crop to center, height will crop to bottom.
        // NOTE: U is height and V is width in the context do to wanting fluid texture to be rotated on sides
        final float deltaU = uMax - uMin;
        final float deltaV = vMax - vMin;
        final float adjustmentU = deltaU * (1 - height);
        final float adjustmentV = deltaV * (1 - width);
        uMax -= adjustmentU;
        vMin += (adjustmentV / 2F);
        vMax -= (adjustmentV / 2F);
        // Build Vertices
        builder.vertex(pose, positions[0], positions[1], positions[2]).color(r, g, b, a).uv(uMin, vMin).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal[0], normal[1], normal[2]).endVertex();
        builder.vertex(pose, positions[9], positions[10], positions[11]).color(r, g, b, a).uv(uMin, vMax).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal[0], normal[1], normal[2]).endVertex();
        builder.vertex(pose, positions[6], positions[7], positions[8]).color(r, g, b, a).uv(uMax, vMax).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal[0], normal[1], normal[2]).endVertex();
        builder.vertex(pose, positions[3], positions[4], positions[5]).color(r, g, b, a).uv(uMax, vMin).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal[0], normal[1], normal[2]).endVertex();
    };

    // Pain, suffering, and ChatGPT.
    public static void renderTiledAABB(IVertexBuilder builder, Matrix4f pose, AxisAlignedBB box, float r, float g, float b, float a, float uMin, float uMax, float vMin, float vMax, int combinedLight, int combinedOverlay) {

        // Pre-cast values from AABB
        final float minX = (float) box.minX;
        final float maxX = (float) box.maxX;
        final float minY = (float) box.minY;
        final float maxY = (float) box.maxY;
        final float minZ = (float) box.minZ;
        final float maxZ = (float) box.maxZ;

        // Pre-calculate deltas for use as width/heights
        final float deltaX = maxX - minX;
        final float deltaY = maxY - minY;
        final float deltaZ = maxZ - minZ;

        // Bottom and top faces
        for (float x = minX; x < maxX; x += 1.0f) {
            for (float z = minZ; z < maxZ; z += 1.0f) {
                float xEnd = Math.min(x + 1.0f, maxX);
                float zEnd = Math.min(z + 1.0f, maxZ);
                // Bottom face (counterclockwise when viewed from below)
                renderQuad(
                    new float[] {
                        x, minY, zEnd,
                        xEnd, minY, zEnd,
                        xEnd, minY, z,
                        x, minY, z
                    },
                    new float[] {0, -1, 0},
                    xEnd - x, zEnd - z,
                    builder, pose, r, g, b, a, uMin, uMax, vMin, vMax, combinedLight, combinedOverlay
                );
                // Top face (counterclockwise when viewed from above)
                renderQuad(
                    new float[]{
                        x, maxY, z,
                        xEnd, maxY, z,
                        xEnd, maxY, zEnd,
                        x, maxY, zEnd
                    },
                    new float[] {0, 1, 0},
                    xEnd - x, zEnd - z,
                    builder, pose, r, g, b, a, uMin, uMax, vMin, vMax, combinedLight, combinedOverlay
                );
            }
        }

        // North and South faces
        for (float x = minX; x < maxX; x += 1.0f) {
            for (float y = minY; y < maxY; y += 1.0f) {
                float xEnd = Math.min(x + 1.0f, maxX);
                float yEnd = Math.min(y + 1.0f, maxY);
                // North face (counterclockwise when viewed from the south)
                renderQuad(
                    new float[]{
                        xEnd, y, minZ,
                        xEnd, yEnd, minZ,
                        x, yEnd, minZ,
                        x, y, minZ
                    },
                    new float[]{0, 0, -1},
                    xEnd - x, yEnd - y,
                    builder, pose, r, g, b, a, uMin, uMax, vMin, vMax, combinedLight, combinedOverlay
                );
                // South face (counterclockwise when viewed from the north)
                renderQuad(
                    new float[]{
                        x, y, maxZ,
                        x, yEnd, maxZ,
                        xEnd, yEnd, maxZ,
                        xEnd, y, maxZ
                    },
                    new float[]{0, 0, 1},
                    xEnd - x, yEnd - y,
                    builder, pose, r, g, b, a, uMin, uMax, vMin, vMax, combinedLight, combinedOverlay
                );
            }
        }

        // East and West faces
        for (float z = minZ; z < maxZ; z += 1.0f) {
            for (float y = minY; y < maxY; y += 1.0f) {
                float zEnd = Math.min(z + 1.0f, maxZ);
                float yEnd = Math.min(y + 1.0f, maxY);
                // East face (counterclockwise when viewed from the west)
                renderQuad(
                    new float[]{
                        maxX, y, zEnd,
                        maxX, yEnd, zEnd,
                        maxX, yEnd, z,
                        maxX, y, z
                    },
                    new float[]{1, 0, 0},
                    zEnd - z, yEnd - y,
                    builder, pose, r, g, b, a, uMin, uMax, vMin, vMax, combinedLight, combinedOverlay
                );
                // West face (counterclockwise when viewed from the east)
                renderQuad(
                    new float[]{
                        minX, y, z,
                        minX, yEnd, z,
                        minX, yEnd, zEnd,
                        minX, y, zEnd
                    },
                    new float[]{-1, 0, 0},
                    zEnd - z, yEnd - y,
                    builder, pose, r, g, b, a, uMin, uMax, vMin, vMax, combinedLight, combinedOverlay
                );
            }
        }
    }

}
