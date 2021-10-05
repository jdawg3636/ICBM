package com.jdawg3636.icbm.common.item;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.entity.EntityMissile;
import com.jdawg3636.icbm.common.reg.EntityReg;
import net.minecraft.command.impl.data.EntityDataAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

public class ItemRocketLauncher extends Item {

    public ItemRocketLauncher(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {

        if(!level.isClientSide()) {

            final EntityMissile entity = EntityReg.MISSILE_INCENDIARY.get().create(level);
            if(entity != null) {

                final Vector3d playerViewVector = player.getLookAngle().normalize();

                final int sourcePosX = (int) (player.getX() + playerViewVector.x);
                final int sourcePosY = (int) (player.getY() + playerViewVector.y + 1);
                final int sourcePosZ = (int) (player.getZ() + playerViewVector.z);

                final int destPosX = sourcePosX + (int)playerViewVector.scale(20).x;
                final int destPosY = sourcePosY + (int)playerViewVector.scale(20).y;
                final int destPosZ = sourcePosZ + (int)playerViewVector.scale(20).z;

                final int totalFlightTicks = 25;

                EntityDataAccessor entityDataAccessor = new EntityDataAccessor(entity);
                CompoundNBT data = entityDataAccessor.getData();
                data.putInt("SourcePosX", sourcePosX);
                data.putInt("SourcePosY", sourcePosY);
                data.putInt("SourcePosZ", sourcePosZ);
                data.putInt("DestPosX", destPosX);
                data.putInt("DestPosY", destPosY);
                data.putInt("DestPosZ", destPosZ);
                data.putInt("TotalFlightTicks", totalFlightTicks);
                data.putInt("MissileSourceType", EntityMissile.MissileSourceType.ROCKET_LAUNCHER.ordinal());
                data.putInt("MissileLaunchPhase", EntityMissile.MissileLaunchPhase.LAUNCHED.ordinal());
                try { entityDataAccessor.setData(data); } catch (Exception e) { e.printStackTrace(); }

                // Initializing prior to spawning, fixes split-second of incorrect rotation.
                Vector3d initialPosition = entity.pathFunction.apply(1);
                Vector3d initialRotation = entity.gradientFunction.apply(initialPosition);
                entity.setPos(initialPosition.x, initialPosition.y, initialPosition.z);
                entity.setRot((float)initialRotation.y, (float)initialRotation.x);

                level.addFreshEntity(entity);

                ICBMReference.logger().printf(Level.INFO, "Launching Missile '%s' from source '%s' at (%s, %s, %s) to (%s, %s, %s) with %s ticks of flight time.", entity.getName().getString(), EntityMissile.MissileSourceType.ROCKET_LAUNCHER.toString(), sourcePosX, sourcePosY, sourcePosZ, destPosX, destPosY, destPosZ, totalFlightTicks);

            }
            return ActionResult.consume(player.getItemInHand(hand));
        }
        else {
            return ActionResult.pass(player.getItemInHand(hand));
        }

    }

}
