package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.ICBMReference;
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

        int depth = ICBMReference.COMMON_CONFIG.getBlastDepthBreaching(); // Basically the radius, just directional. Damage and knockback is NOT directional.
        ICBMBlastEventUtil.doExplosionDamageAndKnockback(this, depth);

        // Early return if the explosion epicenter is inside an explosion-resistant fluid (ex. lava, water)
        if(getBlastWorld().getBlockState(getBlastPosition()).getFluidState().getExplosionResistance() > 0) {
            return true;
        }

        BlockPos currentPos = getBlastPosition();
        while(depth >= 0) {
            // Using deprecated variant of Block::getExplosionResistance since the "proper" version requires an instance of Explosion. We could construct a placeholder, but that feels wasteful.
            //noinspection deprecation
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
