package dk.jarry.minecraft.mod.boundary;


import org.eclipse.microprofile.reactive.messaging.Channel;

import dk.jarry.minecraft.mod.entity.EntityEvent;
import io.smallrye.mutiny.Multi;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/eventEntities")
public class EntityEventResource {

    @Channel("entity-events")
    Multi<EntityEvent> entityEvents;

    /**
     * Endpoint retrieving the "chats" Kafka topic and sending the items to a
     * server sent event.
     */
    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<EntityEvent> stream() {
        return entityEvents;
    }

}
