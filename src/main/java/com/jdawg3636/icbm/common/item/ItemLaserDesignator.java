package com.jdawg3636.icbm.common.item;

import com.jdawg3636.icbm.ICBMReference;
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
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

public class ItemLaserDesignator extends ItemFrequency {

    public static final int ENERGY_CAPACITY = 100_000; // todo make configurable
    public static final int ENERGY_CONSUMED_PER_USAGE = 1_000; // todo make configurable

    public ItemLaserDesignator() {
        this(new Item.Properties().stacksTo(1).tab(ICBMReference.CREATIVE_TAB));
    }

    public ItemLaserDesignator(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        // (Client OR Server) If sneaking, defer to super to open the frequency GUI.
        if(player.isSecondaryUseActive()) {
            return super.use(level, player, hand);
        }
        // Remaining logic is server-only. If on client, consume and return.
        if(level.isClientSide) {
            return ActionResult.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
        }

        // Validate energy (do not consume)
        if(player.getItemInHand(hand).getCapability(CapabilityEnergy.ENERGY).map(es -> es.extractEnergy(ENERGY_CONSUMED_PER_USAGE, true)).orElse(0) != ENERGY_CONSUMED_PER_USAGE) {
            player.sendMessage(new TranslationTextComponent("message." + ICBMReference.MODID + ".laser_designator.out_of_energy").withStyle(TextFormatting.RED), Util.NIL_UUID);
            return ActionResult.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
        }

        // Perform ray trace - todo verify that this isn't loading chunks. noticing a significant server lag spike when looking horizontally above horizon.
        final RayTraceResult rayTraceResult = player.pick(1000, 0F, false);
        // If not targeting a block, consume and return.
        if(rayTraceResult.getType() != RayTraceResult.Type.BLOCK) {
            return ActionResult.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
        }
        // Extract target info
        final Vector3d target = rayTraceResult.getLocation();
        final BlockPos targetBlockPos = ((BlockRayTraceResult)rayTraceResult).getBlockPos();

        // Validate frequency
        final int itemStackFrequency = player.getItemInHand(hand).getOrCreateTag().getInt("frequency");
        if(itemStackFrequency == 0) {
            player.sendMessage(new TranslationTextComponent("message." + ICBMReference.MODID + ".laser_designator.invalid_frequency", itemStackFrequency).withStyle(TextFormatting.RED), Util.NIL_UUID);
            return ActionResult.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
        }

        // Validate and Consume energy
        if(player.getItemInHand(hand).getCapability(CapabilityEnergy.ENERGY).map(es -> es.extractEnergy(ENERGY_CONSUMED_PER_USAGE, false)).orElse(0) != ENERGY_CONSUMED_PER_USAGE) {
            return ActionResult.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
        }

        // Good to go! Find all launch control panels in range and perform launch
        final double distanceToTarget = target.distanceTo(player.position());
        player.sendMessage(new TranslationTextComponent("message." + ICBMReference.MODID + ".laser_designator.launched", itemStackFrequency, targetBlockPos.getX(), targetBlockPos.getY(), targetBlockPos.getZ(), (((int)(distanceToTarget*1000))/1000D)).withStyle(TextFormatting.GRAY).withStyle(TextFormatting.ITALIC), Util.NIL_UUID);
        for (TileEntity blockEntityInLevel : level.blockEntityList) {
            if(blockEntityInLevel instanceof ITileLaunchControlPanel && ((ITileLaunchControlPanel)blockEntityInLevel).getRadioFrequency() == itemStackFrequency) {
                ((ITileLaunchControlPanel)blockEntityInLevel).setTargetX(target.x);
                ((ITileLaunchControlPanel)blockEntityInLevel).setTargetY(target.y);
                ((ITileLaunchControlPanel)blockEntityInLevel).setTargetZ(target.z);
                ITileLaunchControlPanel.MissileRemoteLaunchResult launchResult = ((ITileLaunchControlPanel)blockEntityInLevel).launchMissileUsingFrequency(itemStackFrequency);
                BlockPos blockPos = blockEntityInLevel.getBlockPos();
                switch (launchResult) {
                    case SUCCESSFUL:
                        player.sendMessage(new TranslationTextComponent("message." + ICBMReference.MODID + ".laser_designator.launch_successful", blockPos.getX(), blockPos.getY(), blockPos.getZ()).withStyle(TextFormatting.GREEN), Util.NIL_UUID);
                        break;
                    case DISRUPTED:
                        player.sendMessage(new TranslationTextComponent("message." + ICBMReference.MODID + ".laser_designator.launch_disrupted", blockPos.getX(), blockPos.getY(), blockPos.getZ()).withStyle(TextFormatting.RED), Util.NIL_UUID);
                        break;
                    case FAILED_LAUNCH:
                        player.sendMessage(new TranslationTextComponent("message." + ICBMReference.MODID + ".laser_designator.launch_failed", blockPos.getX(), blockPos.getY(), blockPos.getZ()).withStyle(TextFormatting.RED), Util.NIL_UUID);
                        break;
                    default:
                        // If wrong frequency or unsupported operation, silently move on and don't inform the player.
                        break;
                }
            }
        }

        // Consume and return
        return ActionResult.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

    @Override
    public IFormattableTextComponent getFrequencySetMessage(int frequency) {
        return new TranslationTextComponent("message." + ICBMReference.MODID + ".laser_designator.frequency_set", frequency);
    }

    @Override
    public IFormattableTextComponent getFrequencyTooltip(int frequency) {
        return new TranslationTextComponent("info." + ICBMReference.MODID + ".laser_designator.frequency", frequency);
    }

    @Override
    public IFormattableTextComponent getFrequencyMissingTooltip() {
        return new TranslationTextComponent("info." + ICBMReference.MODID + ".laser_designator.frequency_not_set");
    }

    @Override
    public int getEnergyCapacity() {
        return ENERGY_CAPACITY;
    }

    @Override
    public boolean getIsInfinite() {
        return false;
    }

}
