package com.jdawg3636.icbm.common.event;

import com.google.common.collect.Lists;
import com.jdawg3636.icbm.ICBMReference;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;

public class EventBlastNightmare extends AbstractBlastEvent {

    public EventBlastNightmare(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType, Direction blastDirection) {
        super(blastPosition, blastWorld, blastType, blastDirection);
    }

    @Override
    public boolean executeBlast() {
        ICBMBlastEventUtil.doBlastSoundAndParticles(this);
        for(BlockPos pos : getAffectedBlockPositions(getBlastWorld(), getBlastPosition().getX(), getBlastPosition().getY(), getBlastPosition().getZ(), /*10f*/50f, 3600002)) {
            //getBlastWorld().setBlockAndUpdate(pos, Blocks.RED_STAINED_GLASS.defaultBlockState());
            //getBlastWorld().setBlockAndUpdate(pos, Blocks.GREEN_STAINED_GLASS.defaultBlockState());
            //getBlastWorld().setBlockAndUpdate(pos, Blocks.MAGENTA_STAINED_GLASS.defaultBlockState());
            //getBlastWorld().setBlockAndUpdate(pos, Blocks.ORANGE_STAINED_GLASS.defaultBlockState());
            getBlastWorld().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }
        return false;
    }

    //public static final float Y_SHORTEN = 1.5f;
    public static final float Y_SHORTEN = 1.0f;

    /*
    public static List<BlockPos> getAffectedBlockPositions(World world, double x, double y, double z, float radius, double max_blast_power){
        List<BlockPos> affectedBlockPositions = Lists.newArrayList();
        int radius_int = (int) Math.ceil(radius);
        for (int dx = -radius_int; dx < radius_int + 1; dx++) {
            // fast calculate affected blocks
            int y_lim = (int) (Math.sqrt(radius_int*radius_int-dx*dx)/Y_SHORTEN);
            for (int dy = -y_lim; dy < y_lim + 1; dy++) {
                int z_lim = (int) Math.sqrt(radius_int*radius_int-dx*dx-dy*dy*Y_SHORTEN*Y_SHORTEN);
                for (int dz = -z_lim; dz < z_lim + 1; dz++) {
                    BlockPos blockPos = new BlockPos(x + dx, y + dy, z + dz);
                    BlockState blockState = world.getBlockState(blockPos);
                    double power = getBlastPower(Math.sqrt(dx*dx+dy*dy*Y_SHORTEN*Y_SHORTEN+dz*dz), radius);
                    if (blockState!= Blocks.AIR.defaultBlockState() && ((power>1) ||(power > new Random().nextDouble()))){
                        float resistance = blockState.getBlock().getExplosionResistance();
                        if (resistance < max_blast_power) {
                            affectedBlockPositions.add(blockPos);
                        }
                    }
                }
            }
        }
        return affectedBlockPositions;
    }
    */

    public static List<BlockPos> getAffectedBlockPositions(World world, double x, double y, double z, float radius, double max_blast_power){
        long startTime = System.currentTimeMillis();
        List<BlockPos> affectedBlockPositions = Lists.newArrayList();
        int radius_int = (int) Math.ceil(radius);
        for (int dx = -radius_int; dx < radius_int + 1; dx++) {
            // fast calculate affected blocks
            int y_lim = (int) (Math.sqrt(radius_int*radius_int-dx*dx));
            for (int dy = -y_lim; dy < y_lim + 1; dy++) {
                int z_lim = (int) Math.sqrt(radius_int*radius_int-dx*dx-dy*dy);
                for (int dz = -z_lim; dz < z_lim + 1; dz++) {
                    BlockPos blockPos = new BlockPos(x + dx, y + dy, z + dz);
                    if((dx != -radius_int && dx != radius_int && dy != -y_lim && dy != y_lim && dz != -z_lim && dz != z_lim) || world.random.nextFloat() < ICBMReference.COMMON_CONFIG.getAntimatterFuzzinessPercentage()) {
                        affectedBlockPositions.add(blockPos);
                    }
                }
            }
        }
        long endTime = System.currentTimeMillis();
        ICBMReference.logger().info("Calculated Affected Block Positions in {} milliseconds", endTime - startTime);
        return affectedBlockPositions;
    }

    public static double getBlastPower(double dist, double radius){
        double decay_rd = radius * 0.95;
        if(dist < decay_rd){
            return 1.1d;
        }
        else {
            return -(1/(radius-decay_rd))*(dist-decay_rd) + 1;
        }
    }

}