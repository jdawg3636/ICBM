package com.jdawg3636.icbm.common.item;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.capability.ICBMCapabilities;
import com.jdawg3636.icbm.common.capability.trackingmanager.ITrackingManagerCapability;
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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

public class ItemTracker extends Item {

    public ItemTracker() {
        this(new Item.Properties());
    }

    public ItemTracker(Item.Properties properties) {
        super(properties.stacksTo(1).tab(ICBMReference.CREATIVE_TAB));
    }

    @Override
    public void inventoryTick(ItemStack itemStack, World levelCurrent, Entity entity, int slot, boolean isSelected) {
        super.inventoryTick(itemStack, levelCurrent, entity, slot, isSelected);
        if(levelCurrent != null && !levelCurrent.isClientSide() && ServerLifecycleHooks.getCurrentServer() != null) {
            CompoundNBT existingData = itemStack.getOrCreateTag();
            World levelOverworld = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD);
            if (levelOverworld != null && !levelOverworld.isClientSide() && (isSelected || entity instanceof PlayerEntity && ((PlayerEntity) entity).getOffhandItem() == itemStack) && existingData.contains("tracking_ticket") && levelCurrent instanceof ServerWorld) {
                LazyOptional<ITrackingManagerCapability> cap = levelOverworld.getCapability(ICBMCapabilities.TRACKING_MANAGER_CAPABILITY);
                if (cap.isPresent()) {
                    Vector3d targetPos = cap.orElse(null).getPos((ServerWorld) levelCurrent, existingData.getUUID("tracking_ticket"));
                    if (targetPos != null) {
                        itemStack.getOrCreateTag().put("target_x", DoubleNBT.valueOf(targetPos.x));
                        itemStack.getOrCreateTag().put("target_z", DoubleNBT.valueOf(targetPos.z));
                    } else {
                        itemStack.getOrCreateTag().remove("target_x");
                        itemStack.getOrCreateTag().remove("target_z");
                    }
                }
            }
        }
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack itemStack, PlayerEntity player, LivingEntity target, Hand hand) {

        if(player != null && player.level != null && !player.level.isClientSide()) {
            World levelOverworld = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD);
            LazyOptional<ITrackingManagerCapability> cap = levelOverworld.getCapability(ICBMCapabilities.TRACKING_MANAGER_CAPABILITY);
            if(cap.isPresent()) {

                // Register with Ticket Manager
                try {
                    final UUID oldTicketID = itemStack.getOrCreateTag().getUUID("tracking_ticket");
                    cap.orElse(null).deleteTicket(oldTicketID);
                } catch (Exception ignored) { /* thrown when no previous ticket exists */ }
                final UUID ticketID = cap.orElse(null).createTicket(target.getUUID());

                // Update ItemStack
                ItemStack itemStack1 = itemStack;
                player.inventory.removeItem(itemStack);
                itemStack1.getOrCreateTag().putUUID("tracking_ticket", ticketID);
                if (hand == Hand.MAIN_HAND) {
                    player.inventory.setItem(player.inventory.selected, itemStack1);
                } else {
                    player.inventory.offhand.set(0, itemStack1);
                }

                // Return
                return ActionResultType.sidedSuccess(player.level.isClientSide());

            }
        }

        return ActionResultType.PASS;

    }

    public static float getHasTargetFromItemStack(ItemStack itemStack, @Nullable ClientWorld level, @Nullable LivingEntity livingEntity) {
        final CompoundNBT itemStackData = itemStack.getOrCreateTag();
        if(itemStackData.contains("target_x") && itemStackData.contains("target_z")) {
            return 1F;
        }
        return 0F;
    }

    // Implements functional interface net.minecraft.item.IItemPropertyGetter (Minecraft 1.16, MCP Class Name)
    // Derived from vanilla compass behavior in net.minecraft.item.ItemModelsProperties (Minecraft 1.16, MCP Class Name)
    public static float getAngleFromItemStack(ItemStack itemStack, @Nullable ClientWorld level, @Nullable LivingEntity livingEntity) {

        Entity sourceEntity = livingEntity != null ? (Entity)livingEntity : itemStack.getEntityRepresentation();
        if(sourceEntity == null) {
            return 0F;
        }

        final CompoundNBT itemStackData = itemStack.getOrCreateTag();
        final Random random = new Random();
        final double targetX = itemStackData.contains("target_x") ? itemStackData.getDouble("target_x") : 0D;
        final double targetZ = itemStackData.contains("target_z") ? itemStackData.getDouble("target_z") : 0D;

        double angleOfSource = 0.0D;
        if(livingEntity instanceof PlayerEntity && ((PlayerEntity)livingEntity).isLocalPlayer()) {
            angleOfSource = livingEntity.yRot;
        } else if(sourceEntity instanceof ItemFrameEntity) {
            final ItemFrameEntity itemFrameEntity = (ItemFrameEntity) sourceEntity;
            final Direction direction = itemFrameEntity.getDirection();
            final int j = direction.getAxis().isVertical() ? 90 * direction.getAxisDirection().getStep() : 0;
            angleOfSource = MathHelper.wrapDegrees(180 + direction.get2DDataValue() * 90L + itemFrameEntity.getRotation() * 45L + j);
        } else if(sourceEntity instanceof ItemEntity) {
            angleOfSource = 180.0F - ((ItemEntity)sourceEntity).getSpin(0.5F) / ((float)Math.PI * 2F) * 360.0F;
        } else if(livingEntity != null) {
            angleOfSource = livingEntity.yBodyRot;
        }

        double rawAngleToTarget = Math.atan2(targetZ - sourceEntity.getZ(), targetX - sourceEntity.getX()) / (double)((float)Math.PI * 2F);
        double adjustedAngleToTarget = 0.5D - (MathHelper.positiveModulo(angleOfSource / 360.0D, 1.0D) - 0.25D - rawAngleToTarget);

        return MathHelper.positiveModulo((float)adjustedAngleToTarget, 1.0F);

    }

    // Prevent Reequip Animation when Target Position is Updated
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || !oldStack.getItem().equals(newStack.getItem());
    }

}
