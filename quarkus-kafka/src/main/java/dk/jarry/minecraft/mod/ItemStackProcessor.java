package dk.jarry.minecraft.mod;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationScoped
public class ItemStackProcessor {

    @Inject
    ObjectMapper objectMapper;

    @Incoming("item-stack")
    @Outgoing("players")
    public Player process(String itemStack) throws InterruptedException {

        Player player = null;
        try {
            JsonNode itemStackObj = objectMapper.readTree(itemStack);
            JsonNode playerObj = itemStackObj.get("player");
            player = new Player(playerObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return player;
    }

}
