package dk.jarry.minecraft.mod.entity;

import com.fasterxml.jackson.databind.JsonNode;

public class EntityEvent {

    double x;
    double y;
    double z;
    String event;
    String name;

    public EntityEvent(JsonNode entityEventOjb) {
        if (entityEventOjb.has("x")) {
            this.x = entityEventOjb.get("x").doubleValue();
        }
        if (entityEventOjb.has("y")) {
            this.y = entityEventOjb.get("y").doubleValue();
        }
        if (entityEventOjb.has("z")) {
            this.z = entityEventOjb.get("z").doubleValue();
        }
        if (entityEventOjb.has("event")) {
            this.event = entityEventOjb.get("event").asText();
        }
        if (entityEventOjb.has("name")) {
            this.name = entityEventOjb.get("name").asText();
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

    public String getEvent() {
        return event;
    }

    public String getName() {
        return name;
    }

}
