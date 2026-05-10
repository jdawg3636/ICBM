package com.jdawg3636.icbm.common.block.metallurgic_leaching_chamber;

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

public class TERMetallurgicLeachingChamber extends AbstractTERMachine<TileMetallurgicLeachingChamber> {

    public final LazyOptional<IBakedModel> MODEL_STATIC;

    public final AxisAlignedBB UNADJUSTED_FLUID_AABB = new AxisAlignedBB(
            2, 16, 2,
            14.0, 30.0, 14.0
    );

    public TERMetallurgicLeachingChamber(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        this(
                tileEntityRendererDispatcher,
                LazyOptional.of(() -> Minecraft.getInstance().getModelManager().getModel(ClientProxy.MODEL_METALLURGIC_LEACHING_CHAMBER))
        );
    }

    public TERMetallurgicLeachingChamber(TileEntityRendererDispatcher tileEntityRendererDispatcher, LazyOptional<IBakedModel> modelStatic) {
        super(tileEntityRendererDispatcher);
        this.MODEL_STATIC = modelStatic;
    }

    @Override
    public void render(TileMetallurgicLeachingChamber tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, int combinedLight, int combinedOverlay) {

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
        MODEL_STATIC.ifPresent(model -> Minecraft.getInstance().getItemRenderer().render(Blocks.STONE.asItem().getDefaultInstance(), ItemCameraTransforms.TransformType.NONE, false, matrixStack, renderBuffer, combinedLight, combinedOverlay, model));
        matrixStack.popPose();

        renderFluidTank(tileEntity.fluidTanks.get(0), UNADJUSTED_FLUID_AABB, matrixStack, renderBuffer, combinedLight, combinedOverlay);

        // TODO Render items in chamber

        // Outer Pop
        matrixStack.popPose();

    }

}
