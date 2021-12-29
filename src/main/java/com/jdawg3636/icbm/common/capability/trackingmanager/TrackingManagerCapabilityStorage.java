package com.jdawg3636.icbm.common.capability.trackingmanager;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.UUID;

public class TrackingManagerCapabilityStorage implements Capability.IStorage<ITrackingManagerCapability> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<ITrackingManagerCapability> capability, ITrackingManagerCapability instance, Direction side) {

        CompoundNBT nbt = new CompoundNBT();
        CompoundNBT tickets = new CompoundNBT();

        for(UUID ticketID : instance.getTicketIDList()) {
            tickets.putUUID(ticketID.toString(), instance.lookupTicket(ticketID));
        }

        nbt.put("tickets", tickets);
        return nbt;

    }

    @Override
    public void readNBT(Capability<ITrackingManagerCapability> capability, ITrackingManagerCapability instance, Direction side, INBT nbt) {
        CompoundNBT tickets = ((CompoundNBT) nbt).getCompound("tickets");
        for (String ticketID : tickets.getAllKeys()) {
            instance.forceCreateTicket(UUID.fromString(ticketID), tickets.getUUID(ticketID));
        }
    }

}
