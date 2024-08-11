package com.jdawg3636.icbm.common.capability.missiledirector;

import com.jdawg3636.icbm.common.entity.EntityMissile;
import net.minecraftforge.event.TickEvent;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface IMissileDirectorCapability {

    UUID registerMissile(LogicalMissile logicalMissile, Optional<UUID> forceLogicalUUID);

    Set<UUID> getLogicalMissileIDList();

    Collection<LogicalMissile> getLogicalMissiles();

    LogicalMissile lookupLogicalMissile(UUID missileID);

    void deleteMissile(UUID missileID);

    void deleteMissile(LogicalMissile logicalMissile);

    void deleteMissile(EntityMissile puppetEntity);

    void deleteAllMissiles();

    void onWorldTickEvent(TickEvent.WorldTickEvent event);

}
