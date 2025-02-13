package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.common.reg.SoundEventReg;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.network.play.server.SExplosionPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
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
            event.getBlastWorld().playSound((PlayerEntity) null, event.getBlastPosition().getX() + 0.5, event.getBlastPosition().getY() + 0.5, event.getBlastPosition().getZ() + 0.5, soundEvent.get(), SoundCategory.BLOCKS, event.getSoundEventRangeMultiplier(), 1.0F);
            // Particles - Handled Client Side by net.minecraft.client.network.play.ClientPlayNetHandler::handleParticleEvent (MCP Class Names and Package Structure, Official Method/Field Mappings, Minecraft 1.16.5)
            event.getBlastWorld().sendParticles(ParticleTypes.EXPLOSION_EMITTER, event.getBlastPosition().getX() + 0.5, event.getBlastPosition().getY() + 0.5, event.getBlastPosition().getZ() + 0.5, 1, 0, 0, 0, 1.0D);
        }
    }

    public static void doVanillaExplosion(AbstractBlastEvent event) {
        doVanillaExplosion(event, 4.0F);
    }

    public static void doVanillaExplosion(AbstractBlastEvent event, float explosionPower) {
        doVanillaExplosion(event.getBlastWorld(), event.getBlastPosition().getX() + 0.5, event.getBlastPosition().getY() + 0.5, event.getBlastPosition().getZ() + 0.5, explosionPower);
    }

    public static void doVanillaExplosion(ServerWorld level, BlockPos blockPos, float explosionPower) {
        level.explode(null, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, explosionPower, Explosion.Mode.BREAK);
    }

    public static void doVanillaExplosion(ServerWorld level, double posX, double posY, double posZ, float explosionPower) {
        level.explode(null, posX, posY, posZ, explosionPower, Explosion.Mode.BREAK);
    }

    public static void doVanillaExplosionServerOnly(ServerWorld level, BlockPos blockPos) {
        doVanillaExplosionServerOnly(level, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 4.0F);
    }

    public static void doVanillaExplosionServerOnly(ServerWorld level, BlockPos blockPos, float explosionPower) {
        doVanillaExplosionServerOnly(level, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, explosionPower);
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

                    double deltaX = entity.getX() - event.getBlastPosition().getX() + 0.5;
                    double deltaY = entity.getY() - event.getBlastPosition().getY() + 0.5;
                    double deltaZ = entity.getZ() - event.getBlastPosition().getZ() + 0.5;
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

    // Adapted from a portion of net.minecraft.world.Explosion::explode (MC 1.16.5, MCP + Parchment Mappings)
    public static void doExplosionDamageAndKnockback(World level, Vector3d blastPosition, DamageSource damageSource, double radius) {

        // Process Parameters
        damageSource = damageSource != null ? damageSource : DamageSource.explosion((LivingEntity) null);

        // Calculate AABB and find entities
        double diameter = radius * 2.0F;
        double xMin = MathHelper.floor(blastPosition.x() - diameter - 1.0D);
        double xMax = MathHelper.floor(blastPosition.x() + diameter + 1.0D);
        double yMin = MathHelper.floor(blastPosition.y() - diameter - 1.0D);
        double yMax = MathHelper.floor(blastPosition.y() + diameter + 1.0D);
        double zMin = MathHelper.floor(blastPosition.z() - diameter - 1.0D);
        double zMax = MathHelper.floor(blastPosition.z() + diameter + 1.0D);
        List<Entity> entities = level.getEntities(null, new AxisAlignedBB(xMin, yMin, zMin, xMax, yMax, zMax));

        // TODO fire this event? Leaving it out for now to avoid sending null for Explosion (likely causing NPE in other mods)
        // net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(level, null, list, diameter);

        for(int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);
            if (!entity.ignoreExplosion()) {
                double entityDistanceToEpicenterAsRatio = (double)(MathHelper.sqrt(entity.distanceToSqr(blastPosition)) / diameter);
                if (entityDistanceToEpicenterAsRatio <= 1.0D) {
                    double entityDistanceX = entity.getX() - blastPosition.x();
                    // Note: primed explosives from this mod are also instances of TNTEntity
                    double entityDistanceY = (entity instanceof TNTEntity ? entity.getY() : entity.getEyeY()) - blastPosition.y();
                    double entityDistanceZ = entity.getZ() - blastPosition.z();
                    double entityDistance = MathHelper.sqrt(entityDistanceX * entityDistanceX + entityDistanceY * entityDistanceY + entityDistanceZ * entityDistanceZ);
                    if (entityDistance != 0.0D) {
                        // Normalize distances (components sum to 1)
                        entityDistanceX = entityDistanceX / entityDistance;
                        entityDistanceY = entityDistanceY / entityDistance;
                        entityDistanceZ = entityDistanceZ / entityDistance;
                        double seenPercent = (double)Explosion.getSeenPercent(blastPosition, entity);
                        double knockback = (1.0D - entityDistanceToEpicenterAsRatio) * seenPercent * radius;
                        entity.hurt(damageSource, (float)((int)((knockback * knockback + knockback) / 2.0D * 7.0D * diameter + 1.0D)));
                        double knockbackAfterDampener = knockback;
                        if (entity instanceof LivingEntity) {
                            knockbackAfterDampener = ProtectionEnchantment.getExplosionKnockbackAfterDampener((LivingEntity)entity, knockback);
                        }

                        entity.setDeltaMovement(entity.getDeltaMovement().add(entityDistanceX * knockbackAfterDampener, entityDistanceY * knockbackAfterDampener, entityDistanceZ * knockbackAfterDampener));
                        if (entity instanceof ServerPlayerEntity) {
                            ServerPlayerEntity playerentity = (ServerPlayerEntity)entity;
                            if (!playerentity.isSpectator() && (!playerentity.isCreative() || !playerentity.abilities.flying)) {
                                // Vanilla code would cache this in a list for ServerWorld to send later - we're just doing it directly
                                // this.hitPlayers.put(playerentity, new Vector3d(entityDistanceX * d10, d7 * d10, d9 * d10));
                                playerentity.connection.send(new SExplosionPacket(blastPosition.x(), blastPosition.y(), blastPosition.z(), (float)radius, new ArrayList<>(), new Vector3d(entityDistanceX * knockback, entityDistanceY * knockback, entityDistanceZ * knockback)));
                            }
                        }
                    }
                }
            }
        }
    }

    // Loosely adapted from a portion of net.minecraft.world.Explosion::finalizeExplosion (MC 1.16.5, MCP Class Mappings + Mojang Method Mappings)
    public static void dropLootForBlockPos(ServerWorld level, BlockPos blockPos, @Nullable Entity source, boolean destroyBlock) {
        BlockState blockstate = level.getBlockState(blockPos);
        TileEntity tileentity = blockstate.hasTileEntity() ? level.getBlockEntity(blockPos) : null;
        LootContext.Builder lootContextBuilder = new LootContext.Builder(level)
                .withRandom(level.random)
                .withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf(blockPos))
                .withParameter(LootParameters.TOOL, ItemStack.EMPTY)
                .withOptionalParameter(LootParameters.BLOCK_ENTITY, tileentity)
                .withOptionalParameter(LootParameters.THIS_ENTITY, source);
        blockstate.getDrops(lootContextBuilder).forEach((itemStack) -> Block.popResource(level, blockPos, itemStack));
        if(destroyBlock) level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
    }

}
