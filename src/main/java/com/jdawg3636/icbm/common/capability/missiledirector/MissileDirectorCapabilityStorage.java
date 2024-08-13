package com.jdawg3636.icbm.common.capability.missiledirector;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class MissileDirectorCapabilityStorage implements Capability.IStorage<IMissileDirectorCapability> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<IMissileDirectorCapability> capability, IMissileDirectorCapability instance, Direction side) {
        CompoundNBT compound = new CompoundNBT();
        CompoundNBT logicalMissilesNBT = new CompoundNBT();
        instance.getLogicalMissileIDList().forEach(uuid -> {
            instance.lookupLogicalMissile(uuid).ifPresent(lm -> {
                CompoundNBT lmNBT = new CompoundNBT();
                lm.save(lmNBT);
                logicalMissilesNBT.put(uuid.toString(), lmNBT);
            });
        });
        compound.put("LogicalMissiles", logicalMissilesNBT);
        return compound;
    }

    @Override
    public void readNBT(Capability<IMissileDirectorCapability> capability, IMissileDirectorCapability instance, Direction side, INBT nbt) {
        CompoundNBT logicalMissilesNBT = ((CompoundNBT) nbt).getCompound("LogicalMissiles");
        instance.deleteAllMissiles();
        logicalMissilesNBT.getAllKeys().forEach(uuidString -> {
            UUID uuid = UUID.fromString(uuidString);
            LogicalMissile logicalMissile = new LogicalMissile(instance.getLevel(), logicalMissilesNBT.getCompound(uuidString));
            instance.registerMissile(logicalMissile, Optional.of(uuid));
        });
    }

}
