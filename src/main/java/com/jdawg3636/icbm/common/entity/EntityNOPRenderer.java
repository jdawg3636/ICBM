package com.jdawg3636.icbm.common.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * Dummy renderer for use by invisible entities (ex. blasts)
 */
public class EntityNOPRenderer extends EntityRenderer<Entity> {

    public EntityNOPRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager);
    }

    @Override
    public ResourceLocation getTextureLocation(Entity entity) {
        return AtlasTexture.LOCATION_BLOCKS;
    }

}
