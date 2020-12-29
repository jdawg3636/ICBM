package icbm.classic;

//TODO//import icbm.classic.prefab.tile.IGuiTile;
//TODO//import icbm.classic.content.entity.missile.EntityMissile;
//TODO//import icbm.classic.lib.transform.vector.Pos;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CommonProxy /* implements IGuiHandler */ {

    public static final int GUI_ITEM = 10002;
    public static final int GUI_ENTITY = 10001;

    public void onClientSetupEvent() {
    }

    public void doLoadModels() {
    }

    public void preInit() {
    }

    public void init() {
    }

    public void postInit() {
    }

    public void loadComplete() {
    }

    //TODO//IGuiHandler// @Override
    //TODO//public Object getServerGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z) {
    //TODO//    if (ID == GUI_ITEM)
    //TODO//        return getServerGuiElement(y, player, x);
    //TODO//    else if (ID == GUI_ENTITY)
    //TODO//        return getServerGuiElement(y, player, world.getEntityByID(x));
    //TODO//    return getServerGuiElement(ID, player, world.getTileEntity(new BlockPos(x, y, z)));
    //TODO//}

    //TODO//public Object getServerGuiElement(int ID, PlayerEntity player, int slot) {
    //TODO//    ItemStack stack = player.inventory.getStackInSlot(slot);
    //TODO//    if (stack != null && stack.getItem() instanceof IGuiTile)
    //TODO//        return ((IGuiTile) stack.getItem()).getServerGuiElement(ID, player);
    //TODO//    return null;
    //TODO//}

    //TODO//public Object getServerGuiElement(int ID, PlayerEntity player, TileEntity tile) {
    //TODO//    if (tile instanceof IGuiTile)
    //TODO//        return ((IGuiTile) tile).getServerGuiElement(ID, player);
    //TODO//    return null;
    //TODO//}

    //TODO//public Object getServerGuiElement(int ID, PlayerEntity player, Entity entity) {
    //TODO//    if (entity instanceof IGuiTile)
    //TODO//        return ((IGuiTile) entity).getServerGuiElement(ID, player);
    //TODO//    return null;
    //TODO//}

    //TODO//IGuiHandler// @Override
    //TODO//public Object getClientGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z) {
    //TODO//    if (ID == GUI_ITEM)
    //TODO//        return getServerGuiElement(y, player, world.getEntityByID(x));
    //TODO//    else if (ID == GUI_ENTITY)
    //TODO//        return getClientGuiElement(y, player, world.getEntityByID(x));
    //TODO//    return getClientGuiElement(ID, player, world.getTileEntity(new BlockPos(x, y, z)));
    //TODO//}

    //TODO//public Object getClientGuiElement(int ID, PlayerEntity player, int slot) {
    //TODO//    ItemStack stack = player.inventory.getStackInSlot(slot);
    //TODO//    if (stack != null && stack.getItem() instanceof IGuiTile)
    //TODO//        return ((IGuiTile) stack.getItem()).getClientGuiElement(ID, player);
    //TODO//    return null;
    //TODO//}

    //TODO//public Object getClientGuiElement(int ID, PlayerEntity player, TileEntity tile) {
    //TODO//    if (tile instanceof IGuiTile)
    //TODO//        return ((IGuiTile) tile).getClientGuiElement(ID, player);
    //TODO//    return null;
    //TODO//}

    //TODO//public Object getClientGuiElement(int ID, PlayerEntity player, Entity entity) {
    //TODO//    if (entity instanceof IGuiTile)
    //TODO//        return ((IGuiTile) entity).getClientGuiElement(ID, player);
    //TODO//    return null;
    //TODO//}

    /* TODO
    public void spawnSmoke(World world, Pos position, double v, double v1, double v2, float red, float green, float blue, float scale, int age) {
    }

    public void spawnAirParticle(World world, Pos position, double v, double v1, double v2, float red, float green, float blue, float scale, int age) {
    }
    */

    /*TODO
    public void spawnMissileSmoke(EntityMissile missile) {
    }
    */

}
