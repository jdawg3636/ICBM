package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.common.entity.EntityBlastNightmare;
import com.jdawg3636.icbm.common.reg.EntityReg;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class EventBlastNightmare extends AbstractBlastEvent {

    public EventBlastNightmare(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType, Direction blastDirection) {
        super(blastPosition, blastWorld, blastType, blastDirection);
    }

    @Override
    public boolean executeBlast() {

        ICBMBlastEventUtil.doBlastSoundAndParticles(this);

        ServerWorld level = getBlastWorld();
        double explosionCenterPosX = getBlastPosition().getX() + 0.5;
        double explosionCenterPosY = getBlastPosition().getY() + 0.5;
        double explosionCenterPosZ = getBlastPosition().getZ() + 0.5;

        // Spawn Entity
        EntityBlastNightmare entity = EntityReg.BLAST_NIGHTMARE.get().create(level);
        if(entity != null) {
            entity.setPos(explosionCenterPosX, explosionCenterPosY, explosionCenterPosZ);
            level.addFreshEntity(entity);
        }

        // Set time and play sound
        for(ServerWorld serverLevel : level.getServer().getAllLevels()) {
            // Play Sound
            for(ServerPlayerEntity player : serverLevel.players()) {
                player.playNotifySound(SoundEvents.BEACON_POWER_SELECT, SoundCategory.BLOCKS, 1F, 1F);
            }
            // Set Time (must be done per-level; matches behavior of vanilla "/time" command).
            serverLevel.setDayTime(18_000L);
        }

        return true;

    }

}