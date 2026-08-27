package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.ICBMReference;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class EntityRubberBoatRenderer extends BoatRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ICBMReference.MODID, "textures/entity/boat/rubber.png");
    public EntityRubberBoatRenderer(EntityRendererManager manager) { super(manager); }
    @Override public ResourceLocation getTextureLocation(net.minecraft.entity.item.BoatEntity entity) { return TEXTURE; }
}
