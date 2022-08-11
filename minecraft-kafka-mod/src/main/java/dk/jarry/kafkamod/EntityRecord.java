package dk.jarry.kafkamod;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;

@JsonPropertyOrder(alphabetic = true)
public class EntityRecord extends Location {

    Entity entity;
    EntityEvent event;

    public EntityRecord(EntityEvent event ){
        super(event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ());
        this.event = event;
        this.entity = event.getEntity();
    }

    public String getName() {
        return entity.getDisplayName().getString();
    }

    public String getEvent() {
        return event.getClass().getSimpleName();
    }

    @Override
    public String toString() {
        String action = "";
        if (event instanceof EntityJoinLevelEvent) {
            action = " join the world";
        } else if (event instanceof EntityLeaveLevelEvent) {
            action = " leave the world";
        }
        return "Entity - name: " + getName() + " " + action + " - location : { x:" + x + ", y:" + y + ", z:"
                + z + "}";
    }

}
