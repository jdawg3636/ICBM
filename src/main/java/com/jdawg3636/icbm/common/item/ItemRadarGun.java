package com.jdawg3636.icbm.common.item;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.launcher_control_panel.ILaunchControlPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ItemRadarGun extends Item {

    public ItemRadarGun() {
        this(new Item.Properties().stacksTo(1).tab(ICBMReference.CREATIVE_TAB));
    }

    public ItemRadarGun(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        if(!level.isClientSide()) {
            RayTraceResult rayTraceResult = player.pick(1000, 0F, false);
            if(rayTraceResult != null && rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
                final Vector3d target = rayTraceResult.getLocation();
                final BlockPos targetBlockPos = ((BlockRayTraceResult)rayTraceResult).getBlockPos();
                final TileEntity targetTileEntity = level.getBlockEntity(targetBlockPos);
                if(targetTileEntity instanceof ILaunchControlPanel) {
                    final ItemStack itemStackInHand = player.getItemInHand(hand);
                    if(hasCoordinates(itemStackInHand)) {
                        ((ILaunchControlPanel)targetTileEntity).setTargetX(itemStackInHand.getOrCreateTag().getDouble("target_x"));
                        ((ILaunchControlPanel)targetTileEntity).setTargetY(itemStackInHand.getOrCreateTag().getDouble("target_y"));
                        ((ILaunchControlPanel)targetTileEntity).setTargetZ(itemStackInHand.getOrCreateTag().getDouble("target_z"));
                        player.sendMessage(new TranslationTextComponent("message.radarGun.transfer"), Util.NIL_UUID);
                    }
                    else {
                        player.sendMessage(new TranslationTextComponent("message.radarGun.noCoords"), Util.NIL_UUID);
                    }
                }
                else {
                    player.getItemInHand(hand).getOrCreateTag().putDouble("target_x", target.x);
                    player.getItemInHand(hand).getOrCreateTag().putDouble("target_y", target.y);
                    player.getItemInHand(hand).getOrCreateTag().putDouble("target_z", target.z);
                    final double distanceToTarget = target.distanceTo(player.position());
                    player.sendMessage(new TranslationTextComponent("message.radarGun.scanned", targetBlockPos.getX(), targetBlockPos.getY(), targetBlockPos.getZ(), (((int)(distanceToTarget*1000))/1000D)), Util.NIL_UUID);
                }
            }
        }
        return ActionResult.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

    public boolean hasCoordinates(ItemStack itemStack) {
        if(!(itemStack.getItem() instanceof ItemRadarGun)) return false;
        // tagType of 6 is a Double
        if(!itemStack.getOrCreateTag().contains("target_x", 6)) return false;
        if(!itemStack.getOrCreateTag().contains("target_y", 6)) return false;
        if(!itemStack.getOrCreateTag().contains("target_z", 6)) return false;
        return true;
    }

}
