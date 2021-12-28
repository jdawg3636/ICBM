package com.jdawg3636.icbm.common.item;

import com.jdawg3636.icbm.ICBMReference;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemTracker extends Item {

    public ItemTracker() {
        this(new Item.Properties());
    }

    public ItemTracker(Item.Properties properties) {
        super(properties.stacksTo(1).tab(ICBMReference.CREATIVE_TAB));
    }

    @Override
    public void inventoryTick(ItemStack itemStack, World level, Entity entity, int slot, boolean isSelected) {
        super.inventoryTick(itemStack, level, entity, slot, isSelected);
        CompoundNBT existingData = itemStack.getOrCreateTag();
        if(existingData.contains("tracking_ticket")) {
            // todo: set angle based on
        }
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack itemStack, PlayerEntity player, LivingEntity target, Hand hand) {

        // todo: set tracking ticket

        // Debug
        if(player != null && player.level != null) {
            if(!player.level.isClientSide()) {
                ItemStack itemStack1 = itemStack;
                player.inventory.removeItem(itemStack);
                itemStack1.addTagElement("target_x", DoubleNBT.valueOf(target.getX()));
                itemStack1.addTagElement("target_z", DoubleNBT.valueOf(target.getZ()));
                if (hand == Hand.MAIN_HAND) {
                    player.inventory.setItem(player.inventory.selected, itemStack1);
                } else {
                    player.inventory.offhand.set(0, itemStack1);
                }
            }
            return ActionResultType.sidedSuccess(player.level.isClientSide());
        }

        // Default
        return ActionResultType.PASS;
        //return super.interactLivingEntity(itemStack, player, target, hand);

    }

    // Implements functional interface net.minecraft.item.IItemPropertyGetter (Minecraft 1.16, MCP Class Name)
    // Derived from vanilla compass behavior in net.minecraft.item.ItemModelsProperties (Minecraft 1.16, MCP Class Name)
    public static float getAngleFromItemStack(ItemStack itemStack, @Nullable ClientWorld level, @Nullable LivingEntity livingEntity) {

        Entity sourceEntity = livingEntity != null ? (Entity)livingEntity : itemStack.getEntityRepresentation();
        if(sourceEntity == null) {
            return 0F;
        }

        final CompoundNBT itemStackData = itemStack.getOrCreateTag();
        final double targetX = itemStackData.contains("target_x") ? itemStackData.getDouble("target_x") : 0F;
        final double targetZ = itemStackData.contains("target_z") ? itemStackData.getDouble("target_z") : 0F;

        double angleOfSource = 0.0D;
        if (livingEntity instanceof PlayerEntity && ((PlayerEntity)livingEntity).isLocalPlayer()) {
            angleOfSource = livingEntity.yRot;
        } else if (sourceEntity instanceof ItemFrameEntity) {
            final ItemFrameEntity itemFrameEntity = (ItemFrameEntity) sourceEntity;
            final Direction direction = itemFrameEntity.getDirection();
            final int j = direction.getAxis().isVertical() ? 90 * direction.getAxisDirection().getStep() : 0;
            angleOfSource = MathHelper.wrapDegrees(180 + direction.get2DDataValue() * 90L + itemFrameEntity.getRotation() * 45L + j);
        } else if (sourceEntity instanceof ItemEntity) {
            angleOfSource = 180.0F - ((ItemEntity)sourceEntity).getSpin(0.5F) / ((float)Math.PI * 2F) * 360.0F;
        } else if (livingEntity != null) {
            angleOfSource = livingEntity.yBodyRot;
        }

        double rawAngleToTarget = Math.atan2(targetZ - sourceEntity.getZ(), targetX - sourceEntity.getX()) / (double)((float)Math.PI * 2F);
        double adjustedAngleToTarget = 0.5D - (MathHelper.positiveModulo(angleOfSource / 360.0D, 1.0D) - 0.25D - rawAngleToTarget);

        return MathHelper.positiveModulo((float)adjustedAngleToTarget, 1.0F);

    }

}
