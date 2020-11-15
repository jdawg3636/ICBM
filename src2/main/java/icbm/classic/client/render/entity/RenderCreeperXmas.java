package icbm.classic.client.render.entity;

import icbm.classic.ICBMConstants;
import icbm.classic.client.models.mobs.ModelCreeperXmas;
import icbm.classic.content.entity.mobs.EntityXmasCreeper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderCreeperXmas extends MobRenderer<EntityXmasCreeper, ModelCreeperXmas<EntityXmasCreeper>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ICBMConstants.DOMAIN, "textures/entity/creeper/creeper.png");

    public RenderCreeperXmas(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ModelCreeperXmas(), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityXmasCreeper entity)
    {
        return TEXTURE;
    }
}