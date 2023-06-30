package dk.jarry.minecraft.mod.entity;

import com.fasterxml.jackson.databind.JsonNode;

public class Chat {

    Player player;
    String message;

    public Chat() {
    }

    public Chat(JsonNode chatObj) {

        if (chatObj.has("player")) {
            this.player = new Player(chatObj.get("player"));
        }

        if (chatObj.has("message")) {
            this.message = chatObj.get("message").asText();
        }
    }

    public Player getPlayer() {
        return player;
    }

    public String getMessage() {
        return message;
    }

}
