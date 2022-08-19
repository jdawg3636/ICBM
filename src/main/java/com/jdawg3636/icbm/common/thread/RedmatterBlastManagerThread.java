package com.jdawg3636.icbm.common.thread;

import com.jdawg3636.icbm.common.entity.EntityRedmatterBlast;
import com.jdawg3636.icbm.common.reg.EntityReg;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class RedmatterBlastManagerThread extends AntimatterBlastManagerThread {

    private HashMap<BlockPos, Integer> hashCache;

    @Override
    public String getRegistryName() {
        return "icbm:redmatter";
    }

    @Override
    public Runnable getPostCompletionFunction(ServerWorld level) {
        return () -> {
            // Create Entity
            EntityRedmatterBlast entity = EntityReg.BLAST_REDMATTER.get().create(level);
            if(entity != null) {
                entity.setPos(explosionCenterPosX, explosionCenterPosY, explosionCenterPosZ);
                // Pull Results from Worker Threads
                entity.blocksToDestroy = new ArrayList<>();
                for (AntimatterBlastWorkerThread worker : threadPool) {
                    entity.blocksToDestroy.addAll(worker.blocksToBeDestroyed);
                }
                // Sort Results
                hashCache = new HashMap<>();
                entity.blocksToDestroy.sort(this::compareBlockPosDistanceWithFuzziness);
                hashCache = null;
                // Spawn Entity
                level.addFreshEntity(entity);
            }
        };
    }

    // Implements Functional Interface java.util.Comparator<BlockPos>
    private int compareBlockPosDistanceWithFuzziness(BlockPos a, BlockPos b) {
        double distA = getDistanceToCenter(a);
        double distB = getDistanceToCenter(b);
        if(fuzzyEdgeThickness != 0) {
            try {
                distA += hashBlockPos(a) % fuzzyEdgeThickness;
                distB += hashBlockPos(b) % fuzzyEdgeThickness;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return Double.compare(distA, distB);
    }

    private double getDistanceToCenter(BlockPos pos) {
        return Math.sqrt((explosionCenterPosX - pos.getX()) * (explosionCenterPosX - pos.getX()) + (explosionCenterPosY - pos.getY()) * (explosionCenterPosY - pos.getY()) + (explosionCenterPosZ - pos.getZ()) * (explosionCenterPosZ - pos.getZ()));
    }

    private int hashBlockPos(BlockPos pos) {
        if(hashCache.containsKey(pos)) {
            return hashCache.get(pos);
        }
        int toReturn = Integer.parseInt(DigestUtils.sha1Hex("" + pos.getX() + pos.getY() + pos.getZ()).substring(0,4).toUpperCase(), 16);
        hashCache.put(pos, toReturn);
        return toReturn;
    }

}
