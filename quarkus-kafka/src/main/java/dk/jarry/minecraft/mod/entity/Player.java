package dk.jarry.minecraft.mod.entity;

import com.fasterxml.jackson.databind.JsonNode;

public class Player {

    double x;
    double y;
    double z;
    String ipAddress;
    String name;

    public Player(){
    }

    public Player(JsonNode playerObj){
        if (playerObj.has("x")){
            this.x = playerObj.get("x").doubleValue();
        }
        if (playerObj.has("y")){
            this.y = playerObj.get("y").doubleValue();
        }
        if (playerObj.has("z")){
            this.z = playerObj.get("z").doubleValue();
        }
        if (playerObj.has("ipAddress")){
            this.ipAddress = playerObj.get("ipAddress").asText();
        }
        if (playerObj.has("name")){
            this.name = playerObj.get("name").asText();
        }
    }


    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public double getZ(){
        return z;
    }

    public String getIpAddress(){
        return ipAddress;
    }

    public String getName() {
        return name;
    }

}
