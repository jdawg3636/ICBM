package com.jdawg3636.icbm.common.item;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.entity.EntityGrenade;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.item.minecart.TNTMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Util;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ItemDefuser extends ItemEnergy {

    public static final int ENERGY_CONSUMED_PER_USAGE = 1_000;

    public ItemDefuser() {
        this(new Item.Properties().tab(ICBMReference.CREATIVE_TAB));
    }

    public ItemDefuser(Properties properties) {
        super(properties);
    }

    @Override
    public int getEnergyCapacity() {
        return 100_000; // todo make configurable
    }

    @Override
    public boolean getIsInfinite() {
        return false;
    }

    public static void onInteractWithEntity(final PlayerInteractEvent.EntityInteractSpecific event) {
        // Only run on server
        if(!event.getTarget().level.isClientSide()) {
            // Validate defusable
            if(event.getTarget() instanceof TNTEntity || event.getTarget() instanceof TNTMinecartEntity || event.getTarget() instanceof EntityGrenade) {
                // Validate energy
                if(event.getPlayer().getItemInHand(event.getHand()).getCapability(CapabilityEnergy.ENERGY).map(es -> es.extractEnergy(ENERGY_CONSUMED_PER_USAGE, false)).orElse(0) == ENERGY_CONSUMED_PER_USAGE) {
                    ItemStack toDrop = event.getTarget().getPickedResult(null);
                    if (toDrop == null || toDrop.equals(ItemStack.EMPTY)) {
                        if (event.getTarget().getType().equals(EntityType.TNT)) {
                            toDrop = Items.TNT.getDefaultInstance();
                        } else {
                            return;
                        }
                    }
                    event.getTarget().spawnAtLocation(toDrop);
                    event.getTarget().kill();
                    event.setCancellationResult(ActionResultType.SUCCESS);
                }
                else {
                    event.getPlayer().sendMessage(new TranslationTextComponent("message.icbm.defuser.out_of_energy").withStyle(TextFormatting.RED), Util.NIL_UUID);
                }
            }
        }
    }

}
