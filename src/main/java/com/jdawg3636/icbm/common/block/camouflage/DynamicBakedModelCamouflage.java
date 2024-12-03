package com.jdawg3636.icbm.common.block.camouflage;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.jdawg3636.icbm.common.listener.ClientProxy;
import com.jdawg3636.icbm.common.reg.BlockReg;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import net.minecraftforge.common.util.LazyOptional;

import java.util.*;
import java.util.function.Function;

public class DynamicBakedModelCamouflage implements IDynamicBakedModel {

    public static final LazyOptional<IBakedModel> defaultModel = LazyOptional.of(() -> {
        return Minecraft.getInstance().getModelManager().getModel(ClientProxy.MODEL_CAMOUFLAGE_DEFAULT);
    });

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand, IModelData extraData) {

        TileCamouflage tile = extraData.getData(TileCamouflage.TILE_ENTITY);
        if (tile == null) return defaultModel.orElse(null).getQuads(state, side, rand, extraData);

        BlockState mimickedState = tile.getAppearanceNoNull();
        if (tile.isSideTransparent(side)) {
            mimickedState = BlockReg.REINFORCED_GLASS.get().defaultBlockState();
        }

        IBakedModel model;
        boolean shouldRender;
        BlockState blockStateForRender;
        if (mimickedState.getBlock() instanceof BlockCamouflage) {
            blockStateForRender = state;
            model = defaultModel.orElse(null);
            shouldRender = MinecraftForgeClient.getRenderLayer().equals(RenderType.solid());
        }
        else {
            blockStateForRender = mimickedState;
            model = Minecraft.getInstance().getBlockRenderer().getBlockModel(mimickedState);
            shouldRender = RenderTypeLookup.canRenderInLayer(mimickedState, MinecraftForgeClient.getRenderLayer());
        }

        if (side != null && tile.getLevel().getBlockState(tile.getBlockPos().relative(side)).getBlock() instanceof BlockCamouflage) {
            shouldRender = false;
        }

        return shouldRender ? model.getQuads(blockStateForRender, side, rand, extraData) : Collections.emptyList();

    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.EMPTY;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return defaultModel.orElse(null).getParticleIcon();
    }

    public static class CamouflageModelGeometry implements IModelGeometry<CamouflageModelGeometry> {

        @Override
        public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<RenderMaterial, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation) {
            return new DynamicBakedModelCamouflage();
        }

        @Override
        public Collection<RenderMaterial> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
            return Collections.singletonList(new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, ClientProxy.MODEL_CAMOUFLAGE_DEFAULT));
        }

    }

    public static class CamouflageModelLoader implements IModelLoader<CamouflageModelGeometry> {

        @Override
        public void onResourceManagerReload(IResourceManager resourceManager) {}

        @Override
        public CamouflageModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
            return new CamouflageModelGeometry();
        }

    }

}

