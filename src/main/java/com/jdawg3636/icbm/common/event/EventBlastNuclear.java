package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.common.capability.ICBMCapabilities;
import com.jdawg3636.icbm.common.capability.blastcontroller.IBlastControllerCapability;
import com.jdawg3636.icbm.common.thread.NuclearBlastManagerThread;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;

public class EventBlastNuclear extends AbstractBlastEvent {

    public EventBlastNuclear(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType) {
        super(blastPosition, blastWorld, blastType);
    }

    @Override
    public boolean executeBlast() {

        /*
         * NOTE: All of this is super WIP. Still needs a LOT of work.
         */

        /*
        doBlastSoundAndParticles(event);

        if (!event.getBlastWorld().isClientSide) {
            int radius = 64;
            for (int explosionPosY = -radius / 8; explosionPosY <= 0; explosionPosY += 8) {
                for(int explosionPosX = -radius; explosionPosX <= radius; explosionPosX += 8) {
                    for (int explosionPosZ = -radius; explosionPosZ <= radius; explosionPosZ += 8) {
                        if(explosionPosX * explosionPosX + explosionPosZ * explosionPosZ + explosionPosY * explosionPosY < radius * radius) {
                            doVanillaExplosionServerOnly(event.getBlastWorld(), event.getBlastPosition().offset(explosionPosX, explosionPosY, explosionPosZ), 4F * 4.0F);
                        }
                    }
                }
            }
            for(int i = -radius - 10; i <= radius + 10; ++i) {
                for (int j = (-radius / 8) - 10; j <= 0; ++j) {
                    for (int k = -radius - 10; k <= radius + 10; ++k) {
                        if(event.getBlastWorld().random.nextFloat() < 0.15 && i * i + j * j + k * k < radius * radius && event.getBlastWorld().getBlockState(event.getBlastPosition().offset(i, j, k)).getBlock().getTags().contains(new ResourceLocation(ICBMReference.MODID, "can_become_radioactive_material"))) {
                            event.getBlastWorld().setBlock(event.getBlastPosition().offset(i, j, k), BlockReg.RADIOACTIVE_MATERIAL.get().defaultBlockState(), 3);
                        }
                    }
                }
            }
        }
         */
        NuclearBlastManagerThread blastManagerThread = new NuclearBlastManagerThread();
        blastManagerThread.explosionCenterPosX = getBlastPosition().getX();
        blastManagerThread.explosionCenterPosY = getBlastPosition().getY();
        blastManagerThread.explosionCenterPosZ = getBlastPosition().getZ();
        blastManagerThread.radius = 30.0F; //todo: make configurable, default to 30 if performance is acceptable
        blastManagerThread.threadCount = 4;
        LazyOptional<IBlastControllerCapability> cap = getBlastWorld().getCapability(ICBMCapabilities.BLAST_CONTROLLER_CAPABILITY);
        if(cap.isPresent()) {
            cap.orElse(null).enqueueBlastThread(blastManagerThread);
        }
        return true;
    }

}
