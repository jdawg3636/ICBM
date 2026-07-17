package com.jdawg3636.icbm.common.block.thermochemical_mixer;

import com.jdawg3636.icbm.common.block.machine.AbstractBlockMachine;
import com.jdawg3636.icbm.common.block.machine.AbstractTERMachine;
import com.jdawg3636.icbm.common.listener.ClientProxy;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.common.util.LazyOptional;

public class TERThermochemicalMixer extends AbstractTERMachine<TileThermochemicalMixer> {

    public final LazyOptional<IBakedModel> MODEL_STATIC;

    public TERThermochemicalMixer(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        this(
                tileEntityRendererDispatcher,
                LazyOptional.of(() -> Minecraft.getInstance().getModelManager().getModel(ClientProxy.MODEL_THERMOCHEMICAL_MIXER))
        );
    }

    public TERThermochemicalMixer(TileEntityRendererDispatcher tileEntityRendererDispatcher, LazyOptional<IBakedModel> modelStatic) {
        super(tileEntityRendererDispatcher);
        this.MODEL_STATIC = modelStatic;
    }

    @Override
    public void render(TileThermochemicalMixer tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, int combinedLight, int combinedOverlay) {

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
        if (facingDir == Direction.WEST)   matrixStack.mulPose(new Quaternion(0, 270, 0, true));
        else if (facingDir == Direction.NORTH)  matrixStack.mulPose(new Quaternion(0, 180, 0, true));
        else if (facingDir == Direction.EAST)   matrixStack.mulPose(new Quaternion(0,  90, 0, true));
        MODEL_STATIC.ifPresent(model -> {
            Minecraft.getInstance().getItemRenderer().render(Blocks.STONE.asItem().getDefaultInstance(), ItemCameraTransforms.TransformType.NONE, false, matrixStack, renderBuffer, combinedLight, combinedOverlay, model);
        });
        matrixStack.popPose();

//        // Render liquid in chamber
//        renderFluidTank(tileEntity.fluidTanks.get(0), UNADJUSTED_FLUID_AABB, matrixStack, renderBuffer, combinedLight, combinedOverlay);
//
//        // Render items in chamber
//        ItemStack itemStack = tileEntity.itemHandlerLazyOptional.map(ish -> ish.getStackInSlot(TileMetallurgicLeachingChamber.MachineSlots.ITEM_INPUT.ordinal())).orElse(ItemStack.EMPTY);
//        Random seededRandom = new Random(321);
//        Random seededRandomForYAxis = new Random(248);
//        for(int i = 0; i < Math.ceil(itemStack.getCount() / 8.0); ++i) {
//            matrixStack.pushPose();
//            // Translate to center
//            matrixStack.translate(0.5, 1.5 - (6.0 / 16.0), 0.5);
//            // Rotate for Facing Direction
//            matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - facingDir.toYRot()));
//            // Scale
//            matrixStack.scale(0.25F, 0.25F, 0.25F);
//            // Random Translation
//            double offsetToBack = ((64 * seededRandom.nextDouble() - 40.0) / 16.0);
//            double offsetToLeft = ((32 * seededRandom.nextDouble() - 15.0) / 16.0);
//            double offsetUp = 2.5 * seededRandomForYAxis.nextDouble() * tileEntity.fluidTanks.get(0).map(ft -> Math.min(1.0, tileEntity.inputTankAmountClientLerped / (double)ft.getCapacity())).orElse(0.0);
//            matrixStack.translate(offsetToLeft, offsetUp, offsetToBack);
//            // Random Rotation +/- 30 degrees
//            matrixStack.mulPose(new Quaternion(60 * seededRandom.nextFloat() - 30, 60 * seededRandom.nextFloat() - 30, 60 * seededRandom.nextFloat() - 30, true));
//            // Render
//            Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemCameraTransforms.TransformType.FIXED, combinedLight, OverlayTexture.NO_OVERLAY, matrixStack, renderBuffer);
//            matrixStack.popPose();
//        }

        // Outer Pop
        matrixStack.popPose();

    }

}
