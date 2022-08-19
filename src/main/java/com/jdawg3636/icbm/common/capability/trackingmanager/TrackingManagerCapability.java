package com.jdawg3636.icbm.common.capability.trackingmanager;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.CapabilityManager;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class TrackingManagerCapability implements ITrackingManagerCapability {

    private final HashMap<UUID, UUID> tickets = new HashMap<>();
    private final Random random = new Random();

    public static void register() {
        CapabilityManager.INSTANCE.register(ITrackingManagerCapability.class, new TrackingManagerCapabilityStorage(), TrackingManagerCapability::new);
    }

    @Override
    public UUID createTicket(UUID targetEntityID) {
        long i = random.nextLong() & -61441L | 16384L;
        long j = random.nextLong() & 4611686018427387903L | Long.MIN_VALUE;
        UUID newTicketID = new UUID(i, j);
        tickets.put(newTicketID, targetEntityID);
        return newTicketID;
    }

    @Override
    public void forceCreateTicket(UUID ticketID, UUID targetEntityID) {
        tickets.put(ticketID, targetEntityID);
    }

    @Override
    public Set<UUID> getTicketIDList() {
        return tickets.keySet();
    }

    @Override
    public UUID lookupTicket(UUID ticketID) {
        return tickets.get(ticketID);
    }

    @Override
    public void deleteTicket(UUID ticketID) {
        tickets.remove(ticketID);
    }

    @Override
    public void clearTickets(UUID targetEntityID) {
        tickets.keySet().iterator().forEachRemaining((UUID ticketID) -> {
            if(tickets.get(ticketID).equals(targetEntityID)) tickets.remove(ticketID);
        });
    }

    @Override
    public Vector3d getPos(ServerWorld level, UUID ticketID) {
        if(!tickets.containsKey(ticketID)) return null;
        Entity targetEntity = level.getEntity(tickets.get(ticketID));
        if(targetEntity == null) return null;
        return targetEntity.position();
    }

}
