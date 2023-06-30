package dk.jarry.minecraft.mod.control;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dk.jarry.minecraft.mod.entity.EntityEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EntityEventProcessor {

    @Inject
    ObjectMapper objectMapper;

    @Incoming("kafka-mod-entity-event")
    @Outgoing("entity-events")
    public EntityEvent process(String record) throws InterruptedException {

        EntityEvent entityEvent = null;
        try {
            JsonNode entityEventObj = objectMapper.readTree(record);
            entityEvent = new EntityEvent(entityEventObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entityEvent;

    }

}
