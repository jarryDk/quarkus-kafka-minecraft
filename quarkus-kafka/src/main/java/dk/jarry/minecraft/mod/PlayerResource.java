package dk.jarry.minecraft.mod;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.reactive.messaging.Channel;

import io.smallrye.mutiny.Multi;

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
