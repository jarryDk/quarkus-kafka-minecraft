package dk.jarry.minecraft.mod.entity;

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

    @Override
    public String toString() {
        return "PlayerEvent [x=" + x + ", y=" + y + ", z=" + z + ", ipAddress=" + ipAddress + ", name=" + name
                + ", stringUUID=" + stringUUID + ", typeOfEvent=" + typeOfEvent + ", timeStamp=" + timeStamp + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((stringUUID == null) ? 0 : stringUUID.hashCode());
        result = prime * result + ((typeOfEvent == null) ? 0 : typeOfEvent.hashCode());
        result = prime * result + ((timeStamp == null) ? 0 : timeStamp.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PlayerEvent other = (PlayerEvent) obj;
        if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
            return false;
        if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
            return false;
        if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
            return false;
        if (ipAddress == null) {
            if (other.ipAddress != null)
                return false;
        } else if (!ipAddress.equals(other.ipAddress))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (stringUUID == null) {
            if (other.stringUUID != null)
                return false;
        } else if (!stringUUID.equals(other.stringUUID))
            return false;
        if (typeOfEvent == null) {
            if (other.typeOfEvent != null)
                return false;
        } else if (!typeOfEvent.equals(other.typeOfEvent))
            return false;
        if (timeStamp == null) {
            if (other.timeStamp != null)
                return false;
        } else if (!timeStamp.equals(other.timeStamp))
            return false;
        return true;
    }

    

}
