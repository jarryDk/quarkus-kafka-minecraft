package dk.jarry.kafkamod.control;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import dk.jarry.kafkamod.KafkaMod;
import dk.jarry.kafkamod.KafkaProperties;
import dk.jarry.kafkamod.entity.EntityEventRecord;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EntityEventSubscriber {

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    private EntityEventSubscriber(){
    }

    public static void register(){
        EntityEventSubscriber eventSubscriber = new EntityEventSubscriber();
        MinecraftForge.EVENT_BUS.register(eventSubscriber);
        LOGGER.info("{} add to The core Forge EventBusses", //
            eventSubscriber.getClass().getSimpleName());
    }

    @SubscribeEvent
    public void onEntityJoinLevelEvent(EntityJoinLevelEvent event) {
        EntityEventRecord entityEventRecord = new EntityEventRecord(event);
        String key = entityEventRecord.getName();
        KafkaMod.addRecordToTopic(key, entityEventRecord.toJsonNode(), KafkaProperties.KAFKA_MOD_ENTITY_EVENT);
        LOGGER.debug(entityEventRecord.toString());
    }

    @SubscribeEvent
    public void onEntityLeaveLevelEvent(EntityLeaveLevelEvent event) {
        EntityEventRecord entityEventRecord = new EntityEventRecord(event);
        String key = entityEventRecord.getName();
        KafkaMod.addRecordToTopic(key, entityEventRecord.toJsonNode(), KafkaProperties.KAFKA_MOD_ENTITY_EVENT);
        LOGGER.debug(entityEventRecord.toString());
    }
}
