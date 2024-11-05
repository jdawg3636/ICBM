package com.jdawg3636.icbm.common.item;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.launcher_control_panel.ITileLaunchControlPanel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

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
            if(rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
                final Vector3d target = rayTraceResult.getLocation();
                final BlockPos targetBlockPos = ((BlockRayTraceResult)rayTraceResult).getBlockPos();
                final TileEntity targetTileEntity = level.getBlockEntity(targetBlockPos);
                if(targetTileEntity instanceof ITileLaunchControlPanel) {
                    final ItemStack itemStackInHand = player.getItemInHand(hand);
                    if(hasCoordinates(itemStackInHand)) {
                        ((ITileLaunchControlPanel)targetTileEntity).setTargetX(itemStackInHand.getOrCreateTag().getDouble("target_x"));
                        ((ITileLaunchControlPanel)targetTileEntity).setTargetY(itemStackInHand.getOrCreateTag().getDouble("target_y"));
                        ((ITileLaunchControlPanel)targetTileEntity).setTargetZ(itemStackInHand.getOrCreateTag().getDouble("target_z"));
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
            else {
                player.getItemInHand(hand).removeTagKey("target_x");
                player.getItemInHand(hand).removeTagKey("target_y");
                player.getItemInHand(hand).removeTagKey("target_z");
                player.sendMessage(new TranslationTextComponent("message.radarGun.cleared"), Util.NIL_UUID);
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

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemStack, @Nullable World level, List<ITextComponent> tooltip, ITooltipFlag flag) {
        if(hasCoordinates(itemStack)) {
            CompoundNBT nbt = itemStack.getOrCreateTag();
            tooltip.add(new TranslationTextComponent("icbm.info.radarGun.coordinates"));
            tooltip.add(new StringTextComponent(String.format("X: %.3f", nbt.getDouble("target_x"))));
            tooltip.add(new StringTextComponent(String.format("Y: %.3f", nbt.getDouble("target_y"))));
            tooltip.add(new StringTextComponent(String.format("Z: %.3f", nbt.getDouble("target_z"))));
        }
    }

}
