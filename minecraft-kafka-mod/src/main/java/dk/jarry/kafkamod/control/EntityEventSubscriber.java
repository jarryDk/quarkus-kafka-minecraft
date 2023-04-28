package dk.jarry.kafkamod.control;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import dk.jarry.kafkamod.KafkaMod;
import dk.jarry.kafkamod.KafkaProperties;
import dk.jarry.kafkamod.entity.EntityRecord;
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
        LOGGER.info(eventSubscriber.getClass().getSimpleName() + " add to The core Forge EventBusses");
    }

    @SubscribeEvent
    public void onEntityJoinLevelEvent(EntityJoinLevelEvent event) {
        EntityRecord entityRecord = new EntityRecord(event);
        String key = entityRecord.getName();
        KafkaMod.addRecordToTopic(key, entityRecord.toJsonNode(), KafkaProperties.KAFKA_MOD_ENTITY_EVENT);
        LOGGER.debug(entityRecord.toString());
    }

    @SubscribeEvent
    public void onEntityLeaveLevelEvent(EntityLeaveLevelEvent event) {
        EntityRecord entityRecord = new EntityRecord(event);
        String key = entityRecord.getName();
        KafkaMod.addRecordToTopic(key, entityRecord.toJsonNode(), KafkaProperties.KAFKA_MOD_ENTITY_EVENT);
        LOGGER.debug(entityRecord.toString());
    }
}
