package dk.jarry.minecraft.mod.control;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dk.jarry.minecraft.mod.entity.Player;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class ItemStackProcessorTest {

  @Inject
  ItemStackProcessor processor;

  @Test
  void process() throws InterruptedException {

    Player player = processor.process(ITME_STACK);

    assertEquals("127.0.0.1", player.getIpAddress());
    assertEquals("jarry_dk", player.getName());
    assertEquals(15.667966305570742, player.getX());
    assertEquals(58.53584062504456, player.getY());
    assertEquals(-6.168235685710132, player.getZ());

  }

  final static String ITME_STACK = """
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

}
