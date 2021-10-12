package com.jdawg3636.icbm.common.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;

public class EntityLingeringBlastRenderer extends EntityRenderer<EntityLingeringBlast> {
    public EntityLingeringBlastRenderer(EntityRendererManager p_i46554_1_) {
        super(p_i46554_1_);
    }

    public ResourceLocation getTextureLocation(EntityLingeringBlast p_110775_1_) {
        return AtlasTexture.LOCATION_BLOCKS;
    }
}
