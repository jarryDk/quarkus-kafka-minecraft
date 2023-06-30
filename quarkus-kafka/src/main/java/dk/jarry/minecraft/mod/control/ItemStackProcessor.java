package dk.jarry.minecraft.mod.control;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dk.jarry.minecraft.mod.entity.Player;

@ApplicationScoped
public class ItemStackProcessor {

    @Inject
    ObjectMapper objectMapper;

    @Incoming("kafka-mod-item-stack")
    @Outgoing("players")
    public Player process(String record) throws InterruptedException {

        Player player = null;
        try {
            JsonNode itemStackObj = objectMapper.readTree(record);
            JsonNode playerObj = itemStackObj.get("player");
            player = new Player(playerObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return player;
    }

}
