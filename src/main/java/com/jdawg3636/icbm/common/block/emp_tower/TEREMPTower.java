package com.jdawg3636.icbm.common.block.emp_tower;

import com.jdawg3636.icbm.common.listener.ClientProxy;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

// https://mcforge.readthedocs.io/en/1.16.x/tileentities/tesr/
public class TEREMPTower extends TileEntityRenderer<TileEMPTower> {

    public TEREMPTower(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
    }

    @Override
    public void render(TileEMPTower tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, int combinedLight, int combinedOverlay) {

        IBakedModel MODEL_CLOCKWISE        = Minecraft.getInstance().getModelManager().getModel(ClientProxy.MODEL_EMP_TOWER_CLOCKWISE);
        IBakedModel MODEL_COUNTERCLOCKWISE = Minecraft.getInstance().getModelManager().getModel(ClientProxy.MODEL_EMP_TOWER_COUNTERCLOCKWISE);
        IBakedModel MODEL_STATIC           = Minecraft.getInstance().getModelManager().getModel(ClientProxy.MODEL_EMP_TOWER_STATIC);

        BlockState blockState = tileEntity.getBlockState();
        Direction direction = (blockState.getBlock() instanceof BlockEMPTower) ? blockState.getValue(BlockEMPTower.FACING) : null;
        float animationRadians = ((float)(tileEntity.getAnimationRadians() + (partialTicks * 0.05 * 2 * Math.PI)));

        // Outer Push, used for offsetting the entire model and rotating to account for the block's facing direction
        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.5, 0.5);
        if (direction == Direction.NORTH)       matrixStack.mulPose(new Quaternion(0,  90, 0, true));
        else if (direction == Direction.SOUTH)  matrixStack.mulPose(new Quaternion(0, 270, 0, true));
        else if (direction == Direction.WEST)   matrixStack.mulPose(new Quaternion(0, 180, 0, true));

        // Clockwise Model
        matrixStack.pushPose();
        matrixStack.mulPose(new Quaternion(new Vector3f(0, 1, 0), -animationRadians, false));
        Minecraft.getInstance().getItemRenderer().render(new ItemStack(Blocks.STONE), ItemCameraTransforms.TransformType.NONE, false, matrixStack, renderBuffer, combinedLight, combinedOverlay, MODEL_CLOCKWISE);
        matrixStack.popPose();

        // Counterclockwise Model
        matrixStack.pushPose();
        matrixStack.mulPose(new Quaternion(new Vector3f(0, 1, 0), animationRadians, false));
        Minecraft.getInstance().getItemRenderer().render(new ItemStack(Blocks.STONE), ItemCameraTransforms.TransformType.NONE, false, matrixStack, renderBuffer, combinedLight, combinedOverlay, MODEL_COUNTERCLOCKWISE);
        matrixStack.popPose();

        // Static Model
        Minecraft.getInstance().getItemRenderer().render(new ItemStack(Blocks.STONE), ItemCameraTransforms.TransformType.NONE, false, matrixStack, renderBuffer, combinedLight, combinedOverlay, MODEL_STATIC);

        // Outer Pop
        matrixStack.popPose();

    }

}
