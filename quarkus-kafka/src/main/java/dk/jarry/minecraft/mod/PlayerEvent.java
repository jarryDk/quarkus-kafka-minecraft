package dk.jarry.minecraft.mod;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PlayerEvent {

    double x;
    double y;
    double z;
    String ipAddress;
    String name;
    String stringUUID;
    String typeOfEvent;
    String timeStamp;

    public PlayerEvent(){
        
    }

    public PlayerEvent(JsonNode playerEventObj) {
        if (playerEventObj.has("x")){
            this.x = playerEventObj.get("x").doubleValue();
        }
        if (playerEventObj.has("y")){
            this.y = playerEventObj.get("y").doubleValue();
        }
        if (playerEventObj.has("z")){
            this.z = playerEventObj.get("z").doubleValue();
        }
        if (playerEventObj.has("ipAddress")){
            this.ipAddress = playerEventObj.get("ipAddress").asText();
        }
        if (playerEventObj.has("name")){
            this.name = playerEventObj.get("name").asText();
        }
        if (playerEventObj.has("stringUUID")){
            this.stringUUID = playerEventObj.get("stringUUID").asText();
        }
        if (playerEventObj.has("typeOfEvent")){
            this.typeOfEvent = playerEventObj.get("typeOfEvent").asText();
        }
        if (playerEventObj.has("timeStamp")){
            this.timeStamp = playerEventObj.get("timeStamp").asText();
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getName() {
        return name;
    }

    public String getStringUUID() {
        return stringUUID;
    }

    public String getTypeOfEvent() {
        return typeOfEvent;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public JsonNode toJsonNode() {
        return  new ObjectMapper().valueToTree(this);
    }

}
