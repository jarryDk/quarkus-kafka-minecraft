package dk.jarry.minecraft.mod.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class PlayerTest {

    @Inject
    ObjectMapper objectMapper;

    @Test
    void jsonNodeConstructor() throws JsonMappingException, JsonProcessingException {

        String itemStack = """
                {
                    "displayName": "Cobblestone",
                    "player": {
                      "ipAddress": "127.0.0.1",
                      "name": "jarry_dk",
                      "stringUUID": "b8b308b9-8ea9-4c57-98e2-721ae72ba0dd",
                      "x": 15.667966305570742,
                      "y": 58.53584062504456,
                      "z": -6.168235685710132
                    },
                    "typeOfEvent": "Pickup"
                  }
                    """;

        JsonNode itemStackObj = objectMapper.readTree(itemStack);
        JsonNode playerObj = itemStackObj.get("player");

        Player player = new Player(playerObj);
        assertEquals("127.0.0.1", player.ipAddress);
        assertEquals("jarry_dk", player.name);
        assertEquals(15.667966305570742, player.x);
        assertEquals(58.53584062504456, player.y);
        assertEquals(-6.168235685710132, player.z);

    }

}
