package com.jdawg3636.icbm.common.capability.missiledirector;

import com.jdawg3636.icbm.common.entity.EntityMissile;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface IMissileDirectorCapability {

    UUID registerMissile(LogicalMissile logicalMissile, Optional<UUID> forceLogicalUUID);

    Set<UUID> getLogicalMissileIDList();

    HashMap<UUID, LogicalMissile> getLogicalMissiles();

    Optional<LogicalMissile> lookupLogicalMissile(UUID missileID);

    Optional<UUID> lookupLogicalMissile(LogicalMissile logicalMissile);

    void deleteMissile(UUID missileID);

    void deleteMissile(LogicalMissile logicalMissile);

    void deleteMissile(EntityMissile puppetEntity);

    void deleteAllMissiles();

    void onWorldTickEvent(TickEvent.WorldTickEvent event);

    ServerWorld getLevel();

}
