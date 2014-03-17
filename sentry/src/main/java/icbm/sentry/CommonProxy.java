package icbm.sentry;

import icbm.sentry.platform.TileTurretPlatform;
import icbm.sentry.platform.gui.ContainerTurretPlatform;
import icbm.sentry.turret.block.TileTurret;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.api.vector.Vector3;
import calclavia.lib.gui.ContainerDummy;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler
{
    /** GUI IDs */
    public static final int GUI_PLATFORM_ID = 0;
    public static final int GUI_PLATFORM_TERMINAL_ID = 1;
    public static final int GUI_PLATFORM_ACCESS_ID = 2;

    public void init()
    {

    }

    public void preInit()
    {

    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile instanceof TileTurretPlatform)
        {
            if (ID == 0)
                return new ContainerTurretPlatform(player.inventory, (TileTurretPlatform) tile);
        }
        if (tile instanceof TileTurret)
        {
            if (ID == 1)
                return new ContainerDummy(player, tile);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }

    /** Renders a bullet tracer from one spot to another will later be replaced with start and degree */
    public void renderTracer(World world, Vector3 position, Vector3 target)
    {

    }

    public void renderBeam(World world, Vector3 position, Vector3 target, float red, float green, float blue, int age)
    {

    }
}
