package dk.jarry.kafkamod;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

@JsonPropertyOrder(alphabetic = true)
public class PlayerEventRecord extends KafkaModPlayer {

    PlayerEvent event;
    Player player;
    String typeOfEvent;
    String timeStamp;

    public PlayerEventRecord(PlayerEvent event) {
        super(event.getEntity());
        this.timeStamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT);
        this.event = event;
        this.player = event.getEntity();
        typeOfEvent = event.getClass().getSimpleName();
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getTypeOfEvent() {
        return typeOfEvent;
    }

    @Override
    public String toString() {
        String action = "";
        if (event instanceof PlayerEvent.PlayerLoggedInEvent) {
            action = " join the world";
        } else if (event instanceof PlayerEvent.PlayerLoggedOutEvent) {
            action = " leave the world";
        }
        return "Player: " + getName() + " (" + getStringUUID() + ")" + " " + action //
                + " at timestamp : " + getTimeStamp() //
                + " - location : { x:" + x + ", y:" + y + ", z:" + z + "}";
    }

    public JsonNode toJsonNode() {
        return KafkaMod.objectMapper.valueToTree(this);
    }

}
