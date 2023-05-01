package dk.jarry.minecraft.mod;

import org.eclipse.microprofile.reactive.messaging.Channel;

import io.smallrye.mutiny.Multi;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/players")
public class PlayerResource {

    @Channel("players")
    Multi<Player> players;

    /**
     * Endpoint retrieving the "players" Kafka topic and sending the items to a
     * server sent event.
     */
    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<Player> stream() {
        return players;
    }

}
