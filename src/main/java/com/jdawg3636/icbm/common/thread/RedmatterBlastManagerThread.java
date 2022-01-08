package com.jdawg3636.icbm.common.thread;

import com.jdawg3636.icbm.common.entity.EntityRedmatterBlast;
import com.jdawg3636.icbm.common.reg.EntityReg;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;

public class RedmatterBlastManagerThread extends AntimatterBlastManagerThread {

    @Override
    public String getRegistryName() {
        return "icbm:redmatter";
    }

    @Override
    public Runnable getPostCompletionFunction(ServerWorld level) {
        return () -> {
            // Create Entity
            EntityRedmatterBlast entity = EntityReg.BLAST_REDMATTER.get().create(level);
            entity.setPos(explosionCenterPosX, explosionCenterPosY, explosionCenterPosZ);
            // Pull Results from Worker Threads
            entity.blocksToDestroy = new ArrayList<>();
            for(AntimatterBlastWorkerThread worker : threadPool) {
                entity.blocksToDestroy.addAll(worker.blocksToBeDestroyed);
            }
            // Sort Results
            entity.blocksToDestroy.sort((a,b)-> Double.compare(getDistanceToCenter(a), getDistanceToCenter(b)));
            // Spawn Entity
            level.addFreshEntity(entity);
        };
    }

    private double getDistanceToCenter(BlockPos pos) {
        return Math.sqrt((explosionCenterPosX - pos.getX()) * (explosionCenterPosX - pos.getX()) + (explosionCenterPosY - pos.getY()) * (explosionCenterPosY - pos.getY()) + (explosionCenterPosZ - pos.getZ()) * (explosionCenterPosZ - pos.getZ()));
    }

}
