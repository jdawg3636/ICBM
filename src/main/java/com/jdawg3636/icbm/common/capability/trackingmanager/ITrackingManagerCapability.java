package com.jdawg3636.icbm.common.capability.trackingmanager;

import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import java.util.Set;
import java.util.UUID;

public interface ITrackingManagerCapability {

    UUID createTicket(UUID targetEntityID);

    void forceCreateTicket(UUID ticketID, UUID targetEntityID);

    Set<UUID> getTicketIDList();

    UUID lookupTicket(UUID ticketID);

    void deleteTicket(UUID ticketID);

    void clearTickets(UUID targetEntityID);

    Vector3d getPos(ServerWorld level, UUID ticketID);

}
