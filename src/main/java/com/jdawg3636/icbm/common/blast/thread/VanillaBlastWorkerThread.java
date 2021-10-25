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
    public float radius;

    public Set<BlockPos> blocksToBeDestroyed = Sets.newHashSet();

    @Override
    public void run() {

        int i = 16;

        for (int j = 0; j < 16; ++j) {
            for (int k = 0; k < 16; ++k) {
                for (int l = 0; l < 16; ++l) {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                        double d0 = (double) ((float) j / 15.0F * 2.0F - 1.0F);
                        double d1 = (double) ((float) k / 15.0F * 2.0F - 1.0F);
                        double d2 = (double) ((float) l / 15.0F * 2.0F - 1.0F);
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 = d0 / d3;
                        d1 = d1 / d3;
                        d2 = d2 / d3;
                        float f = radius * (0.7F + randomSupplier.get().nextFloat() * 0.6F);
                        double d4 = explosionCenterPosX;
                        double d6 = explosionCenterPosY;
                        double d8 = explosionCenterPosZ;

                        for (float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {

                            BlockPos blockpos = new BlockPos(d4, d6, d8);
                            BlockState blockstate = blockStateSupplier.apply(blockpos);

                            if(!blockstate.isAir()) f -= (blockstate.getBlock().getExplosionResistance() + 0.3F) * 0.3F;
                            if (f > 0.0F) blocksToBeDestroyed.add(blockpos);

                            d4 += d0 * (double) 0.3F;
                            d6 += d1 * (double) 0.3F;
                            d8 += d2 * (double) 0.3F;

                        }
                    }
                }
            }
        }
    }

}
