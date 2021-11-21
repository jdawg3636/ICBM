package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.common.reg.SoundEventReg;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class EventBlastIncendiary extends AbstractBlastEvent {

    public EventBlastIncendiary(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType, Direction blastDirection) {
        super(blastPosition, blastWorld, blastType, blastDirection, SoundEventReg.EXPLOSION_INCENDIARY);
    }

    @Override
    // todo: this is the only blast to still use original code from ICBM Classic. This is very messy and should probably be rewritten.
    public boolean executeBlast() {

        ICBMBlastEventUtil.doBlastSoundAndParticles(this);
        ICBMBlastEventUtil.doVanillaExplosionServerOnly(getBlastWorld(), getBlastPosition());

        // Copied (with slight modifications) from old icbm.content.blast.BlastFire
        // Would like to clean this up a bit if possible
        if (!getBlastWorld().isClientSide) {

            int radius = (!(getBlastType() == AbstractBlastEvent.Type.GRENADE)) ? 14 : 7;

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
                            float var14 = radius * (0.7F + getBlastWorld().random.nextFloat() * 0.6F);
                            double var15 = getBlastPosition().getX();
                            double var17 = getBlastPosition().getY();
                            double var19 = getBlastPosition().getZ();

                            for (float var21 = 0.3F; var14 > 0.0F; var14 -= var21 * 0.75F) {

                                BlockPos targetPosition = new BlockPos(var15, var17, var19);
                                double distanceFromCenter = Math.sqrt(getBlastPosition().distSqr(targetPosition));
                                BlockState blockState = getBlastWorld().getBlockState(targetPosition);
                                Block block = blockState.getBlock();

                                if (!block.isAir(blockState, getBlastWorld(), targetPosition))
                                    var14 -= (block.getExplosionResistance() + 0.3F) * var21;

                                if (var14 > 0.0F) {

                                    // Set fire by chance and distance
                                    double chance = radius - (Math.random() * distanceFromCenter);

                                    if (chance > distanceFromCenter * 0.55) {

                                        boolean canReplace = blockState.getMaterial().isReplaceable() || block.isAir(blockState, getBlastWorld(), targetPosition);

                                        if (canReplace)
                                            getBlastWorld().setBlockAndUpdate(targetPosition, Blocks.FIRE.defaultBlockState());
                                        else if (block == Blocks.ICE) {
                                            getBlastWorld().setBlockAndUpdate(targetPosition, Blocks.WATER.defaultBlockState());
                                            getBlastWorld().neighborChanged(targetPosition, Blocks.WATER, targetPosition);
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

        return true;

    }

}
