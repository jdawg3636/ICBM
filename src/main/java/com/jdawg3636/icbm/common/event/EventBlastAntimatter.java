package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.reg.SoundEventReg;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

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

        for(BlockPos pos : ICBMBlastEventUtil.getBlockPositionsWithinFuzzySphere(getBlastPosition().getX() + 0.5, getBlastPosition().getY() + 0.5, getBlastPosition().getZ() + 0.5, radius, level.random, fuzzinessPercentage)) {
            BlockState blockState = level.getBlockState(pos);
            if(!blockState.isAir(level, pos) && (canDestroyIndestructible || blockState.getBlock().explosionResistance < 3_600_000F)) {
                level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            }
        }
        return true;

    }

}
