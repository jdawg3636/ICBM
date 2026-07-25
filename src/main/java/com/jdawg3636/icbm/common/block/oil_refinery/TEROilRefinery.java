package com.jdawg3636.icbm.common.block.oil_refinery;

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
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class TEROilRefinery extends AbstractTERMachine<TileOilRefinery> {

    public final LazyOptional<IBakedModel> MODEL_STATIC;

    public final AxisAlignedBB UNADJUSTED_FLUID_AABB = new AxisAlignedBB(
            2, 0, 2,
            14.0, 28.0, 14.0
    );

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

            final LazyOptional<FluidTank> fluidTank = tileEntity.fluidTanks.get(i);

            // Push for tank
            matrixStack.pushPose();

            // Translate up and back (same for both tanks)
            matrixStack.translate(-normal.getX(), 4.0D / 16.0D, -normal.getZ());
            // Translate left or right (direction depends on which tank)
            if(i == 0) matrixStack.translate(-tangent.getX(), 0, -tangent.getZ());
            else matrixStack.translate(tangent.getX(), 0, tangent.getZ());

            renderFluidTank(fluidTank, UNADJUSTED_FLUID_AABB, true, matrixStack, renderBuffer, combinedLight, combinedOverlay);

            // Pop for tank
            matrixStack.popPose();

        }

        // Outer Pop
        matrixStack.popPose();

    }

}
