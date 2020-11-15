package icbm.classic.content.blocks.emptower;

import com.mojang.blaze3d.platform.GlStateManager;
import icbm.classic.ICBMConstants;
import icbm.classic.client.models.ModelEmpTower;;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 1/10/2017.
 */
public class TESREMPTower extends TileEntityRenderer<TileEMPTower> {

    public static final ResourceLocation TEXTURE_FILE = new ResourceLocation(ICBMConstants.DOMAIN, "textures/models/" + "emp_tower.png");
    public static final ModelEmpTower MODEL = new ModelEmpTower();

    @Override
    @OnlyIn(Dist.CLIENT)
    // ConduitTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn
    public void render(TileEMPTower tileEntityIn, float partialTicks, double x, double y, double z, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translated(x + 0.5F, y + 1.5F, z + 0.5F);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TEXTURE_FILE);
        GlStateManager.rotate(180F, 0.0F, 0.0F, 1.0F);
        MODEL.render(tileEntityIn.rotation, 0.0625F);
        GlStateManager.popMatrix();
    }

}
