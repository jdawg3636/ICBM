package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.ICBMReference;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class EventBlastAnvil extends AbstractBlastEvent {

    public EventBlastAnvil(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType, Direction blastDirection) {
        super(blastPosition, blastWorld, blastType, blastDirection);
    }

    @Override
    public boolean executeBlast() {
        ICBMBlastEventUtil.doBlastSoundAndParticles(this);
        ICBMBlastEventUtil.doVanillaExplosion(this);
        // Early return if the explosion epicenter is inside an explosion-resistant fluid (ex. lava, water)
        if(getBlastWorld().getBlockState(getBlastPosition()).getFluidState().getExplosionResistance() > 0) {
            return true;
        }
        // Reverse-solve initial horizontal velocity given desired horizontal distance. This is a good but imperfect approximation.
        // https://www.desmos.com/calculator/yvmg500ceq
        double maxHorizontalVelocity = ICBMReference.COMMON_CONFIG.getBlastRadiusAnvil() * 0.0345096860612;
        // Adjust radius for grenade (if applicable)
        maxHorizontalVelocity *= getBlastType() == AbstractBlastEvent.Type.GRENADE ? 0.625 : 1;
        // Round up to the nearest multiple of (1/16)
        maxHorizontalVelocity = Math.ceil(maxHorizontalVelocity / 0.0625) * 0.0625;
        for(double i = -maxHorizontalVelocity; i <= maxHorizontalVelocity; i += 0.0625) {
            for(double j = -maxHorizontalVelocity; j <= maxHorizontalVelocity; j += 0.0625) {
                // Truncate to Circle from Square
                if(i * i + j * j > maxHorizontalVelocity * maxHorizontalVelocity) {
                    continue;
                }
                FallingBlockEntity fallingblockentity = new FallingBlockEntity(getBlastWorld(), (double)getBlastPosition().getX() + 0.5D, (double)getBlastPosition().getY() + 1, (double)getBlastPosition().getZ() + 0.5D, Blocks.DAMAGED_ANVIL.defaultBlockState());
                fallingblockentity.time = 1;
                fallingblockentity.cancelDrop = true;
                fallingblockentity.fallDamageAmount = 6.0F;
                fallingblockentity.setDeltaMovement(i, 1, j);
                fallingblockentity.setHurtsEntities(true);
                getBlastWorld().addFreshEntity(fallingblockentity);
            }
        }
        return true;
    }

}
