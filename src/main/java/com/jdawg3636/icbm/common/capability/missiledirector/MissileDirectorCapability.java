package com.jdawg3636.icbm.common.capability.missiledirector;

import com.jdawg3636.icbm.common.entity.EntityMissile;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.TickEvent;

import java.util.*;

public class MissileDirectorCapability implements IMissileDirectorCapability {

    private final HashMap<UUID, LogicalMissile> logicalMissiles = new HashMap<>();
    private final Random random = new Random();
    private final World level;

//    public MissileDirectorCapability() {
//        this(null);
//    }

    public MissileDirectorCapability(World level) {
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
    public Collection<LogicalMissile> getLogicalMissiles() {
        return logicalMissiles.values();
    }

    @Override
    public LogicalMissile lookupLogicalMissile(UUID missileID) {
        return logicalMissiles.get(missileID);
    }

    @Override
    public void deleteMissile(UUID missileID) {
        logicalMissiles.remove(missileID).kill(level);
    }

    @Override
    public void deleteMissile(LogicalMissile logicalMissile) {
        getLogicalMissileIDList().stream().filter(uuid -> lookupLogicalMissile(uuid).equals(logicalMissile)).forEach(this::deleteMissile);
    }

    @Override
    public void deleteMissile(EntityMissile puppetEntity) {
        logicalMissiles.entrySet().stream().filter(entry -> entry.getValue().puppetEntity.map(pe -> pe.equals(puppetEntity)).orElse(false)).forEach(entry -> deleteMissile(entry.getKey()));
    }

    @Override
    public void deleteAllMissiles() {
        getLogicalMissileIDList().forEach(this::deleteMissile);
    }

    @Override
    public void onWorldTickEvent(TickEvent.WorldTickEvent event) {
        if(event.world instanceof ServerWorld) {
            logicalMissiles.values().forEach(missile -> missile.tick((ServerWorld) event.world));
        }
    }

}
