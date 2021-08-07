package com.jdawg3636.icbm.common.item;

import com.jdawg3636.icbm.ICBM;
import com.jdawg3636.icbm.common.entity.EntityMissile;
import com.jdawg3636.icbm.common.reg.EntityReg;
import net.minecraft.command.impl.data.EntityDataAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;

import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.apache.logging.log4j.Level;

public class ItemRocketLauncher extends Item {

    public ItemRocketLauncher(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        if(!level.isClientSide()) {
            EntityMissile entity = EntityReg.MISSILE_INCENDIARY.get().create(level);
            if(entity != null) {

                Vector3d playerViewVector = player.getLookAngle();
                playerViewVector = playerViewVector.normalize();

                int sourcePosX = (int) (player.getX() + playerViewVector.x);
                int sourcePosY = (int) (player.getY() + playerViewVector.y);
                int sourcePosZ = (int) (player.getZ() + playerViewVector.z);

                int destPosX = sourcePosX + (int)playerViewVector.scale(20).x;
                int destPosY = sourcePosY + (int)playerViewVector.scale(20).y;
                int destPosZ = sourcePosZ + (int)playerViewVector.scale(20).z;

                entity.setPos(player.getX(), player.getY() + 2, player.getZ());

                EntityDataAccessor entityDataAccessor = new EntityDataAccessor(entity);
                CompoundNBT data = entityDataAccessor.getData();
                data.putInt("SourcePosX", sourcePosX);
                data.putInt("SourcePosY", sourcePosY);
                data.putInt("SourcePosZ", sourcePosZ);
                data.putInt("DestPosX", destPosX);
                data.putInt("DestPosY", destPosY);
                data.putInt("DestPosZ", destPosZ);
                data.putInt("TotalFlightTicks", 25);
                data.putInt("MissileSourceType", EntityMissile.MissileSourceType.ROCKET_LAUNCHER.ordinal());
                data.putInt("MissileLaunchPhase", EntityMissile.MissileLaunchPhase.LAUNCHED.ordinal());
                try { entityDataAccessor.setData(data); } catch (Exception e) { e.printStackTrace(); }

                level.addFreshEntity(entity);

                ICBM.logger().printf(Level.INFO, "Launching Missile '%s' from source '%s' at (%s, %s, %s) to (%s, %s, %s) with %s ticks of flight time.", entity.getName().getString(), EntityMissile.MissileSourceType.ROCKET_LAUNCHER.toString(), sourcePosX, sourcePosY, sourcePosZ, destPosX, destPosY, destPosZ, 75);

            }
            return ActionResult.consume(player.getItemInHand(hand));
        }
        else return ActionResult.pass(player.getItemInHand(hand));
    }

}
