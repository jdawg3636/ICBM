package com.jdawg3636.icbm.common.event;

import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class EventBlastBreaching extends AbstractBlastEvent {

    public EventBlastBreaching(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType, Direction blastDirection) {
        super(blastPosition, blastWorld, blastType, blastDirection);
    }

    @Override
    public boolean executeBlast() {
        ICBMBlastEventUtil.doBlastSoundAndParticles(this);
        int depth = 7; // todo: make configurable
        BlockPos currentPos = getBlastPosition();
        while(depth >= 0) {
            if(getBlastWorld().getBlockState(currentPos).getBlock().getExplosionResistance() > Blocks.OBSIDIAN.getExplosionResistance()) {
                break;
            }
            ICBMBlastEventUtil.doVanillaExplosionServerOnly(getBlastWorld(), currentPos, 2.5F);
            currentPos = currentPos.offset(getBlastDirection().getNormal());
            --depth;
        }
        return true;
    }

}
