package dk.jarry.kafkamod.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;

import dk.jarry.kafkamod.KafkaMod;

@JsonPropertyOrder(alphabetic = true)
public class ChatRecord {

    KafkaModPlayer player;
    String message;

    public ChatRecord(String message, String username) {
        this.message = message;
    }

    public ChatRecord(KafkaModPlayer player, String message) {
        this.player = player;
        this.message = message;
    }

    public KafkaModPlayer getPlayer() {
        return player;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ChatRecord [player=" + player + ", message=" + message + "]";
    }

    public JsonNode toJsonNode() {
        return KafkaMod.objectMapper.valueToTree(this);
    }

}
