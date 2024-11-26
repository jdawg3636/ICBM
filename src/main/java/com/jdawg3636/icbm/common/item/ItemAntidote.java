package com.jdawg3636.icbm.common.item;

import com.jdawg3636.icbm.common.reg.EffectReg;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemAntidote extends Item {

    public ItemAntidote(Properties properties) {
        super(properties);
    }

    // Adapted from net.minecraft.item.MilkBucketItem::finishUsingItem, MC 1.16.5, MCP Package/Class Names, Mojmap Method/Field Names
    public ItemStack finishUsingItem(ItemStack itemStack, World level, LivingEntity livingEntityUsing) {

        if (!level.isClientSide) {
            livingEntityUsing.curePotionEffects(Items.MILK_BUCKET.getDefaultInstance());
            livingEntityUsing.removeEffect(EffectReg.ENGINEERED_PATHOGEN.get());
            livingEntityUsing.addEffect(new EffectInstance(EffectReg.ENGINEERED_PATHOGEN_IMMUNITY.get(), 20 * 120));
        }

        if (livingEntityUsing instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)livingEntityUsing;
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayerentity, itemStack);
            serverplayerentity.awardStat(Stats.ITEM_USED.get(this));
        }

        if (livingEntityUsing instanceof PlayerEntity && !((PlayerEntity)livingEntityUsing).abilities.instabuild) {
            itemStack.shrink(1);
        }

        return itemStack;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getUseDuration(ItemStack itemStack) {
        return 32;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public UseAction getUseAnimation(ItemStack itemStack) {
        return UseAction.EAT;
    }

    /**
     * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see
     * {@link #onItemUse}.
     */
    public ActionResult<ItemStack> use(World level, PlayerEntity playerEntity, Hand hand) {
        return DrinkHelper.useDrink(level, playerEntity, hand);
    }

}
