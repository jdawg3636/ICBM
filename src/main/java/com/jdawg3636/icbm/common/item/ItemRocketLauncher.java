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

                entity.setPos(player.getX(), player.getY() + 2, player.getZ());

                EntityDataAccessor entityDataAccessor = new EntityDataAccessor(entity);
                CompoundNBT data = entityDataAccessor.getData();
                data.putInt("SourcePosX", ((int) player.getX()));
                data.putInt("SourcePosY", ((int) player.getY()));
                data.putInt("SourcePosZ", ((int) player.getZ()));
                data.putInt("DestPosX", ((int) player.getX()+50));
                data.putInt("DestPosY", ((int) player.getY()+20));
                data.putInt("DestPosZ", ((int) player.getZ()+50));
                data.putInt("TotalFlightTicks", 2000);
                data.putInt("MissileSourceType", EntityMissile.MissileSourceType.ROCKET_LAUNCHER.ordinal());
                data.putInt("MissileLaunchPhase", EntityMissile.MissileLaunchPhase.LAUNCHED.ordinal());
                try { entityDataAccessor.setData(data); } catch (Exception e) { e.printStackTrace(); }

                level.addFreshEntity(entity);

                ICBM.logger().printf(Level.INFO, "Launching Missile '%s' from (%s, %s, %s) to (%s, %s, %s) with %s ticks of flight time.", entity.getName().getString(), ((int) player.getX()), ((int) player.getY()), ((int) player.getZ()), ((int) player.getX()+50), ((int) player.getY()+20), ((int) player.getZ()+50), 2000);

            }
            return ActionResult.consume(player.getItemInHand(hand));
        }
        else return ActionResult.pass(player.getItemInHand(hand));
    }

}
