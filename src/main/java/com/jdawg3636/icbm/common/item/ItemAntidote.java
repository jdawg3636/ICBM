package com.jdawg3636.icbm.common.item;

import com.jdawg3636.icbm.common.reg.EffectReg;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ItemAntidote extends Item {

    public ItemAntidote(Properties properties) {
        super(properties);
    }

    public ItemStack finishUsingItem(ItemStack itemStack, World level, LivingEntity livingEntityUsing) {
        tryApplyAntidote(itemStack, level, livingEntityUsing, livingEntityUsing);
        return itemStack;
    }

    // Adapted from net.minecraft.item.MilkBucketItem::finishUsingItem, MC 1.16.5, MCP Package/Class Names, Mojmap Method/Field Names
    public static boolean tryApplyAntidote(ItemStack antidoteItemStack, World level, LivingEntity applyingEntity, LivingEntity targetEntity) {

        if(!(antidoteItemStack.getItem() instanceof ItemAntidote)) {
            return false;
        }

        if (!level.isClientSide) {
            targetEntity.addEffect(new EffectInstance(EffectReg.ENGINEERED_PATHOGEN_IMMUNITY.get(), 20 * 120));
        }

        if (applyingEntity instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)applyingEntity;
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayerentity, antidoteItemStack);
            serverplayerentity.awardStat(Stats.ITEM_USED.get(antidoteItemStack.getItem()));
        }

        if (applyingEntity instanceof PlayerEntity && !((PlayerEntity)applyingEntity).abilities.instabuild) {
            antidoteItemStack.shrink(1);
        }

        return true;

    }

    public static void onInteractWithEntity(final PlayerInteractEvent.EntityInteractSpecific event) {
        if(event.getWorld().isClientSide()) {
            if(event.getTarget() instanceof LivingEntity) {
                event.setCancellationResult(ActionResultType.sidedSuccess(event.getWorld().isClientSide));
            }
        }
        else {
            // Validate target
            Entity entityToApplyTo = event.getTarget();
            if(!(entityToApplyTo instanceof LivingEntity)) return;
            // Prepare Parameters
            ItemStack antidoteItemStack = event.getPlayer().getItemInHand(event.getHand());
            World level = event.getWorld();
            LivingEntity applyingEntity = event.getPlayer();
            LivingEntity livingEntityToApplyTo = (LivingEntity) entityToApplyTo;
            // Try to apply
            boolean success = tryApplyAntidote(antidoteItemStack, level, applyingEntity, livingEntityToApplyTo);
            // If successful, consume event
            if(success) {
                event.getPlayer().setItemInHand(event.getHand(), antidoteItemStack);
                event.setCancellationResult(ActionResultType.sidedSuccess(level.isClientSide));
            }
        }
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
