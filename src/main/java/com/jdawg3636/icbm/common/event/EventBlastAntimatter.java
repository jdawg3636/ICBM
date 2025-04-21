package com.jdawg3636.icbm.common.event;

import com.google.common.collect.Lists;
import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.reg.SoundEventReg;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Random;

public class EventBlastAntimatter extends AbstractBlastEvent {

    public EventBlastAntimatter(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType, Direction blastDirection) {
        super(blastPosition, blastWorld, blastType, blastDirection, SoundEventReg.EXPLOSION_ANTIMATTER, 8.0F);
    }

    @Override
    public boolean executeBlast() {

        ICBMBlastEventUtil.doBlastSoundAndParticles(this);

        final ServerWorld level = getBlastWorld();
        final double radius = ICBMReference.COMMON_CONFIG.getBlastRadiusAntimatter();
        final double fuzzinessPercentage = ICBMReference.COMMON_CONFIG.getAntimatterFuzzinessPercentage();
        final boolean canDestroyIndestructible = ICBMReference.COMMON_CONFIG.getAntimatterCanDestroyBedrock();

        for(BlockPos pos : getAffectedBlockPositions(getBlastPosition().getX() + 0.5, getBlastPosition().getY() + 0.5, getBlastPosition().getZ() + 0.5, radius, level.random, fuzzinessPercentage)) {
            BlockState blockState = level.getBlockState(pos);
            if(!blockState.isAir(level, pos) && (canDestroyIndestructible || blockState.getBlock().explosionResistance < 3_600_000F)) {
                level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            }
        }
        return true;

    }

    /**
     * <p> Calculates all the block positions that should be affected by a blast at the specified epicenter and radius.
     * Includes some randomness logic for making the crater edges "fuzzy". Does NOT have access to the level or take
     * into account the blast resistance of affected blocks. </p>
     * <p> Algorithm adapted from "Guns, Rockets and Atomic Explosions" mod by songxia23 (MIT License) </p>
     * <p> <a href="https://github.com/MikhailTapio/nuclear_craft/blob/6b59198da4c8459fc64d467ca06e076326dd9615/src/main/java/com/song/nuclear_craft/entities/ExplosionUtils.java#L44">https://github.com/MikhailTapio/nuclear_craft/blob/6b59198da4c8459fc64d467ca06e076326dd9615/src/main/java/com/song/nuclear_craft/entities/ExplosionUtils.java#L44</a> </p>
     */
    public static List<BlockPos> getAffectedBlockPositions(double x, double y, double z, double radius, Random random, double fuzzinessPercentage) {
        // Create list to return
        List<BlockPos> affectedBlockPositions = Lists.newArrayList();
        // Round radius up to next integer for the algorithm to work
        int radius_int = (int) Math.ceil(radius);
        // Iterate over all x values in radius
        for (int dx = -radius_int; dx < radius_int + 1; dx++) {
            // Use pythagorean theorem to iterate over only the potentially-valid y values for this x-constrained plane.
            int y_lim = (int) (Math.sqrt(radius_int*radius_int-dx*dx));
            for (int dy = -y_lim; dy < y_lim + 1; dy++) {
                // Use pythagorean theorem to iterate over only the valid z values within the xy-constrained line.
                int z_lim = (int) Math.sqrt(radius_int*radius_int-dx*dx-dy*dy);
                for (int dz = -z_lim; dz < z_lim + 1; dz++) {
                    // Random chance to leave off some of the blocks on the edge of the scan space on any axis.
                    // This isn't perfect since we don't want computational expense of calculating distance to epicenter, but seems good enough in testing.
                    if((dx != -radius_int && dx != radius_int && dy != -y_lim && dy != y_lim && dz != -z_lim && dz != z_lim) || random.nextFloat() < fuzzinessPercentage) {
                        BlockPos blockPos = new BlockPos(x + dx, y + dy, z + dz);
                        affectedBlockPositions.add(blockPos);
                    }
                }
            }
        }
        // Return
        return affectedBlockPositions;
    }

}
