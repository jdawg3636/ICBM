package icbm.classic.content.items.behavior;

import icbm.classic.content.entity.EntityBombCart;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MinecartItem;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Copied almost 1:1 from {@link MinecartItem}
 * */
public class BombCartDispenseBehavior extends DefaultDispenseItemBehavior {

    private final DefaultDispenseItemBehavior behaviourDefaultDispenseItem = new DefaultDispenseItemBehavior();

    public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        Direction direction = source.getBlockState().get(DispenserBlock.FACING);
        World world = source.getWorld();
        double x = source.getX() + (double)direction.getXOffset() * 1.125D;
        double y = Math.floor(source.getY()) + (double)direction.getYOffset();
        double z = source.getZ() + (double)direction.getZOffset() * 1.125D;
        BlockPos blockpos = source.getBlockPos().offset(direction);
        BlockState blockstate = world.getBlockState(blockpos);
        RailShape railshape = blockstate.getBlock() instanceof AbstractRailBlock ? ((AbstractRailBlock)blockstate.getBlock()).getRailDirection(blockstate, world, blockpos, null) : RailShape.NORTH_SOUTH;
        double d3;
        if (blockstate.func_235714_a_(BlockTags.RAILS)) {
            if (railshape.isAscending()) {
                d3 = 0.6D;
            } else {
                d3 = 0.1D;
            }
        } else {
            if (!blockstate.isAir() || !world.getBlockState(blockpos.down()).func_235714_a_(BlockTags.RAILS)) {
                return this.behaviourDefaultDispenseItem.dispense(source, stack);
            }

            BlockState blockstate1 = world.getBlockState(blockpos.down());
            RailShape railshape1 = blockstate1.getBlock() instanceof AbstractRailBlock ? blockstate1.get(((AbstractRailBlock)blockstate1.getBlock()).getShapeProperty()) : RailShape.NORTH_SOUTH;
            if (direction != Direction.DOWN && railshape1.isAscending()) {
                d3 = -0.4D;
            } else {
                d3 = -0.9D;
            }
        }

        // Vanilla uses the AbstractMinecartEntity.Type enum - bypassing this by using the constructor instead of AbstractMinecartEntity.create()
        // AbstractMinecartEntity cart = AbstractMinecartEntity.create(world, d0, d1 + d3, d2, ((MinecartItem)stack.getItem()).minecartType);
        EntityBombCart cart = new EntityBombCart(world, x, y + d3, z, stack.getDamage());

        if (stack.hasDisplayName()) {
            cart.setCustomName(stack.getDisplayName());
        }

        world.addEntity(cart);
        stack.shrink(1);
        return stack;

    }
}
