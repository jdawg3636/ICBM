package com.jdawg3636.icbm.common.item;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.launcher_control_panel.ILaunchControlPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ItemRemoteDetonator extends Item {

    public ItemRemoteDetonator() {
        this(new Item.Properties().stacksTo(1).tab(ICBMReference.CREATIVE_TAB));
    }

    public ItemRemoteDetonator(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        if(!level.isClientSide()) {
            final int itemStackFrequency = player.getItemInHand(hand).getOrCreateTag().getInt("frequency");
            if(itemStackFrequency == 0) {
                player.sendMessage(new TranslationTextComponent("message.icbm.remote_detonator.invalid_frequency", itemStackFrequency), Util.NIL_UUID);
            }
            else {
                for (TileEntity blockEntityInLevel : level.blockEntityList) {
                    if(blockEntityInLevel instanceof ILaunchControlPanel && ((ILaunchControlPanel)blockEntityInLevel).getRadioFrequency() == itemStackFrequency) {
                        ((ILaunchControlPanel)blockEntityInLevel).launchMissile();
                    }
                }
                player.sendMessage(new TranslationTextComponent("message.icbm.remote_detonator.launched", itemStackFrequency), Util.NIL_UUID);
            }
        }
        return ActionResult.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        TileEntity targetTileEntity = context.getLevel().getBlockEntity(context.getClickedPos());
        if(targetTileEntity instanceof ILaunchControlPanel) {
            if(!context.getLevel().isClientSide && context.getPlayer() != null) {
                final int frequency = ((ILaunchControlPanel)targetTileEntity).getRadioFrequency();
                context.getPlayer().getItemInHand(context.getHand()).getOrCreateTag().putInt("frequency", frequency);
                context.getPlayer().sendMessage(new TranslationTextComponent("message.icbm.remote_detonator.frequency_set", frequency), Util.NIL_UUID);
            }
            return ActionResultType.sidedSuccess(context.getLevel().isClientSide);
        }
        return super.useOn(context);
    }

}
