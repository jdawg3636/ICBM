package com.jdawg3636.icbm.mixin;

import com.jdawg3636.icbm.ICBMReference;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.pipeline.ForgeBlockModelRenderer;
import net.minecraftforge.client.model.pipeline.VertexBufferConsumer;
import net.minecraftforge.client.model.pipeline.VertexLighterFlat;
import net.minecraftforge.client.model.pipeline.VertexLighterSmoothAo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

// Always use the "experimental" render pipeline for blocks from this mod, regardless of the user-defined value
// in config/forge-client.toml. This is done because it fixes very noticeable visual anomalies with OBJ models, and
// most end-users are not aware of the config option in Forge (and shouldn't be forced to change it regardless).
// TODO: Remove Once Forge Enables experimentalForgeLightPipelineEnabled By Default
@Mixin(ForgeBlockModelRenderer.class)
public class MixinClientOBJLightingPipeline extends BlockModelRenderer {

    @Inject(method = "renderModelSmooth", at = @At("HEAD"), cancellable = true, remap = false)
    public void onRenderModelSmooth(IBlockDisplayReader world, IBakedModel model, BlockState state, BlockPos pos, MatrixStack matrixStack, IVertexBuilder buffer, boolean checkSides, Random rand, long seed, int combinedOverlayIn, IModelData modelData, CallbackInfoReturnable<Boolean> callback) {
        ResourceLocation registryName = state.getBlock().getRegistryName();
        Package optifinePackage = Package.getPackage("net.optifine");
        if(registryName != null && registryName.getNamespace().equals(ICBMReference.MODID) && optifinePackage == null) {

            /*
             * All of this code is copied from vanilla, with the exception of the condition being swapped.
             * TODO: Update with Minecraft Version Changes
             */

            VertexBufferConsumer consumer = consumerSmooth.get();
            consumer.setBuffer(buffer);

            VertexLighterSmoothAo lighter = lighterSmooth.get();
            lighter.setParent(consumer);
            lighter.setTransform(matrixStack.last());

            callback.setReturnValue(render(lighter, world, model, state, pos, matrixStack, checkSides, rand, seed, modelData));

        }
    }

    @Shadow(remap = false) @Final
    private ThreadLocal<VertexLighterFlat> lighterFlat;
    @Shadow(remap = false) @Final
    private ThreadLocal<VertexLighterSmoothAo> lighterSmooth;
    @Shadow(remap = false) @Final
    private ThreadLocal<VertexBufferConsumer> consumerFlat;
    @Shadow(remap = false) @Final
    private ThreadLocal<VertexBufferConsumer> consumerSmooth;
    @Shadow(remap = false)
    public static boolean render(VertexLighterFlat lighter, IBlockDisplayReader world, IBakedModel model, BlockState state, BlockPos pos, MatrixStack matrixStack, boolean checkSides, Random rand, long seed, IModelData modelData) {
        return true;
    }

    // Constructor to Trick the Java Compiler
    public MixinClientOBJLightingPipeline(BlockColors blockColors) {
        super(blockColors);
    }

}
