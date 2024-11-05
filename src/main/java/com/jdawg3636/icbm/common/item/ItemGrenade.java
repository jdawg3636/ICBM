package com.jdawg3636.icbm.common.item;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.entity.EntityGrenade;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

public class ItemGrenade extends Item {

    public final RegistryObject<EntityType<EntityGrenade>> entityForm;

    public ItemGrenade(RegistryObject<EntityType<EntityGrenade>> entityForm) {
        super(new Item.Properties().tab(ICBMReference.CREATIVE_TAB).stacksTo(16));
        this.entityForm = entityForm;
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 60;
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        playerIn.startUsingItem(handIn);
        return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, World world, LivingEntity entityLiving, int timeLeft) {

        if (!world.isClientSide()) {

            world.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.TNT_PRIMED, SoundCategory.BLOCKS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

            float throwEnergy = (float) (getUseDuration(itemStack) - timeLeft) / (float) getUseDuration(itemStack) + 0.7f;
            EntityGrenade grenade = entityForm.get().create(world);
            if(grenade != null) {
                grenade.moveTo(entityLiving.getX(), entityLiving.getY() + entityLiving.getEyeHeight() * 0.8, entityLiving.getZ(), entityLiving.getRotationVector().y, entityLiving.getRotationVector().x);
                grenade.shootFromRotation(entityLiving, entityLiving.getRotationVector().x - 20, entityLiving.getRotationVector().y, 0.0F, throwEnergy, 1.0F);
                world.addFreshEntity(grenade);
                if (!(entityLiving instanceof PlayerEntity && ((PlayerEntity) entityLiving).isCreative())) {
                    itemStack.shrink(1);
                }
            }

        }

    }

}
