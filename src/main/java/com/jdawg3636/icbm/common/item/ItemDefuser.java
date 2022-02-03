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
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ItemDefuser extends Item {

    public ItemDefuser() {
        this(new Item.Properties().tab(ICBMReference.CREATIVE_TAB));
    }

    public ItemDefuser(Properties properties) {
        super(properties);
    }

    public static void onInteractWithEntity(final PlayerInteractEvent.EntityInteractSpecific event) {
        if(!event.getTarget().level.isClientSide()) {
            if(event.getTarget() instanceof TNTEntity || event.getTarget() instanceof TNTMinecartEntity || event.getTarget() instanceof EntityGrenade) {
                ItemStack toDrop = event.getTarget().getPickedResult(null);
                if(toDrop == null || toDrop.equals(ItemStack.EMPTY)) {
                    if(event.getTarget().getType().equals(EntityType.TNT)) {
                        toDrop = Items.TNT.getDefaultInstance();
                    }
                    else {
                        return;
                    }
                }
                event.getTarget().spawnAtLocation(toDrop);
                event.getTarget().kill();
                event.setCancellationResult(ActionResultType.SUCCESS);
            }
        }
    }

}
