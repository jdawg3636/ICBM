package com.jdawg3636.icbm.common.event;

import net.minecraft.block.Blocks;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class EventBlastAnvil extends AbstractBlastEvent {

    public EventBlastAnvil(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType) {
        super(blastPosition, blastWorld, blastType);
    }

    @Override
    public boolean executeBlast() {
        ICBMBlastEventUtil.doBlastSoundAndParticles(this);
        ICBMBlastEventUtil.doVanillaExplosion(this);
        for(double i = -0.5; i <= 0.5; i += 0.0625) {
            for(double j = -0.5; j <= 0.5; j += 0.0625) {
                if(i * i + j * j > 0.5 * 0.5) continue; // Truncate to Circle from Square
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
