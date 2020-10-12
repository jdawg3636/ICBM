package icbm.classic.prefab.inventory;

import com.builtbroken.jlib.data.vector.IPos3D;
import icbm.classic.lib.transform.vector.Pos;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

/**
 * Series of helper classes for dealing with any kind of inventory
 *
 * @author Calclavia, DarkCow(aka Darkguardsman, Robert)
 */
public class InventoryUtility {

    /**
     * Attempts to drop the block at the location as an item. Does not check what the block is
     * and can fail if the block doesn't contain items.
     *
     * @param world
     * @param pos
     * @param destroy - will break the block
     */
    public static List<ItemEntity> dropBlockAsItem(World world, BlockPos pos, boolean destroy) {

        List<ItemEntity> entities = new ArrayList();

        if (!world.isRemote) {

            BlockState state = world.getBlockState(pos);

            if (state != null && !state.getBlock().isAir(state, world, pos)) {

                List<ItemStack> items = Block.getDrops(state, (ServerWorld) world, pos, null);

                for (ItemStack itemStack : items) {

                    ItemEntity entityItem = dropItemStack(world, new Pos(pos), itemStack, 10);

                    if (entityItem != null)
                        entities.add(entityItem);

                }

            }

            if (destroy)
                world.setBlockState(pos, Blocks.AIR.getDefaultState());

        }

        return entities;

    }

    public static ItemEntity dropItemStack(World world, IPos3D position, ItemStack itemStack, int delay) {
        return dropItemStack(world, position, itemStack, delay, 0f);
    }

    public static ItemEntity dropItemStack(World world, IPos3D position, ItemStack itemStack, int delay, float randomAmount) {
        return dropItemStack(world, position.x(), position.y(), position.z(), itemStack, delay, randomAmount);
    }

    public static ItemEntity dropItemStack(World world, double x, double y, double z, ItemStack itemStack, int delay, float randomAmount) {

        //TODO fire drop events if not already done by forge
        //TODO add banned item filtering, prevent creative mode only items from being dropped
        if (world != null && !world.isRemote && !itemStack.isEmpty()) {

            double randomX = 0;
            double randomY = 0;
            double randomZ = 0;

            if (randomAmount > 0) {
                randomX = world.rand.nextFloat() * randomAmount + (1.0F - randomAmount) * 0.5D;
                randomY = world.rand.nextFloat() * randomAmount + (1.0F - randomAmount) * 0.5D;
                randomZ = world.rand.nextFloat() * randomAmount + (1.0F - randomAmount) * 0.5D;
            }

            ItemEntity entityitem = new ItemEntity(world, x + randomX, y + randomY, z + randomZ, itemStack);

            if (randomAmount <= 0)
                entityitem.setMotion(0, 0, 0);

            if (itemStack.hasTag())
                entityitem.getItem().setTag(itemStack.getTag().copy());

            entityitem.setPickupDelay(delay);
            world.addEntity(entityitem);

            return entityitem;

        }

        return null;

    }

    /**
     * Checks if the two item stacks match each other exactly. Item, meta, stacksize, nbt
     *
     * @param stackA - item stack a
     * @param stackB - item stack a
     * @return true if they match
     */
    public static boolean stacksMatchExact(ItemStack stackA, ItemStack stackB) {

        if (!stackA.isEmpty() && !stackB.isEmpty())
            return stackA.isItemEqual(stackB) && doesStackNBTMatch(stackA, stackB) && stackA.getCount() == stackB.getCount();

        return stackA.isEmpty() && stackB.isEmpty();

    }

    /**
     * Checks if two item stacks match each other using item, meta, and nbt to compare
     *
     * @param stackA - item stack a
     * @param stackB - item stack a
     * @return true if they match
     */
    public static boolean stacksMatch(ItemStack stackA, ItemStack stackB) {

        if (!stackA.isEmpty() && !stackB.isEmpty())
            return stackA.isItemEqual(stackB) && doesStackNBTMatch(stackA, stackB);

        return stackA.isEmpty() && stackB.isEmpty();

    }

    /**
     * Checks if two itemStack's nbt matches exactly. Does not check item, stacksize, or damage value.
     *
     * @param stackA - item stack a, can't be null
     * @param stackB - item stack a, can't be null
     * @return true if the stack's nbt matches
     */
    public static boolean doesStackNBTMatch(ItemStack stackA, ItemStack stackB) {
        return doTagsMatch(stackA.getTag(), stackB.getTag());
    }

    public static boolean doTagsMatch(final CompoundNBT tag, final CompoundNBT tag2) {

        boolean firstTagEmpty = tag == null || tag.isEmpty();
        boolean firstTagEmpty2 = tag2 == null || tag2.isEmpty();

        if (firstTagEmpty && firstTagEmpty2)
            return true;
        else if (!firstTagEmpty && firstTagEmpty2)
            return false;
        else if (firstTagEmpty && !firstTagEmpty2)
            return false;

        return tag.equals(tag2);

    }

}
