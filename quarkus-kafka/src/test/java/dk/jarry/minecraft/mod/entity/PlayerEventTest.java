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
public class PlayerEventTest {

    @Inject
    ObjectMapper objectMapper;

    @Test
    void jsonNodeConstructor() throws JsonMappingException, JsonProcessingException {

        String playerEventString = """
                {
                    "x" : 15.667966305570742,
                    "y" : 58.53584062504456,
                    "z" : -6.168235685710132,
                    "ipAddress" : "127.0.0.1",
                    "name" : "jarry_dk",
                    "stringUUID" : "b8b308b9-8ea9-4c57-98e2-721ae72ba0dd",
                    "typeOfEvent" : "Pickup",
                    "timeStamp" : "timeStamp"
                }
                        """;

        JsonNode playerEventObj = objectMapper.readTree(playerEventString);
        PlayerEvent playerEvent = new PlayerEvent(playerEventObj);

        assertEquals(15.667966305570742, playerEvent.x);
        assertEquals(58.53584062504456, playerEvent.y);
        assertEquals(-6.168235685710132, playerEvent.z);

        assertEquals("127.0.0.1", playerEvent.ipAddress);
        assertEquals("jarry_dk", playerEvent.name);
        assertEquals("b8b308b9-8ea9-4c57-98e2-721ae72ba0dd", playerEvent.stringUUID);
        assertEquals("Pickup", playerEvent.typeOfEvent);
        assertEquals("timeStamp", playerEvent.timeStamp);

        JsonNode toJsonNode = playerEvent.toJsonNode();
        assertEquals(playerEventObj, toJsonNode);

    }

}
