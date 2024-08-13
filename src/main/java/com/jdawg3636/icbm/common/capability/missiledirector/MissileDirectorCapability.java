package com.jdawg3636.icbm.common.capability.missiledirector;

import com.jdawg3636.icbm.common.entity.EntityMissile;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.TickEvent;

import java.util.*;

public class MissileDirectorCapability implements IMissileDirectorCapability {

    private HashMap<UUID, LogicalMissile> logicalMissiles = new HashMap<>();
    private final Random random = new Random();
    private final ServerWorld level;

//    public MissileDirectorCapability() {
//        this(null);
//    }

    public MissileDirectorCapability(ServerWorld level) {
        this.level = level;
    }

    public static void register() {
//        CapabilityManager.INSTANCE.register(IMissileDirectorCapability.class, new MissileDirectorCapabilityStorage(), MissileDirectorCapability::new);
        CapabilityManager.INSTANCE.register(IMissileDirectorCapability.class, new MissileDirectorCapabilityStorage(), () -> null);
    }

    @Override
    public UUID registerMissile(LogicalMissile logicalMissile, Optional<UUID> forceLogicalUUID) {
        UUID logicalUUID = forceLogicalUUID.orElseGet(() -> {
            long i = random.nextLong() & -61441L | 16384L;
            long j = random.nextLong() & 4611686018427387903L | Long.MIN_VALUE;
            return new UUID(i, j);
        });
        logicalMissiles.put(logicalUUID, logicalMissile);
        return logicalUUID;
    }

    @Override
    public Set<UUID> getLogicalMissileIDList() {
        return logicalMissiles.keySet();
    }

    @Override
    public HashMap<UUID, LogicalMissile> getLogicalMissiles() {
        return logicalMissiles;
    }

    @Override
    public Optional<LogicalMissile> lookupLogicalMissile(UUID missileID) {
        return Optional.ofNullable(logicalMissiles.get(missileID));
    }

    @Override
    public Optional<UUID> lookupLogicalMissile(LogicalMissile logicalMissile) {
        return logicalMissiles.entrySet().stream().filter(entry -> entry.getValue().equals(logicalMissile)).map(Map.Entry::getKey).findAny();
    }

    @Override
    public void deleteMissile(UUID missileID) {
        if(missileID == null) return;
        Optional.ofNullable(logicalMissiles.remove(missileID)).ifPresent(LogicalMissile::kill);
    }

    @Override
    public void deleteMissile(LogicalMissile logicalMissile) {
        if(logicalMissile == null) return;
        new HashMap<>(logicalMissiles).entrySet().stream().filter(entry -> entry.getValue().equals(logicalMissile)).map(Map.Entry::getKey).forEach(this::deleteMissile);
    }

    @Override
    public void deleteMissile(EntityMissile puppetEntity) {
        if(puppetEntity == null) return;
        new HashMap<>(logicalMissiles).entrySet().stream().filter(entry -> entry.getValue().getPuppetEntity().map(pe -> pe.equals(puppetEntity)).orElse(false)).forEach(entry -> deleteMissile(entry.getKey()));
    }

    @Override
    public void deleteAllMissiles() {
        logicalMissiles = new HashMap<>();
    }

    @Override
    public void onWorldTickEvent(TickEvent.WorldTickEvent event) {
        if(event.world instanceof ServerWorld) {
            // Have to make a new HashMap to avoid ConcurrentModificationException
            new HashMap<>(logicalMissiles).values().forEach(missile -> missile.tick((ServerWorld) event.world));
        }
    }

    @Override
    public ServerWorld getLevel() {
        return level;
    }

}
