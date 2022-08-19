package com.jdawg3636.icbm.common.thread;

import com.jdawg3636.icbm.common.entity.EntityLingeringBlastRadiation;
import com.jdawg3636.icbm.common.reg.BlockReg;
import com.jdawg3636.icbm.common.reg.EntityReg;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class NuclearBlastManagerThread extends RaytracedBlastManagerThread {

    @Override
    public String getRegistryName() {
        return "icbm:nuclear";
    }

    @Override
    public RaytracedBlastWorkerThread getNewWorkerThread() {
        return new NuclearBlastWorkerThread();
    }

    @Override
    public void decorate(World level, BlockPos blockPos) {
        level.setBlockAndUpdate(blockPos, BlockReg.RADIOACTIVE_MATERIAL.get().defaultBlockState());
    }

    @Override
    public Runnable getPostCompletionFunction(final ServerWorld level) {
        return () -> {
            super.getPostCompletionFunction(level).run();
            EntityLingeringBlastRadiation entity = EntityReg.BLAST_RADIATION.get().create(level);
            if(entity != null) {
                entity.setPos(explosionCenterPosX, explosionCenterPosY, explosionCenterPosZ);
                entity.ticksRemaining = 300 * 20;
                entity.radius = radius;
                level.addFreshEntity(entity);
            }
        };
    }

}
