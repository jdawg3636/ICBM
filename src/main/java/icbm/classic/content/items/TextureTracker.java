package icbm.classic.content.items;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;import net.minecraftforge.fml.relauncher.SideOnly;
import icbm.classic.ICBMClassic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class TextureTracker extends TextureAtlasSprite
{
    public double currentAngle;
    public double angleDelta;

    public TextureTracker()
    {
        super(ICBMClassic.itemTracker.getUnlocalizedName().replace("item.", ""));
    }

    @Override
    public void updateAnimation()
    {
        Minecraft minecraft = Minecraft.getMinecraft();
        World world = minecraft.theWorld;
        EntityPlayer player = minecraft.thePlayer;

        double angel = 0;

        if (world != null)
        {
            double xDifference = 0;
            double zDifference = 0;

            ItemStack itemStack = player.getCurrentEquippedItem();

            if (itemStack != null)
            {
                if (itemStack.getItem() instanceof ItemTracker)
                {
                    Entity trackingEntity = ((ItemTracker) itemStack.getItem()).getTrackingEntity(FMLClientHandler.instance().getClient().theWorld, itemStack);

                    if (trackingEntity != null)
                    {
                        xDifference = trackingEntity.posX - player.posX;
                        zDifference = trackingEntity.posZ - player.posZ;
                    }
                }
            }

            player.rotationYaw %= 360.0D;
            angel = -((player.rotationYaw - 90.0D) * Math.PI / 180.0D - Math.atan2(zDifference, xDifference));
        }

        double d6;

        for (d6 = angel - this.currentAngle; d6 < -Math.PI; d6 += (Math.PI * 2D))
        {
            ;
        }

        while (d6 >= Math.PI)
        {
            d6 -= (Math.PI * 2D);
        }

        if (d6 < -1.0D)
        {
            d6 = -1.0D;
        }

        if (d6 > 1.0D)
        {
            d6 = 1.0D;
        }

        this.angleDelta += d6 * 0.1D;
        this.angleDelta *= 0.8D;
        this.currentAngle += this.angleDelta;

        int i;

        for (i = (int) ((this.currentAngle / (Math.PI * 2D) + 1.0D) * this.framesTextureData.size()) % this.framesTextureData.size(); i < 0; i = (i + this.framesTextureData.size()) % this.framesTextureData.size())
        {
            ;
        }

        if (i != this.frameCounter)
        {
            this.frameCounter = i;
            TextureUtil.uploadTextureMipmap((int[][])this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
        }
    }
}
