package dk.jarry.kafkamod.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;

import dk.jarry.kafkamod.KafkaMod;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;

@JsonPropertyOrder(alphabetic = true)
public class EntityEventRecord extends Location {

    Entity entity;
    EntityEvent event;

    public EntityEventRecord(EntityEvent event){
        super(event);
        this.event = event;
        this.entity = event.getEntity();
    }

    public String getName() {
        return entity.getDisplayName().getString();
    }

    public String getEvent() {
        return event.getClass().getSimpleName();
    }

    public int getEntityId(){
        return entity.getId();
    }

    public String getEntityPackage(){
        return entity.getClass().getPackage().getName();
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

    public JsonNode toJsonNode() {
        return KafkaMod.objectMapper.valueToTree(this);
    }

}
