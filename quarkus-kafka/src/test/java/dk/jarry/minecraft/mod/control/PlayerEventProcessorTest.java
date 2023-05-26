package dk.jarry.minecraft.mod.control;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class PlayerEventProcessorTest {

    @Inject
    PlayerEventProcessor processor;

    @Test
    void process() throws InterruptedException {

        processor.process(PLAYER_EVENT_PICKUP_EVENT);
        assertTrue(processor.getPlayerEvents().size() == 1);

        processor.process(PLAYER_EVENT_LOGGED_IN_EVENT);
        assertTrue(processor.getActivePlayerEvents().size() == 1);

        processor.process(PLAYER_EVENT_LOGGED_OUT_EVENT);
        assertTrue(processor.getActivePlayerEvents().size() == 0);

    }

    final static String PLAYER_EVENT_PICKUP_EVENT = """
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

    final static String PLAYER_EVENT_LOGGED_IN_EVENT = """
            {
                "x" : 15.667966305570742,
                "y" : 58.53584062504456,
                "z" : -6.168235685710132,
                "ipAddress" : "127.0.0.1",
                "name" : "jarry_dk",
                "stringUUID" : "b8b308b9-8ea9-4c57-98e2-721ae72ba0dd",
                "typeOfEvent" : "PlayerLoggedInEvent",
                "timeStamp" : "timeStamp"
            }
                    """;

    final static String PLAYER_EVENT_LOGGED_OUT_EVENT = """
            {
                "x" : 15.667966305570742,
                "y" : 58.53584062504456,
                "z" : -6.168235685710132,
                "ipAddress" : "127.0.0.1",
                "name" : "jarry_dk",
                "stringUUID" : "b8b308b9-8ea9-4c57-98e2-721ae72ba0dd",
                "typeOfEvent" : "PlayerLoggedOutEvent",
                "timeStamp" : "timeStamp"
            }
                    """;
}
