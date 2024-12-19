package com.jdawg3636.icbm.common.item;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.capability.missiledirector.MissileSourceType;
import com.jdawg3636.icbm.common.entity.EntityMissile;
import com.jdawg3636.icbm.common.reg.ICBMTags;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShootableItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import java.util.function.Predicate;

public class ItemRocketLauncher extends ShootableItem {

    public ItemRocketLauncher() {
        this(new Item.Properties().tab(ICBMReference.CREATIVE_TAB).stacksTo(1));
    }

    public ItemRocketLauncher(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {

        if(!level.isClientSide()) {

            // Identify Ammo Type
            final ItemStack itemstack = player.getProjectile(this.getDefaultInstance());
            EntityMissile entity = null;
            if(itemstack.getItem() instanceof ItemMissile) {
                if(((ItemMissile)itemstack.getItem()).getPercentageFuelFilled(itemstack) >= 1.0) {
                    entity = ((ItemMissile) itemstack.getItem()).getMissileEntity().get().create(level);
                }
                else {
                    player.sendMessage(new TranslationTextComponent("message." + ICBMReference.MODID + ".rocket_launcher.no_fuel", itemstack.getDisplayName()), Util.NIL_UUID);
                }
            }

            // If ammo was present and missile created....
            if(entity != null) {

                // Calculate Missile Data
                final Vector3d playerViewVector = player.getLookAngle().normalize();
                final int sourcePosX = (int) (player.getX() + playerViewVector.x);
                final int sourcePosY = (int) (player.getY() + playerViewVector.y + 1);
                final int sourcePosZ = (int) (player.getZ() + playerViewVector.z);
                final int destPosX = sourcePosX + (int)playerViewVector.scale(20).x;
                final int destPosY = sourcePosY + (int)playerViewVector.scale(20).y;
                final int destPosZ = sourcePosZ + (int)playerViewVector.scale(20).z;
                final int totalFlightTicks = 10;

                // Initialize, Spawn, and Launch the missile entity
                entity.updateMissileData(new BlockPos(sourcePosX, sourcePosY, sourcePosZ), new BlockPos(destPosX, destPosY, destPosZ), null, totalFlightTicks, MissileSourceType.ROCKET_LAUNCHER);
                final Vector3d initialPosition = entity.getPathFunction().apply(1);
                final Vector3d initialRotation = entity.getGradientFunction().apply(initialPosition);
                entity.addEntityToLevel(initialPosition, initialRotation);
                boolean successfulLaunch = entity.launchMissile();

                if(successfulLaunch) {
                    // Log Missile Path
                    if (ICBMReference.COMMON_CONFIG.getDoLogMissilePathsHandheld()) {
                        ICBMReference.logger().printf(Level.INFO, "Launching Missile '%s' from source '%s' at (%s, %s, %s) to (%s, %s, %s) with %s ticks of flight time.", entity.getName().getString(), MissileSourceType.ROCKET_LAUNCHER.toString(), sourcePosX, sourcePosY, sourcePosZ, destPosX, destPosY, destPosZ, totalFlightTicks);
                    }

                    // Take ammo from player (unless in creative mode - using same check as vanilla bow)
                    if (!player.abilities.instabuild) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            player.inventory.removeItem(itemstack);
                        }
                    }
                }
                else {
                    player.sendMessage(new TranslationTextComponent("message.icbm.rocket_launcher.launch_failed", entity.getName()), Util.NIL_UUID);
                    entity.kill();
                }

            }
            return ActionResult.consume(player.getItemInHand(hand));
        }
        else {
            return ActionResult.pass(player.getItemInHand(hand));
        }

    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return (ItemStack itemStack) -> {
            return itemStack.getItem().is(ICBMTags.Items.MISSILES);
        };
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    /**
     * This is used internally by MC's mob AI but is otherwise irrelevant; just returning 0 rather than actually attempting to calculate.
     */
    @Override
    public int getDefaultProjectileRange() {
        return 0;
    }

}
