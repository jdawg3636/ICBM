package com.jdawg3636.icbm.common.blast.event;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Separate Event Handler for Mod's Own Events
 * Keeps stuff like blast behavior separate from core functionality such as registration
 */
public class ICBMBlastEventHandler {

    // Not Subscribed - Called Directly by Subclass Events to ensure this is triggered before the rest of the blast code
    public static void onBlast(BlastEvent event) {
        // Loosely Based on net.minecraft.world.Explosion.doExplosionB(boolean spawnParticles)
        if (!event.getBlastWorld().isClientSide) {
            // Sound
            event.getBlastWorld().playSound((PlayerEntity) null, event.getBlastPosition().getX(), event.getBlastPosition().getY(), event.getBlastPosition().getZ(), SoundEvents.GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (event.getBlastWorld().random.nextFloat() - event.getBlastWorld().random.nextFloat()) * 0.2F) * 0.7F);
            // Particles - Handled Client Side by net.minecraft.client.network.play.ClientPlayNetHandler.handleParticles(SSpawnParticlePacket packetIn)
            event.getBlastWorld().sendParticles(ParticleTypes.EXPLOSION_EMITTER, event.getBlastPosition().getX(), event.getBlastPosition().getY(), event.getBlastPosition().getZ(), 1, 0, 0, 0, 1.0D);
        }
    }

    @SubscribeEvent
    public static void onBlastAntimatter(BlastEvent.Antimatter event) {

        onBlast(event);

        if (!event.getBlastWorld().isClientSide) {
            int radius = 50;
            int radiusSq = radius * radius;
            for(int i = -radius; i <= radius; i++)
                for(int j = -radius; j <= radius; j++)
                    for(int k = -radius; k <= radius; k++) {
                        BlockPos candidatePos = new BlockPos(event.getBlastPosition().getX() + i, event.getBlastPosition().getY() + j, event.getBlastPosition().getZ() + k);
                        if (event.getBlastPosition().distSqr(candidatePos) < radiusSq) {
                            event.getBlastWorld().setBlock(candidatePos, Blocks.AIR.defaultBlockState(), 3);
                        }
                    }
        }

    }

    @SubscribeEvent
    public static void onBlastIncendiary(BlastEvent.Incendiary event) {

        onBlast(event);

        // Copied (with slight modifications) from old icbm.content.blast.BlastFire
        // Would like to clean this up a bit if possible
        if (!event.getBlastWorld().isClientSide) {

            int radius = (!(event.getBlastType() == BlastEvent.Type.GRENADE)) ? 14 : 7;

            for (int x = 0; x < radius; ++x) {
                for (int y = 0; y < radius; ++y) {
                    for (int z = 0; z < radius; ++z) {

                        if (x == 0 || x == radius - 1 || y == 0 || y == radius - 1 || z == 0 || z == radius - 1) {

                            double xStep = x / (radius - 1.0F) * 2.0F - 1.0F;
                            double yStep = y / (radius - 1.0F) * 2.0F - 1.0F;
                            double zStep = z / (radius - 1.0F) * 2.0F - 1.0F;
                            double diagonalDistance = Math.sqrt(xStep * xStep + yStep * yStep + zStep * zStep);
                            xStep /= diagonalDistance;
                            yStep /= diagonalDistance;
                            zStep /= diagonalDistance;
                            float var14 = radius * (0.7F + event.getBlastWorld().random.nextFloat() * 0.6F);
                            double var15 = event.getBlastPosition().getX();
                            double var17 = event.getBlastPosition().getY();
                            double var19 = event.getBlastPosition().getZ();

                            for (float var21 = 0.3F; var14 > 0.0F; var14 -= var21 * 0.75F) {

                                BlockPos targetPosition = new BlockPos(var15, var17, var19);
                                double distanceFromCenter = Math.sqrt(event.getBlastPosition().distSqr(targetPosition));
                                BlockState blockState = event.getBlastWorld().getBlockState(targetPosition);
                                Block block = blockState.getBlock();

                                if (!block.isAir(blockState, event.getBlastWorld(), targetPosition))
                                    var14 -= (block.getExplosionResistance() + 0.3F) * var21;

                                if (var14 > 0.0F) {

                                    // Set fire by chance and distance
                                    double chance = radius - (Math.random() * distanceFromCenter);

                                    if (chance > distanceFromCenter * 0.55) {

                                        boolean canReplace = blockState.getMaterial().isReplaceable() || block.isAir(blockState, event.getBlastWorld(), targetPosition);

                                        if (canReplace)
                                            event.getBlastWorld().setBlockAndUpdate(targetPosition, Blocks.FIRE.defaultBlockState());
                                        else if (block == Blocks.ICE) {
                                            event.getBlastWorld().setBlockAndUpdate(targetPosition, Blocks.WATER.defaultBlockState());
                                            event.getBlastWorld().neighborChanged(targetPosition, Blocks.WATER, targetPosition);
                                        }

                                    }

                                }

                                var15 += xStep * var21;
                                var17 += yStep * var21;
                                var19 += zStep * var21;

                            }
                        }
                    }
                }
            }
        }

    }

}
