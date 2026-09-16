package com.jdawg3636.icbm.common.block.centrifugal_deposition_unit;

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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class TERCentrifugalDepositionUnit extends AbstractTERMachine<TileCentrifugalDepositionUnit> {

    private static final AxisAlignedBB SOUTH_BOTTLE_FLUID_AABB = new AxisAlignedBB(0, 7, 8, 8, 16, 16);
    private static final AxisAlignedBB NORTH_BOTTLE_FLUID_AABB = new AxisAlignedBB(0, 7, -8, 8, 16, 0);
    private static final AxisAlignedBB EAST_BOTTLE_FLUID_AABB = new AxisAlignedBB(8, 7, 0, 16, 16, 8);
    private static final AxisAlignedBB WEST_BOTTLE_FLUID_AABB = new AxisAlignedBB(-8, 7, 0, 0, 16, 8);

    public final LazyOptional<IBakedModel> MODEL_STATIC;
    public final LazyOptional<IBakedModel> MODEL_SPINNING;

    public TERCentrifugalDepositionUnit(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        this(
                tileEntityRendererDispatcher,
                LazyOptional.of(() -> Minecraft.getInstance().getModelManager().getModel(ClientProxy.MODEL_CENTRIFUGAL_DEPOSITION_UNIT_STATIC)),
                LazyOptional.of(() -> Minecraft.getInstance().getModelManager().getModel(ClientProxy.MODEL_CENTRIFUGAL_DEPOSITION_UNIT_SPINNING))
        );
    }

    public TERCentrifugalDepositionUnit(TileEntityRendererDispatcher tileEntityRendererDispatcher, LazyOptional<IBakedModel> modelStatic, LazyOptional<IBakedModel> modelSpinning) {
        super(tileEntityRendererDispatcher);
        this.MODEL_STATIC = modelStatic;
        this.MODEL_SPINNING = modelSpinning;
    }

    @Override
    public void render(TileCentrifugalDepositionUnit tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, int combinedLight, int combinedOverlay) {

        // Outer Push
        matrixStack.pushPose();

        final Direction facingDir = tileEntity.getBlockState().getValue(AbstractBlockMachine.FACING);

        // Render the static model
        matrixStack.pushPose();
        matrixStack.translate(0.5, 1.5, 0.5);
        if (facingDir == Direction.WEST)   matrixStack.mulPose(new Quaternion(0, 270, 0, true));
        else if (facingDir == Direction.NORTH)  matrixStack.mulPose(new Quaternion(0, 180, 0, true));
        else if (facingDir == Direction.EAST)   matrixStack.mulPose(new Quaternion(0,  90, 0, true));
        MODEL_STATIC.ifPresent(model -> Minecraft.getInstance().getItemRenderer().render(Blocks.STONE.asItem().getDefaultInstance(), ItemCameraTransforms.TransformType.NONE, false, matrixStack, renderBuffer, combinedLight, combinedOverlay, model));
        matrixStack.popPose();

        // Render the spinning portion of the model. Note that the center-column pivot is not the center of the multiblock.
        matrixStack.pushPose();
        applySpinnyBitTransform(matrixStack, facingDir, getSpinnyBitRotationDegrees(tileEntity, partialTicks));
        matrixStack.translate(0.5D, 0.5D, 0.5D);
        MODEL_SPINNING.ifPresent(model -> Minecraft.getInstance().getItemRenderer().render(Blocks.STONE.asItem().getDefaultInstance(), ItemCameraTransforms.TransformType.NONE, false, matrixStack, renderBuffer, combinedLight, combinedOverlay, model));
        matrixStack.popPose();

        // Render tile's fluid tank contents as liquid in the spinning bottles (all 4 bottles render identical copies, not separate fluid levels)
        final LazyOptional<FluidTank> inputTank = tileEntity.fluidTanks.get(0);
        if (inputTank.map(tank -> !tank.getFluid().isEmpty()).orElse(false)) {
            matrixStack.pushPose();

            applySpinnyBitTransform(matrixStack, facingDir, getSpinnyBitRotationDegrees(tileEntity, partialTicks));

            renderFluidInBottle(inputTank, SOUTH_BOTTLE_FLUID_AABB, 4, 14.5, 12, Vector3f.ZP, -45, matrixStack, renderBuffer, combinedLight, combinedOverlay);
            renderFluidInBottle(inputTank, NORTH_BOTTLE_FLUID_AABB, 4, 14.5, -4, Vector3f.ZP, 45, matrixStack, renderBuffer, combinedLight, combinedOverlay);
            renderFluidInBottle(inputTank, EAST_BOTTLE_FLUID_AABB, 12, 14.5, 4, Vector3f.XP, -45, matrixStack, renderBuffer, combinedLight, combinedOverlay);
            renderFluidInBottle(inputTank, WEST_BOTTLE_FLUID_AABB, -4, 14.5, 4, Vector3f.XP, 45, matrixStack, renderBuffer, combinedLight, combinedOverlay);

            matrixStack.popPose();
        }

        // Outer Pop
        matrixStack.popPose();

    }

    public static float getSpinnyBitRotationDegrees(TileCentrifugalDepositionUnit tileEntity, float partialTicks) {
        final float rotationsPerProcessingCycle = 30.0F;
        return (float) (tileEntity.getClientProcessingPercentage(partialTicks) * 360.0D * rotationsPerProcessingCycle);
    }

    private static void applySpinnyBitTransform(MatrixStack matrixStack, Direction facingDir, float rotationDegrees) {
        // Spin about model center to match machine facing direction (BlockState)
        matrixStack.translate(0.5D, 1.5D, 0.5D);
        if (facingDir == Direction.WEST) matrixStack.mulPose(new Quaternion(0, 270, 0, true));
        else if (facingDir == Direction.NORTH) matrixStack.mulPose(new Quaternion(0, 180, 0, true));
        else if (facingDir == Direction.EAST) matrixStack.mulPose(new Quaternion(0, 90, 0, true));
        matrixStack.translate(-0.5D, -0.5D, -0.5D);
        // Spin about center column of the spinning portion (model X/Z 4) to match machine processing progress
        final double spinnybitPivotX = 4.0D / 16.0D;
        final double spinnybitPivotZ = 4.0D / 16.0D;
        matrixStack.translate(spinnybitPivotX, 0.0D, spinnybitPivotZ);
        matrixStack.mulPose(new Quaternion(Vector3f.YP, rotationDegrees, true));
        matrixStack.translate(-spinnybitPivotX, 0.0D, -spinnybitPivotZ);
    }

    private static void renderFluidInBottle(LazyOptional<FluidTank> fluidTank, AxisAlignedBB fluidAABB, double originX, double originY, double originZ, Vector3f rotationAxis, float rotationDegrees, MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, int combinedLight, int combinedOverlay) {
        matrixStack.pushPose();
        matrixStack.translate(originX / 16.0D, originY / 16.0D, originZ / 16.0D);
        matrixStack.mulPose(new Quaternion(rotationAxis, rotationDegrees, true));
        matrixStack.translate(-originX / 16.0D, -originY / 16.0D, -originZ / 16.0D);
        renderFluidTank(fluidTank, fluidAABB, true, matrixStack, renderBuffer, combinedLight, combinedOverlay);
        matrixStack.popPose();
    }

}
