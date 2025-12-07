package com.jdawg3636.icbm.common.item;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.ITileHasRadioFrequency;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public abstract class ItemFrequency extends ItemEnergy {

    public ItemFrequency(Properties properties) {
        super(properties);
    }

    public static int getFrequency(ItemStack itemStack) {
        if(!(itemStack.getItem() instanceof ItemFrequency)) return 0;
        // If this call fails, it does so silently and returns 0. That happens to be exactly what we want.
        return itemStack.getOrCreateTag().getInt("frequency");
    }

    public static void setFrequency(ItemStack itemStack, int frequency) {
        if(!(itemStack.getItem() instanceof ItemFrequency)) return;
        if(frequency < 0) return;
        itemStack.getOrCreateTag().putInt("frequency", frequency);
    }

    public abstract IFormattableTextComponent getFrequencySetMessage(int frequency);

    public abstract IFormattableTextComponent getFrequencyTooltip(int frequency);

    public abstract IFormattableTextComponent getFrequencyMissingTooltip();

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World level, List<ITextComponent> tooltip, ITooltipFlag tooltipFlag) {
        if(level == null) return;
        int frequency = ItemFrequency.getFrequency(stack);
        if(frequency != 0) {
            tooltip.add(getFrequencyTooltip(frequency).withStyle(TextFormatting.GREEN));
        }
        else {
            tooltip.add(getFrequencyMissingTooltip().withStyle(TextFormatting.RED));
        }
        super.appendHoverText(stack, level, tooltip, tooltipFlag);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        TileEntity targetTileEntity = context.getLevel().getBlockEntity(context.getClickedPos());
        if(targetTileEntity instanceof ITileHasRadioFrequency) {
            if(!context.getLevel().isClientSide && context.getPlayer() != null) {
                final int frequency = ((ITileHasRadioFrequency)targetTileEntity).getRadioFrequency();
                ItemFrequency.setFrequency(context.getPlayer().getItemInHand(context.getHand()), frequency);
                context.getPlayer().sendMessage(getFrequencySetMessage(frequency), Util.NIL_UUID);
            }
            return ActionResultType.sidedSuccess(context.getLevel().isClientSide);
        }
        return super.useOn(context);
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        // If on server, do nothing and consume the event.
        if(!level.isClientSide) {
            return super.use(level, player, hand);
        }
        // Identify the slot of the held item
        int slot = 0;
        if (hand == Hand.MAIN_HAND) {
            slot = player.inventory.selected;
            // I don't know how/when this would be false, but vanilla code checks it so we will too.
            if(!PlayerInventory.isHotbarSlot(slot)) {
                return super.use(level, player, hand);
            }
        }
        else if (hand == Hand.OFF_HAND) {
            // We could hard-code the slot index of the offhand, but it's safer to calculate it at runtime
            // in case another mod has modified the inventory in some way.
            for(NonNullList<ItemStack> compartment : player.inventory.compartments) {
                if(compartment != player.inventory.offhand) {
                    slot += compartment.size();
                } else {
                    break;
                }
            }
        }
        // Open the Frequency GUI
        ICBMReference.distProxy().setScreenFrequencyItem(slot);
        // Return success (consume the right-click event)
        ItemStack itemStack = player.inventory.getItem(slot);
        return ActionResult.sidedSuccess(itemStack, true);
    }

}
