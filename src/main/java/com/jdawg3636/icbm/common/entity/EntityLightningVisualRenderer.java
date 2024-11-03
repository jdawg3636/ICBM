package com.jdawg3636.icbm.common.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

import java.util.Random;

public class EntityLightningVisualRenderer extends EntityRenderer<EntityLightningVisual> {

    public EntityLightningVisualRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager);
    }

    @Override
    public void render(EntityLightningVisual entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight) {

        matrixStack.pushPose();

        // Apply rotation specified by entity
        matrixStack.mulPose(new Quaternion(new Vector3f(0, 1, 0), (float)entity.getYawRadians(), false));
        matrixStack.mulPose(new Quaternion(new Vector3f(1, 0, 0), (float)(entity.getPitchRadians()), false));

        float[] segmentXValues = new float[entity.getSegmentCount()];
        float[] segmentZValues = new float[entity.getSegmentCount()];
        float latestSegmentX = 0.0F;
        float latestSegmentZ = 0.0F;
        Random randomPerFrame = new Random(entity.seed);

        for(int i = entity.getSegmentCount() - 1; i >= 0; --i) {
            segmentXValues[i] = latestSegmentX;
            segmentZValues[i] = latestSegmentZ;
            latestSegmentX = (float)(randomPerFrame.nextInt(11) - 5);
            latestSegmentZ = (float)(randomPerFrame.nextInt(11) - 5);
        }
        latestSegmentX = 0.0F;
        latestSegmentZ = 0.0F;
        segmentXValues[0] = latestSegmentX;
        segmentZValues[0] = latestSegmentZ;

        IVertexBuilder vertexBuilder = renderTypeBuffer.getBuffer(RenderType.lightning());
        Matrix4f lastPose = matrixStack.last().pose();

        for(int concentricLayerIdx = 0; concentricLayerIdx < 4; ++concentricLayerIdx) {
            Random randomPerConcentricLayer = new Random(entity.seed);

            for(int forkIdx = 0; forkIdx < entity.getSegmentCount() / 2; ++forkIdx) {
                int highestSegmentYIdx = entity.getSegmentCount() - 1;
                int lowestSegmentYIdx = 0;
                if (forkIdx > 0) {
                    highestSegmentYIdx = entity.getSegmentCount() - 1 - forkIdx;
                }

                if (forkIdx > 0) {
                    lowestSegmentYIdx = highestSegmentYIdx - 2;
                }

                float segmentXValue = segmentXValues[highestSegmentYIdx] - latestSegmentX;
                float segmentZValue = segmentZValues[highestSegmentYIdx] - latestSegmentZ;

                for(int segmentYIdx = highestSegmentYIdx; segmentYIdx >= lowestSegmentYIdx; --segmentYIdx) {
                    float segmentXValueOriginal = segmentXValue;
                    float segmentZValueOriginal = segmentZValue;
                    if (forkIdx == 0) {
                        if(segmentYIdx == lowestSegmentYIdx) {
                            segmentXValue = 0;
                            segmentZValue = 0;
                        } else {
                            segmentXValue += (float) (randomPerConcentricLayer.nextInt(11) - 5);
                            segmentZValue += (float) (randomPerConcentricLayer.nextInt(11) - 5);
                        }
                    } else {
                        segmentXValue = (float)(randomPerConcentricLayer.nextInt(31) - 15);
                        segmentZValue = (float)(randomPerConcentricLayer.nextInt(31) - 15);
                    }

                    float segmentWidthTop = 0.1F + (float)concentricLayerIdx * 0.2F;
                    if (forkIdx == 0) {
                        segmentWidthTop = (float)((double)segmentWidthTop * ((double)segmentYIdx * 0.1 + 1.0));
                    }

                    float segmentWidthBottom = 0.1F + (float)concentricLayerIdx * 0.2F;
                    if (forkIdx == 0) {
                        segmentWidthBottom *= (float)(segmentYIdx - 1) * 0.1F + 1.0F;
                    }

                    //fn(lastPose, vertexBuilder, xBase1,        zBase1,        yBase,       xBase2,                zBase2,                colorRed,       colorGreen,       colorBlue,     xzOffset2,        xzOffset1,
                    quad(lastPose, vertexBuilder, segmentXValue, segmentZValue, segmentYIdx, segmentXValueOriginal, segmentZValueOriginal, 0.45F, 0.45F, 0.5F, segmentWidthTop, segmentWidthBottom, false, false, true, false); // north
                    quad(lastPose, vertexBuilder, segmentXValue, segmentZValue, segmentYIdx, segmentXValueOriginal, segmentZValueOriginal, 0.45F, 0.45F, 0.5F, segmentWidthTop, segmentWidthBottom, true, false, true, true);   // east
                    quad(lastPose, vertexBuilder, segmentXValue, segmentZValue, segmentYIdx, segmentXValueOriginal, segmentZValueOriginal, 0.45F, 0.45F, 0.5F, segmentWidthTop, segmentWidthBottom, true, true, false, true);   // south
                    quad(lastPose, vertexBuilder, segmentXValue, segmentZValue, segmentYIdx, segmentXValueOriginal, segmentZValueOriginal, 0.45F, 0.45F, 0.5F, segmentWidthTop, segmentWidthBottom, false, true, false, false); // west
                }
            }
        }

        matrixStack.popPose();

    }

    private static void quad(Matrix4f pose, IVertexBuilder vertexBuilder, float xBase1, float zBase1, int yBase, float xBase2, float zBase2, float colorRed, float colorGreen, float colorBlue, float xzOffset2, float xzOffset1, boolean xFlag1, boolean zFlag1, boolean xFlag2, boolean zFlag2) {
        vertexBuilder.vertex(pose, xBase1 + (xFlag1 ? xzOffset1 : -xzOffset1), (float)(yBase * 16), zBase1 + (zFlag1 ? xzOffset1 : -xzOffset1)).color(colorRed, colorGreen, colorBlue, 0.3F).endVertex();
        vertexBuilder.vertex(pose, xBase2 + (xFlag1 ? xzOffset2 : -xzOffset2), (float)((yBase + 1) * 16), zBase2 + (zFlag1 ? xzOffset2 : -xzOffset2)).color(colorRed, colorGreen, colorBlue, 0.3F).endVertex();
        vertexBuilder.vertex(pose, xBase2 + (xFlag2 ? xzOffset2 : -xzOffset2), (float)((yBase + 1) * 16), zBase2 + (zFlag2 ? xzOffset2 : -xzOffset2)).color(colorRed, colorGreen, colorBlue, 0.3F).endVertex();
        vertexBuilder.vertex(pose, xBase1 + (xFlag2 ? xzOffset1 : -xzOffset1), (float)(yBase * 16), zBase1 + (zFlag2 ? xzOffset1 : -xzOffset1)).color(colorRed, colorGreen, colorBlue, 0.3F).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityLightningVisual entity) {
        return AtlasTexture.LOCATION_BLOCKS;
    }

}
