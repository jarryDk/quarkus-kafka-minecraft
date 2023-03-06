package dk.jarry.kafkamod;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KafkaModEntityEventSubscriper {

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    private KafkaModEntityEventSubscriper(){
    }

    public static void register(){
        KafkaModEntityEventSubscriper eventSubscriper = new KafkaModEntityEventSubscriper();
        MinecraftForge.EVENT_BUS.register(eventSubscriper);
        LOGGER.info(eventSubscriper.getClass().getSimpleName() + " add to The core Forge EventBusses");
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
