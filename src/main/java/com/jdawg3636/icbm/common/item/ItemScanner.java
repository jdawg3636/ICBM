package com.jdawg3636.icbm.common.item;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.capability.ICBMCapabilities;
import com.jdawg3636.icbm.common.capability.trackingmanager.ITrackingManagerCapability;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class ItemScanner extends Item {

    public ItemScanner() {
        this(new Item.Properties());
    }

    public ItemScanner(Item.Properties properties) {
        super(properties.stacksTo(1).tab(ICBMReference.CREATIVE_TAB));
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack itemStack, PlayerEntity player, LivingEntity target, Hand hand) {
        if(player.level != null && !player.level.isClientSide()) {
            World levelOverworld = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD);
            LazyOptional<ITrackingManagerCapability> cap = levelOverworld.getCapability(ICBMCapabilities.TRACKING_MANAGER_CAPABILITY);
            if(cap.isPresent()) {
                cap.orElse(null).clearTickets(target.getUUID());
                player.sendMessage(new TranslationTextComponent("chat.icbm.scannerconfirm.other", target.getName()), Util.NIL_UUID);
            }
        }
        return ActionResultType.sidedSuccess(player.level.isClientSide());
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        if(!level.isClientSide()) {
            World levelOverworld = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD);
            LazyOptional<ITrackingManagerCapability> cap = levelOverworld.getCapability(ICBMCapabilities.TRACKING_MANAGER_CAPABILITY);
            if(cap.isPresent()) {
                cap.orElse(null).clearTickets(player.getUUID());
                player.sendMessage(new TranslationTextComponent("chat.icbm.scannerconfirm.self", player.getName()), Util.NIL_UUID);
            }
        }
        return ActionResult.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

}
