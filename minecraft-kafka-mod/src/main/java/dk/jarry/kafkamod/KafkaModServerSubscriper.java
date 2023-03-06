package dk.jarry.kafkamod;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KafkaModServerSubscriper {

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    private KafkaModServerSubscriper() {
    }

    public static void register() {
        KafkaModServerSubscriper eventSubscriper = new KafkaModServerSubscriper();
        MinecraftForge.EVENT_BUS.register(eventSubscriper);
        LOGGER.info(eventSubscriper.getClass().getSimpleName() + " add to The core Forge EventBusses");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info(KafkaMod.class.getSimpleName() + " - Server starting");
    }

    @SubscribeEvent
    public void onServerStoppedEvent(ServerStoppedEvent event) {
        LOGGER.info(KafkaMod.class.getSimpleName() + " - Server stopping - Graceful close connection to Kafka");
        KafkaMod.producer.close();
    }

}
