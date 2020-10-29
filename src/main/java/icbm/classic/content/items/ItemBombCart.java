package icbm.classic.content.items;

import icbm.classic.api.ICBMClassicAPI;
import icbm.classic.api.reg.IExplosiveData;
import icbm.classic.content.blocks.explosive.BlockItemExplosive;
import icbm.classic.content.entity.EntityBombCart;
import icbm.classic.content.reg.BlockReg;
import icbm.classic.lib.capability.ex.CapabilityExplosiveStack;
import icbm.classic.prefab.item.ItemBase;
import net.minecraft.block.BlockState;
import net.minecraft.block.RailBlock;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBombCart extends ItemBase {

    public ItemBombCart() {
        super(new Item.Properties().maxStackSize(3));
    }

    @Override
    @Nullable
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {

        CapabilityExplosiveStack capabilityExplosive = new CapabilityExplosiveStack(stack);

        if(nbt != null)
            capabilityExplosive.deserializeNBT(nbt);

        return capabilityExplosive;

    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have
     * one of those. Return True if something happen and false if it don't. This is for ITEMS, not
     * BLOCKS
     */
    @Override
    public ActionResult onItemUse(PlayerEntity player, World worldIn, BlockPos pos, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {

        BlockState iblockstate = worldIn.getBlockState(pos);

        if (!RailBlock.isRail(iblockstate))
            return ActionResult.resultFail(null);
        else {

            ItemStack itemstack = player.getHeldItem(hand);

            if (!worldIn.isRemote) {

                RailShape railBlock = iblockstate.getBlock() instanceof RailBlock ? ((RailBlock) iblockstate.getBlock()).getRailDirection(iblockstate, worldIn, pos, null) : RailShape.NORTH_SOUTH;
                double d0 = 0.0D;

                if (railBlock.isAscending())
                    d0 = 0.5D;

                MinecartEntity entityminecart = new EntityBombCart(worldIn, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.0625D + d0, (double) pos.getZ() + 0.5D, itemstack.getItemDamage());

                if (itemstack.hasDisplayName())
                    entityminecart.setCustomName(itemstack.getDisplayName());

                worldIn.addEntity(entityminecart);

            }

            itemstack.shrink(1);
            return ActionResult.resultSuccess(null);

        }

    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getTranslationKey(ItemStack itemstack) {

        final IExplosiveData data = ICBMClassicAPI.EXPLOSIVE_REGISTRY.getExplosiveData(itemstack.getItemDamage());

        if (data != null)
            return "bombcart." + data.getRegistryName();

        return "bombcart";

    }

    @Override
    public String getTranslationKey()
    {
        return "bombcart";
    }

    @Override
    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> items) {

        if (tab == getGroup() || tab == ItemGroup.SEARCH)
            for (int id : ICBMClassicAPI.EX_MINECART_REGISTRY.getExplosivesIDs())
                items.add(new ItemStack(this, 1, id));

    }

    @Override
    protected boolean hasDetailedInfo(ItemStack stack, PlayerEntity player) {
        return true;
    }

    @Override
    protected void getDetailedInfo(ItemStack stack, PlayerEntity player, List list) {
        //TODO change over to a hook
        ((BlockItemExplosive) Item.getItemFromBlock(BlockReg.EXPLOSIVES)).getDetailedInfo(stack, player, list);
    }

}
