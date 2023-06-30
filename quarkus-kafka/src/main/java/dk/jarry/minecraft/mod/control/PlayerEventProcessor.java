package dk.jarry.minecraft.mod.control;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dk.jarry.minecraft.mod.entity.Player;
import dk.jarry.minecraft.mod.entity.PlayerEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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

    @Incoming("kafka-mod-player-event")
    @Outgoing("players")
    public Player process(String record) throws InterruptedException {

        PlayerEvent playerEvent = null;
        try {
            JsonNode playerEventObj = objectMapper.readTree(record);
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

        return playerEvent.getPlayer();

    }

}
