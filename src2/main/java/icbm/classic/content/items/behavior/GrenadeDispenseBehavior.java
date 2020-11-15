package icbm.classic.content.items.behavior;

import icbm.classic.content.entity.EntityGrenade;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;

public class GrenadeDispenseBehavior implements IDispenseItemBehavior
{
    @Override
    public ItemStack dispense(IBlockSource blockSource, ItemStack itemStack)
    {
        if (itemStack.getCount() > 0)
        {
            final World world = blockSource.getWorld();
            if (!world.isRemote)
            {
                world.addEntity(create(world, blockSource, itemStack));
            }

            return itemStack.split(itemStack.getCount() - 1);
        }
        return ItemStack.EMPTY;
    }

    private Entity create(World world, IBlockSource blockSource, ItemStack itemStack)
    {
        final Direction enumFacing = blockSource.getBlockState().get(DispenserBlock.FACING);

        final EntityGrenade entity = new EntityGrenade(world);
        entity.setPosition(blockSource.getX(), blockSource.getY(), blockSource.getZ());
        entity.setItemStack(itemStack);
        entity.setThrowableHeading(enumFacing.getXOffset(), 0.10000000149011612D, enumFacing.getZOffset(), 0.5F, 1.0F);
        return entity;
    }
}
