package com.jdawg3636.icbm.common.item;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.capability.ICBMCapabilities;
import com.jdawg3636.icbm.common.capability.trackingmanager.ITrackingManagerCapability;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.List;

public class ItemScanner extends Item {

    public ItemScanner() {
        this(new Item.Properties().stacksTo(1).tab(ICBMReference.CREATIVE_TAB));
    }

    public ItemScanner(Item.Properties properties) {
        super(properties);
        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack itemStack, PlayerEntity player, LivingEntity target, Hand hand) {
        if(player.level != null) {
            if(!player.level.isClientSide()) {
                World levelOverworld = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD);
                if (levelOverworld != null) {
                    LazyOptional<ITrackingManagerCapability> capOptional = levelOverworld.getCapability(ICBMCapabilities.TRACKING_MANAGER_CAPABILITY);
                    capOptional.ifPresent((ITrackingManagerCapability cap) -> {
                        cap.clearTickets(target.getUUID());
                        player.sendMessage(new TranslationTextComponent("chat.icbm.scannerconfirm.other", target.getName()), Util.NIL_UUID);
                    });
                }
            }
            return ActionResultType.sidedSuccess(player.level.isClientSide());
        }
        return ActionResultType.PASS;
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        if(!level.isClientSide()) {
            World levelOverworld = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD);
            if(levelOverworld != null) {
                LazyOptional<ITrackingManagerCapability> capOptional = levelOverworld.getCapability(ICBMCapabilities.TRACKING_MANAGER_CAPABILITY);
                capOptional.ifPresent((ITrackingManagerCapability cap) -> {
                    cap.clearTickets(player.getUUID());
                    player.sendMessage(new TranslationTextComponent("chat.icbm.scannerconfirm.self", player.getName()), Util.NIL_UUID);
                });
            }
        }
        return ActionResult.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

    public static final IDispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
        protected ItemStack execute(IBlockSource blockSource, ItemStack itemStack) {
            ItemScanner.dispense(blockSource);
            return itemStack;
        }
    };

    public static void dispense(IBlockSource blockSource) {

        BlockPos blockPos = blockSource.getPos().relative((Direction)blockSource.getBlockState().getValue(DispenserBlock.FACING));
        List<LivingEntity> candidateTargetList = blockSource.getLevel().getEntitiesOfClass(LivingEntity.class, new AxisAlignedBB(blockPos), EntityPredicates.NO_SPECTATORS);

        if (candidateTargetList.isEmpty()) return;

        final LivingEntity target = (LivingEntity)candidateTargetList.get(0);
        World levelOverworld = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD);
        if(levelOverworld != null) {
            LazyOptional<ITrackingManagerCapability> capOptional = levelOverworld.getCapability(ICBMCapabilities.TRACKING_MANAGER_CAPABILITY);
            capOptional.ifPresent((ITrackingManagerCapability cap) -> {
                cap.clearTickets(target.getUUID());
                if (target instanceof PlayerEntity) {
                    target.sendMessage(new TranslationTextComponent("chat.icbm.scannerconfirm.self", target.getName()), Util.NIL_UUID);
                }
            });
        }

    }

}
