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
import net.minecraftforge.common.util.LazyOptional;

// https://mcforge.readthedocs.io/en/1.16.x/tileentities/tesr/
public class TEREMPTower extends TileEntityRenderer<TileEMPTower> {

    public final LazyOptional<IBakedModel> MODEL_CLOCKWISE;
    public final LazyOptional<IBakedModel> MODEL_COUNTERCLOCKWISE;
    public final LazyOptional<IBakedModel> MODEL_STATIC;

    public TEREMPTower(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        this(
            tileEntityRendererDispatcher,
            LazyOptional.of(() -> Minecraft.getInstance().getModelManager().getModel(ClientProxy.MODEL_EMP_TOWER_CLOCKWISE)),
            LazyOptional.of(() -> Minecraft.getInstance().getModelManager().getModel(ClientProxy.MODEL_EMP_TOWER_COUNTERCLOCKWISE)),
            LazyOptional.of(() -> Minecraft.getInstance().getModelManager().getModel(ClientProxy.MODEL_EMP_TOWER_STATIC))
        );
    }

    public TEREMPTower(TileEntityRendererDispatcher tileEntityRendererDispatcher, LazyOptional<IBakedModel> modelClockwise, LazyOptional<IBakedModel> modelCounterclockwise, LazyOptional<IBakedModel> modelStatic) {
        super(tileEntityRendererDispatcher);
        this.MODEL_CLOCKWISE = modelClockwise;
        this.MODEL_COUNTERCLOCKWISE = modelCounterclockwise;
        this.MODEL_STATIC = modelStatic;
    }

    @Override
    public void render(TileEMPTower tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, int combinedLight, int combinedOverlay) {

        BlockState blockState = tileEntity.getBlockState();
        Direction direction = (blockState.getBlock() instanceof BlockEMPTower) ? blockState.getValue(BlockEMPTower.FACING) : null;
        float animationRadians = tileEntity.getAnimationRadians(partialTicks);

        // Outer Push, used for offsetting the entire model and rotating to account for the block's facing direction
        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.5, 0.5);
        if (direction == Direction.NORTH)       matrixStack.mulPose(new Quaternion(0,  90, 0, true));
        else if (direction == Direction.SOUTH)  matrixStack.mulPose(new Quaternion(0, 270, 0, true));
        else if (direction == Direction.WEST)   matrixStack.mulPose(new Quaternion(0, 180, 0, true));

        // Clockwise Model
        MODEL_CLOCKWISE.ifPresent(model -> {
            matrixStack.pushPose();
            matrixStack.mulPose(new Quaternion(new Vector3f(0, 1, 0), -animationRadians, false));
            Minecraft.getInstance().getItemRenderer().render(new ItemStack(Blocks.STONE), ItemCameraTransforms.TransformType.NONE, false, matrixStack, renderBuffer, combinedLight, combinedOverlay, model);
            matrixStack.popPose();
        });

        // Counterclockwise Model
        MODEL_COUNTERCLOCKWISE.ifPresent(model -> {
            matrixStack.pushPose();
            matrixStack.mulPose(new Quaternion(new Vector3f(0, 1, 0), animationRadians, false));
            Minecraft.getInstance().getItemRenderer().render(new ItemStack(Blocks.STONE), ItemCameraTransforms.TransformType.NONE, false, matrixStack, renderBuffer, combinedLight, combinedOverlay, model);
            matrixStack.popPose();
        });

        // Static Model
        MODEL_STATIC.ifPresent(model -> Minecraft.getInstance().getItemRenderer().render(new ItemStack(Blocks.STONE), ItemCameraTransforms.TransformType.NONE, false, matrixStack, renderBuffer, combinedLight, combinedOverlay, model));

        // Outer Pop
        matrixStack.popPose();

    }

}
