package dk.jarry.minecraft.mod;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationScoped
public class PlayerEventProcessor {

    @Inject
    ObjectMapper objectMapper;

    Map<String, PlayerEvent> playerEvents = new HashMap<>();
    Map<String, PlayerEvent> activePlayerEvents = new HashMap<>();

    public Map<String, PlayerEvent> getPlayerEvents(){
        return playerEvents;
    }

    public Map<String, PlayerEvent> getActivePlayerEvents(){
        return activePlayerEvents;
    }

    @Incoming("player-event")
    // @Outgoing("players")
    public void process(String playerEventString) throws InterruptedException {

        PlayerEvent playerEvent = null;
        try {
            JsonNode playerEventObj = objectMapper.readTree(playerEventString);
            playerEvent = new PlayerEvent(playerEventObj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        playerEvents.put(playerEvent.getName(), playerEvent);

        if ("PlayerLoggedInEvent".equals(playerEvent.getTypeOfEvent())) {
            activePlayerEvents.put(playerEvent.getName(), playerEvent);
        } else if ("PlayerLoggedOutEvent".equals(playerEvent.getTypeOfEvent())) {
            if (activePlayerEvents.containsKey(playerEvent.getName())) {
                activePlayerEvents.remove(playerEvent.getName());
            } else {
                System.err.println("How did " + playerEvent.getName() + " get into the game?");
            }
        }

        // return playerEvent;
    }

}
