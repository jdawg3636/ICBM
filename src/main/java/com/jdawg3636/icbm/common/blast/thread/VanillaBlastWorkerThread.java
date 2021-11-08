package com.jdawg3636.icbm.common.blast.thread;

import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class VanillaBlastWorkerThread extends Thread {

    public Supplier<Random> randomSupplier;
    public Function<BlockPos, BlockState> blockStateSupplier;

    public double explosionCenterPosX;
    public double explosionCenterPosY;
    public double explosionCenterPosZ;

    // 4.0F is the value used by vanilla TNT, is usually referred to as "explosionPower" in ICBM code rather that "radius"
    public float explosionPower;

    public Set<BlockPos> blocksToBeDestroyed = Sets.newHashSet();

    @Override
    public void run() {
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                for (int k = 0; k < 16; ++k) {
                    if (i == 0 || i == 15 || j == 0 || j == 15 || k == 0 || k == 15) {
                        double rayDirectionX = (double) ((float) i / 15.0F * 2.0F - 1.0F);
                        double rayDirectionY = (double) ((float) j / 15.0F * 2.0F - 1.0F);
                        double rayDirectionZ = (double) ((float) k / 15.0F * 2.0F - 1.0F);
                        double rayMagnitude = Math.sqrt(rayDirectionX * rayDirectionX + rayDirectionY * rayDirectionY + rayDirectionZ * rayDirectionZ);
                        rayDirectionX = rayDirectionX / rayMagnitude;
                        rayDirectionY = rayDirectionY / rayMagnitude;
                        rayDirectionZ = rayDirectionZ / rayMagnitude;
                        double currentPosXWithinRay = explosionCenterPosX;
                        double currentPosYWithinRay = explosionCenterPosY;
                        double currentPosZWithinRay = explosionCenterPosZ;

                        for (float rayPowerRemaining = explosionPower * (0.7F + randomSupplier.get().nextFloat() * 0.6F); rayPowerRemaining > 0.0F; rayPowerRemaining -= 0.22500001F) {

                            BlockPos blockpos = new BlockPos(currentPosXWithinRay, currentPosYWithinRay, currentPosZWithinRay);
                            BlockState blockstate = blockStateSupplier.apply(blockpos);

                            if(!blockstate.isAir()) rayPowerRemaining -= (blockstate.getBlock().getExplosionResistance() + 0.3F) * 0.3F;
                            if (rayPowerRemaining > 0.0F) blocksToBeDestroyed.add(blockpos);

                            currentPosXWithinRay += rayDirectionX * (double) 0.3F;
                            currentPosYWithinRay += rayDirectionY * (double) 0.3F;
                            currentPosZWithinRay += rayDirectionZ * (double) 0.3F;

                        }
                    }
                }
            }
        }
    }

}
