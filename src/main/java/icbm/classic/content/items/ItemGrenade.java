package icbm.classic.content.items;

import icbm.classic.api.ICBMClassicAPI;
import icbm.classic.api.reg.IExplosiveData;
import icbm.classic.content.entity.EntityGrenade;
import icbm.classic.lib.capability.ex.CapabilityExplosiveStack;
import icbm.classic.prefab.item.ItemBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ItemGrenade extends ItemBase
{
    public static final int MAX_USE_DURATION = 3 * 20; //TODO config

    public ItemGrenade() {
        super(new Item.Properties()
            .maxStackSize(16)
            .maxDamage(16)
        );
        //this.setHasSubtypes(true);
    }

    @Override
    @Nullable
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        return new CapabilityExplosiveStack(stack);
    }

    @Override
    public UseAction getUseAction(ItemStack par1ItemStack)
    {
        return UseAction.BOW;
    }

    @Override
    public int getUseDuration(ItemStack par1ItemStack)
    {
        return MAX_USE_DURATION;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        return new ActionResult<ItemStack>(ActionResultType.SUCCESS, itemstack);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, LivingEntity entityLiving, int timeLeft)
    {
        if (!world.isRemote)
        {
            //Play throw sound
            world.playSound(null, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

            //Calculate energy based on player hold time
            final float throwEnergy = (float) (this.getUseDuration(itemStack) - timeLeft) / (float) this.getUseDuration(itemStack);

            //Create generate entity
            new EntityGrenade(world)
            .setItemStack(itemStack)
            .setThrower(entityLiving)
            .aimFromThrower()
            .setThrowMotion(throwEnergy).spawn();

            //Consume item
            if (!(entityLiving instanceof PlayerEntity) || !((PlayerEntity) entityLiving).abilities.isCreativeMode)
            {
                itemStack.shrink(1);
            }
        }
    }

    @Override
    public String getTranslationKey(ItemStack itemstack)
    {
        final IExplosiveData data = ICBMClassicAPI.EXPLOSIVE_REGISTRY.getExplosiveData(itemstack.getDamage());
        if (data != null)
        {
            return "grenade." + data.getRegistryName();
        }
        return "grenade";
    }

    @Override
    public String getTranslationKey()
    {
        return "grenade";
    }

    @Override
    protected boolean hasDetailedInfo(ItemStack stack, PlayerEntity player)
    {
        return true;
    }

    @Override
    protected void getDetailedInfo(ItemStack stack, PlayerEntity player, List list)
    {
        //TODO ((ItemBlockExplosive) Item.getItemFromBlock(ICBMClassic.blockExplosive)).getDetailedInfo(stack, player, list);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
    {
        super.fillItemGroup(group, items);
        // TODO Need to convert numeric ids to NBT (or just different registry names)
        /*
        if (group == getGroup() || group == ItemGroup.SEARCH)
        {
            for (int id : ICBMClassicAPI.EX_GRENADE_REGISTRY.getExplosivesIDs())
            {
                items.add(new ItemStack(this, 1, id));
            }
        }
        */
    }
}
