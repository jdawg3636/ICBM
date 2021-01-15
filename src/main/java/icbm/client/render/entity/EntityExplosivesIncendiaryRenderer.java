package icbm.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import icbm.common.entity.EntityExplosivesIncendiary;
import icbm.common.reg.BlockReg;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.TNTMinecartRenderer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

// Copied almost one-to-one from net.minecraft.client.renderer.entity.TNTRenderer
@OnlyIn(Dist.CLIENT)
public class EntityExplosivesIncendiaryRenderer extends EntityRenderer<EntityExplosivesIncendiary> {

    public EntityExplosivesIncendiaryRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void render(@Nonnull EntityExplosivesIncendiary entity, float entityYaw, float partialTick, @Nonnull MatrixStack matrix, @Nonnull IRenderTypeBuffer renderer, int light) {
        matrix.push();
        matrix.translate(0, 0.5, 0);
        if (entity.getFuse() - partialTick + 1.0F < 10.0F) {
            float f = 1.0F - (entity.getFuse() - partialTick + 1.0F) / 10.0F;
            f = MathHelper.clamp(f, 0.0F, 1.0F);
            f = f * f;
            f = f * f;
            float f1 = 1.0F + f * 0.3F;
            matrix.scale(f1, f1, f1);
        }

        matrix.rotate(Vector3f.YP.rotationDegrees(-90.0F));
        matrix.translate(-0.5, -0.5, 0.5);
        matrix.rotate(Vector3f.YP.rotationDegrees(90.0F));
        TNTMinecartRenderer.renderTntFlash(BlockReg.EXPLOSIVES_INCENDIARY.get().getDefaultState(), matrix, renderer, light, entity.getFuse() / 5 % 2 == 0);
        matrix.pop();
        super.render(entity, entityYaw, partialTick, matrix, renderer, light);
    }

    @Nonnull
    @Override
    public ResourceLocation getEntityTexture(@Nonnull EntityExplosivesIncendiary entity) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }

}
