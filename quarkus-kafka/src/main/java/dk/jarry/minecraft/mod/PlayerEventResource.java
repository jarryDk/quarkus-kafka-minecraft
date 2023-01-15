package dk.jarry.minecraft.mod;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.fasterxml.jackson.databind.JsonNode;

@Path("/playerEvents")
public class PlayerEventResource {

    @Inject
    PlayerEventProcessor playerEventProcessor;

    /**
     * Endpoint retrieving the "players" Kafka topic and sending the items to a
     * server sent event.
     */
    @GET
    public List<JsonNode> getPlayerEvents() {

        List<JsonNode> playerEvents = new ArrayList<>();

        playerEventProcessor.getPlayerEvents().values()
                .forEach(event -> playerEvents.add(event.toJsonNode()));

        return playerEvents;

    }

    @Path("/active")
    @GET
    public List<JsonNode> getActivePlayerEvents() {

        List<JsonNode> playerEvents = new ArrayList<>();

        playerEventProcessor.getActivePlayerEvents().values()
                .forEach(event -> playerEvents.add(event.toJsonNode()));

        return playerEvents;

    }

}
