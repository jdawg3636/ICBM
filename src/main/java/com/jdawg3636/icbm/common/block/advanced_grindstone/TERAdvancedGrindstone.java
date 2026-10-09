package com.jdawg3636.icbm.common.block.advanced_grindstone;

import com.jdawg3636.icbm.common.block.machine.AbstractBlockMachine;
import com.jdawg3636.icbm.common.block.machine.AbstractTERMachine;
import com.jdawg3636.icbm.common.listener.ClientProxy;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.common.util.LazyOptional;

public class TERAdvancedGrindstone extends AbstractTERMachine<TileAdvancedGrindstone> {
    private static final float DEGREES_PER_INTERACTION = 90.0F;
    private static final double DISH_CONTENT_HEIGHT = 10.6D / 16.0D - 0.5D;
    private static final float ITEM_SCALE = 0.18F;
    private static final float[][] INPUT_ITEM_TRANSFORMS = {
            {-0.24F, -0.20F, 17.0F},
            { 0.13F, -0.27F, -31.0F},
            {-0.08F,  0.18F, 68.0F},
            { 0.27F,  0.09F, -74.0F}
    };
    private static final float[][] OUTPUT_ITEM_TRANSFORMS = {
            { 0.23F, -0.12F, 39.0F},
            {-0.19F,  0.07F, -58.0F},
            { 0.06F,  0.27F, 103.0F},
            {-0.28F,  0.25F, -12.0F}
    };
    private final LazyOptional<IBakedModel> staticModel;
    private final LazyOptional<IBakedModel> dynamicModel;

    public TERAdvancedGrindstone(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
        staticModel = LazyOptional.of(() -> Minecraft.getInstance().getModelManager().getModel(ClientProxy.MODEL_ADVANCED_GRINDSTONE_STATIC));
        dynamicModel = LazyOptional.of(() -> Minecraft.getInstance().getModelManager().getModel(ClientProxy.MODEL_ADVANCED_GRINDSTONE_DYNAMIC));
    }

    @Override public void render(TileAdvancedGrindstone tile, float partialTicks, MatrixStack matrices, IRenderTypeBuffer buffer, int light, int overlay) {
        matrices.pushPose();
        matrices.translate(.5, .5, .5);
        Direction facing = tile.getBlockState().getValue(AbstractBlockMachine.FACING);
        if (facing == Direction.EAST) matrices.mulPose(new Quaternion(0, 270, 0, true));
        else if (facing == Direction.SOUTH) matrices.mulPose(new Quaternion(0, 180, 0, true));
        else if (facing == Direction.WEST) matrices.mulPose(new Quaternion(0, 90, 0, true));

        staticModel.ifPresent(m -> Minecraft.getInstance().getItemRenderer().render(Blocks.STONE.asItem().getDefaultInstance(), ItemCameraTransforms.TransformType.NONE, false, matrices, buffer, light, overlay, m));

        ItemStack abrasive = tile.itemHandlerLazyOptional.map(handler -> handler.getStackInSlot(TileAdvancedGrindstone.MachineSlots.ABRASIVE.ordinal())).orElse(ItemStack.EMPTY);
        renderAbrasiveLayer(abrasive, tile, matrices, buffer, light, overlay);

        ItemStack input = tile.itemHandlerLazyOptional.map(handler -> handler.getStackInSlot(TileAdvancedGrindstone.MachineSlots.INPUT.ordinal())).orElse(ItemStack.EMPTY);
        ItemStack output = tile.itemHandlerLazyOptional.map(handler -> handler.getStackInSlot(TileAdvancedGrindstone.MachineSlots.OUTPUT.ordinal())).orElse(ItemStack.EMPTY);
        renderScatteredItems(input, INPUT_ITEM_TRANSFORMS, matrices, buffer, light);
        renderScatteredItems(output, OUTPUT_ITEM_TRANSFORMS, matrices, buffer, light);

        matrices.pushPose();
        matrices.mulPose(new Quaternion(0, -tile.getClientSpinProgress(partialTicks) * DEGREES_PER_INTERACTION, 0, true));
        dynamicModel.ifPresent(m -> Minecraft.getInstance().getItemRenderer().render(Blocks.STONE.asItem().getDefaultInstance(), ItemCameraTransforms.TransformType.NONE, false, matrices, buffer, light, overlay, m));
        matrices.popPose();

        matrices.popPose();
    }

    private static void renderAbrasiveLayer(ItemStack abrasive, TileAdvancedGrindstone tile, MatrixStack matrices, IRenderTypeBuffer buffer, int light, int overlay) {
        if (abrasive.isEmpty()) return;

        Minecraft minecraft = Minecraft.getInstance();
        IBakedModel itemModel = minecraft.getItemRenderer().getModel(abrasive, tile.getLevel(), null);
        TextureAtlasSprite sprite = itemModel.getParticleIcon();
        int packedColor = minecraft.getItemColors().getColor(abrasive, 0);
        float red = packedColor == -1 ? 1.0F : ((packedColor >> 16) & 0xFF) / 255.0F;
        float green = packedColor == -1 ? 1.0F : ((packedColor >> 8) & 0xFF) / 255.0F;
        float blue = packedColor == -1 ? 1.0F : (packedColor & 0xFF) / 255.0F;

        float min = -6.0F / 16.0F;
        float max = 6.0F / 16.0F;
        float y = (float) DISH_CONTENT_HEIGHT;
        float minU = sprite.getU(2.0D);
        float maxU = sprite.getU(14.0D);
        float minV = sprite.getV(2.0D);
        float maxV = sprite.getV(14.0D);
        Matrix4f pose = matrices.last().pose();
        IVertexBuilder vertices = buffer.getBuffer(Atlases.cutoutBlockSheet());

        vertices.vertex(pose, min, y, min).color(red, green, blue, 1.0F).uv(minU, minV).overlayCoords(overlay).uv2(light).normal(matrices.last().normal(), 0, 1, 0).endVertex();
        vertices.vertex(pose, min, y, max).color(red, green, blue, 1.0F).uv(minU, maxV).overlayCoords(overlay).uv2(light).normal(matrices.last().normal(), 0, 1, 0).endVertex();
        vertices.vertex(pose, max, y, max).color(red, green, blue, 1.0F).uv(maxU, maxV).overlayCoords(overlay).uv2(light).normal(matrices.last().normal(), 0, 1, 0).endVertex();
        vertices.vertex(pose, max, y, min).color(red, green, blue, 1.0F).uv(maxU, minV).overlayCoords(overlay).uv2(light).normal(matrices.last().normal(), 0, 1, 0).endVertex();
    }

    private static void renderScatteredItems(ItemStack stack, float[][] transforms, MatrixStack matrices, IRenderTypeBuffer buffer, int light) {
        int renderedCount = Math.min(transforms.length, stack.getCount());
        for (int i = 0; i < renderedCount; ++i) {
            float[] transform = transforms[i];
            matrices.pushPose();
            matrices.translate(transform[0], DISH_CONTENT_HEIGHT, transform[1]);
            matrices.mulPose(Vector3f.YP.rotationDegrees(transform[2]));
            matrices.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            matrices.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemCameraTransforms.TransformType.FIXED, light, OverlayTexture.NO_OVERLAY, matrices, buffer);
            matrices.popPose();
        }
    }
}
