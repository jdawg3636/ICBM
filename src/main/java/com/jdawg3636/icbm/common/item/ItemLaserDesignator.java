package com.jdawg3636.icbm.common.item;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.ITileHasRadioFrequency;
import com.jdawg3636.icbm.common.block.launcher_control_panel.ITileLaunchControlPanel;
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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

public class ItemLaserDesignator extends ItemEnergy {

    public static final int ENERGY_CAPACITY = 100_000; // todo make configurable
    public static final int ENERGY_CONSUMED_PER_USAGE = 1_000; // todo make configurable

    public ItemLaserDesignator() {
        this(new Item.Properties().stacksTo(1).tab(ICBMReference.CREATIVE_TAB));
    }

    public ItemLaserDesignator(Properties properties) {
        super(properties);
    }

    @Override
    public int getEnergyCapacity() {
        return ENERGY_CAPACITY;
    }

    @Override
    public boolean getIsInfinite() {
        return false;
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        if(!level.isClientSide()) {
            RayTraceResult rayTraceResult = player.pick(1000, 0F, false);
            if(rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
                final Vector3d target = rayTraceResult.getLocation();
                final BlockPos targetBlockPos = ((BlockRayTraceResult)rayTraceResult).getBlockPos();
                final TileEntity targetTileEntity = level.getBlockEntity(targetBlockPos);
                // If target block has a frequency, set ours to match
                if(targetTileEntity instanceof ITileHasRadioFrequency) {
                    final int frequency = ((ITileHasRadioFrequency)targetTileEntity).getRadioFrequency();
                    player.getItemInHand(hand).getOrCreateTag().putInt("frequency", frequency);
                    player.sendMessage(new TranslationTextComponent("message.icbm.laser_designator.frequency_set", frequency), Util.NIL_UUID);
                }
                // Otherwise, attempt to launch
                else {
                    // Validate frequency
                    final int itemStackFrequency = player.getItemInHand(hand).getOrCreateTag().getInt("frequency");
                    if(itemStackFrequency == 0) {
                        player.sendMessage(new TranslationTextComponent("message.icbm.laser_designator.invalid_frequency", itemStackFrequency).withStyle(TextFormatting.RED), Util.NIL_UUID);
                    }
                    // Validate energy
                    else if(player.getItemInHand(hand).getCapability(CapabilityEnergy.ENERGY).map(es -> es.extractEnergy(ENERGY_CONSUMED_PER_USAGE, false)).orElse(0) != ENERGY_CONSUMED_PER_USAGE) {
                        player.sendMessage(new TranslationTextComponent("message.icbm.laser_designator.out_of_energy").withStyle(TextFormatting.RED), Util.NIL_UUID);
                    }
                    // Good to go - find all launch control panels in range and perform launch
                    else {
                        final double distanceToTarget = target.distanceTo(player.position());
                        player.sendMessage(new TranslationTextComponent("message.icbm.laser_designator.launched", itemStackFrequency, targetBlockPos.getX(), targetBlockPos.getY(), targetBlockPos.getZ(), (((int)(distanceToTarget*1000))/1000D)).withStyle(TextFormatting.GRAY).withStyle(TextFormatting.ITALIC), Util.NIL_UUID);
                        for (TileEntity blockEntityInLevel : level.blockEntityList) {
                            if(blockEntityInLevel instanceof ITileLaunchControlPanel && ((ITileLaunchControlPanel)blockEntityInLevel).getRadioFrequency() == itemStackFrequency) {
                                ((ITileLaunchControlPanel)blockEntityInLevel).setTargetX(target.x);
                                ((ITileLaunchControlPanel)blockEntityInLevel).setTargetY(target.y);
                                ((ITileLaunchControlPanel)blockEntityInLevel).setTargetZ(target.z);
                                ITileLaunchControlPanel.MissileRemoteLaunchResult launchResult = ((ITileLaunchControlPanel)blockEntityInLevel).launchMissileUsingFrequency(itemStackFrequency);
                                BlockPos blockPos = blockEntityInLevel.getBlockPos();
                                switch (launchResult) {
                                    case SUCCESSFUL:
                                        player.sendMessage(new TranslationTextComponent("message.icbm.remote_detonator.launch_successful", blockPos.getX(), blockPos.getY(), blockPos.getZ()).withStyle(TextFormatting.GREEN), Util.NIL_UUID);
                                        break;
                                    case DISRUPTED:
                                        player.sendMessage(new TranslationTextComponent("message.icbm.remote_detonator.launch_disrupted", blockPos.getX(), blockPos.getY(), blockPos.getZ()).withStyle(TextFormatting.RED), Util.NIL_UUID);
                                        break;
                                    case FAILED_LAUNCH:
                                        player.sendMessage(new TranslationTextComponent("message.icbm.remote_detonator.launch_failed", blockPos.getX(), blockPos.getY(), blockPos.getZ()).withStyle(TextFormatting.RED), Util.NIL_UUID);
                                        break;
                                    default:
                                        // If wrong frequency or unsupported operation, silently move on and don't inform the player.
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return ActionResult.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

}
