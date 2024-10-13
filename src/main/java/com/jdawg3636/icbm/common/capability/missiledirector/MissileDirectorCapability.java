package com.jdawg3636.icbm.common.capability.missiledirector;

import com.jdawg3636.icbm.common.entity.EntityMissile;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.TickEvent;

import java.util.*;
import java.util.function.BiConsumer;

public class MissileDirectorCapability implements IMissileDirectorCapability {

    private HashMap<UUID, LogicalMissile> logicalMissiles = new HashMap<>();
    private HashMap<UUID, BiConsumer<UUID, LogicalMissile>> listeners = new HashMap<>();
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
        UUID logicalUUID = forceLogicalUUID.orElseGet(this::generateUUID);
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
        Optional.ofNullable(logicalMissiles.remove(missileID)).ifPresent(logicalMissile -> {
            logicalMissile.kill();
            listeners.values().forEach(listener -> listener.accept(missileID, logicalMissile));
        });
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
        getLogicalMissileIDList().forEach(this::deleteMissile);
    }

    @Override
    public void onWorldTickEvent(TickEvent.WorldTickEvent event) {
        if(event.world instanceof ServerWorld) {
            // Have to make a new HashMap to avoid ConcurrentModificationException
            new HashMap<>(logicalMissiles).forEach((uuid, logicalMissile) -> {
                boolean missileChanged = logicalMissile.tick((ServerWorld) event.world);
                if (missileChanged) {
                    listeners.values().forEach(listener -> listener.accept(uuid, logicalMissile));
                }
            });
        }
    }

    @Override
    public UUID registerListener(BiConsumer<UUID, LogicalMissile> listener) {
        UUID uuid = this.generateUUID();
        this.listeners.put(uuid, listener);
        return uuid;
    }

    @Override
    public void removeListener(UUID uuid) {
        this.listeners.remove(uuid);
    }

    @Override
    public ServerWorld getLevel() {
        return level;
    }

    private UUID generateUUID() {
        long i = random.nextLong() & -61441L | 16384L;
        long j = random.nextLong() & 4611686018427387903L | Long.MIN_VALUE;
        return new UUID(i, j);
    }

}
