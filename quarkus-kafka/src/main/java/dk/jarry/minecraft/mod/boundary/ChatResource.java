package dk.jarry.minecraft.mod.boundary;

import org.eclipse.microprofile.reactive.messaging.Channel;

import dk.jarry.minecraft.mod.entity.Chat;
import io.smallrye.mutiny.Multi;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/chats")
public class ChatResource {

    @Channel("chats")
    Multi<Chat> chats;

    /**
     * Endpoint retrieving the "chats" Kafka topic and sending the items to a
     * server sent event.
     */
    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<Chat> stream() {
        return chats;
    }

}
