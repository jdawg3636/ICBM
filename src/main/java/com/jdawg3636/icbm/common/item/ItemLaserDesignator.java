package com.jdawg3636.icbm.common.item;

import com.jdawg3636.icbm.common.block.launcher_control_panel.ILaunchControlPanel;
import net.minecraft.entity.player.PlayerEntity;
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

public class ItemLaserDesignator extends ItemRadarGun {

    public ItemLaserDesignator() {
        super();
    }

    public ItemLaserDesignator(Properties properties) {
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
                    final int frequency = ((ILaunchControlPanel)targetTileEntity).getRadioFrequency();
                    player.getItemInHand(hand).getOrCreateTag().putInt("frequency", frequency);
                    player.sendMessage(new TranslationTextComponent("message.icbm.laser_designator.frequency_set", frequency), Util.NIL_UUID);
                }
                else {
                    final int itemStackFrequency = player.getItemInHand(hand).getOrCreateTag().getInt("frequency");
                    if(itemStackFrequency == 0) {
                        player.sendMessage(new TranslationTextComponent("message.icbm.laser_designator.invalid_frequency", itemStackFrequency), Util.NIL_UUID);
                    }
                    else {
                        for (TileEntity blockEntityInLevel : level.blockEntityList) {
                            if(blockEntityInLevel instanceof ILaunchControlPanel && ((ILaunchControlPanel)blockEntityInLevel).getRadioFrequency() == itemStackFrequency) {
                                ((ILaunchControlPanel)blockEntityInLevel).setTargetX(target.x);
                                ((ILaunchControlPanel)blockEntityInLevel).setTargetY(target.y);
                                ((ILaunchControlPanel)blockEntityInLevel).setTargetZ(target.z);
                                ((ILaunchControlPanel)blockEntityInLevel).launchMissile();
                            }
                        }
                        final double distanceToTarget = target.distanceTo(player.getPosition(0F));
                        player.sendMessage(new TranslationTextComponent("message.icbm.laser_designator.launched", itemStackFrequency, targetBlockPos.getX(), targetBlockPos.getY(), targetBlockPos.getZ(), (((int)(distanceToTarget*1000))/1000D)), Util.NIL_UUID);
                    }
                }
            }
        }
        return ActionResult.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

}
