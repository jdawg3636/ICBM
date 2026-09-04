package com.jdawg3636.icbm.common.capability.missiledirector;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
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
            // Within this constructor, the missile registers ITSELF to the missile director capability. This is done to make
            // sure that registration occurs prior to deserializing NBT (otherwise puppet entity won't be able to look up
            // its logical counterpart during any of those calls). Alternative would be constructor returning an uninitialized shell,
            // which would be bad.
            new LogicalMissile(instance.getLevel(), logicalMissilesNBT.getCompound(uuidString), uuid);
        });
    }

}
