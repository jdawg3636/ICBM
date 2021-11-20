package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.common.reg.SoundEventReg;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SExplosionPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * Utility Class for use in Blast Event implementations
 */
public class ICBMBlastEventUtil {

    public static void doBlastSoundAndParticles(AbstractBlastEvent event) {
        // Loosely Based on net.minecraft.world.Explosion::finalizeExplosion (MCP Class Names and Package Structure, Official Method/Field Mappings, Minecraft 1.16.5)
        if (!event.getBlastWorld().isClientSide) {
            // Sound
            Supplier<SoundEvent> soundEvent = event.getSoundEvent();
            if(soundEvent == null) soundEvent = SoundEventReg.EXPLOSION_GENERIC;
            event.getBlastWorld().playSound((PlayerEntity) null, event.getBlastPosition().getX(), event.getBlastPosition().getY(), event.getBlastPosition().getZ(), soundEvent.get(), SoundCategory.BLOCKS, event.getSoundEventRangeMultiplier(), 1.0F);
            // Particles - Handled Client Side by net.minecraft.client.network.play.ClientPlayNetHandler::handleParticleEvent (MCP Class Names and Package Structure, Official Method/Field Mappings, Minecraft 1.16.5)
            event.getBlastWorld().sendParticles(ParticleTypes.EXPLOSION_EMITTER, event.getBlastPosition().getX(), event.getBlastPosition().getY(), event.getBlastPosition().getZ(), 1, 0, 0, 0, 1.0D);
        }
    }

    public static void doVanillaExplosion(AbstractBlastEvent event) {
        doVanillaExplosion(event, 4.0F);
    }

    public static void doVanillaExplosion(AbstractBlastEvent event, float explosionPower) {
        doVanillaExplosion(event.getBlastWorld(), event.getBlastPosition().getX(), event.getBlastPosition().getY(), event.getBlastPosition().getZ(), explosionPower);
    }

    public static void doVanillaExplosion(ServerWorld level, BlockPos blockPos, float explosionPower) {
        level.explode(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), explosionPower, Explosion.Mode.BREAK);
    }

    public static void doVanillaExplosion(ServerWorld level, double posX, double posY, double posZ, float explosionPower) {
        level.explode(null, posX, posY, posZ, explosionPower, Explosion.Mode.BREAK);
    }

    public static void doVanillaExplosionServerOnly(ServerWorld level, BlockPos blockPos) {
        doVanillaExplosionServerOnly(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 4.0F);
    }

    public static void doVanillaExplosionServerOnly(ServerWorld level, BlockPos blockPos, float explosionPower) {
        doVanillaExplosionServerOnly(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), explosionPower);
    }

    public static void doVanillaExplosionServerOnly(ServerWorld level, double posX, double posY, double posZ, float explosionPower) {
        Explosion explosion = new Explosion(level, null, null, null, posX, posY, posZ, explosionPower, false, Explosion.Mode.BREAK);
        if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(level, explosion)) {
            return;
        }
        explosion.explode();
        explosion.finalizeExplosion(false);
    }

    /**
     * Shared implementation for repulsive/attractive blasts
     * @param movementMultiplier positive -> away from source, negative -> towards source
     */
    public static void doMovementBlast(AbstractBlastEvent event, double movementMultiplier) {
        doBlastSoundAndParticles(event);
        doVanillaExplosion(event, 4.0F / 2F);

        double radius = 10;

        event.getBlastWorld().getEntities(null, new AxisAlignedBB(event.getBlastPosition()).inflate(radius)).forEach(
                (Entity entity) -> {

                    double deltaX = entity.getX() - event.getBlastPosition().getX();
                    double deltaY = entity.getY() - event.getBlastPosition().getY();
                    double deltaZ = entity.getZ() - event.getBlastPosition().getZ();
                    double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
                    double basePushDistance = movementMultiplier * 10D;

                    Vector3d deltaMovement = new Vector3d(basePushDistance * deltaX / distance, basePushDistance * deltaY / distance / 3, basePushDistance * deltaZ / distance);
                    entity.setDeltaMovement(entity.getDeltaMovement().add(deltaMovement));
                    if (entity instanceof ServerPlayerEntity) {
                        // Player movement (other than teleportation) is controlled exclusively client-side (which is dumb, but it's a vanilla mechanic that we can't control)
                        // so we trick the client into moving for us by telling it that there is a vanilla explosion that destroys no blocks and moves the player by our desired amount.
                        // This is handled client-side by net.minecraft.client.network.play.ClientPlayNetHandler::handleExplosion (MCP Class Names and Package Structure, Official Method/Field Mappings, Minecraft 1.16.5).
                        ((ServerPlayerEntity) entity).connection.send(new SExplosionPacket(event.getBlastPosition().getX(), event.getBlastPosition().getY(), event.getBlastPosition().getZ(), 0F, new ArrayList<BlockPos>(), deltaMovement));
                    }

                }
        );
    }

}
