package dk.jarry.kafkamod.control;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import dk.jarry.kafkamod.KafkaMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ServerSubscriber {

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    private ServerSubscriber() {
    }

    public static void register() {
        ServerSubscriber eventSubscriber = new ServerSubscriber();
        MinecraftForge.EVENT_BUS.register(eventSubscriber);
        LOGGER.info("{} add to The core Forge EventBusses", //
            eventSubscriber.getClass().getSimpleName());
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("{} - Server starting", //
            KafkaMod.class.getSimpleName());
    }

    @SubscribeEvent
    public void onServerStoppedEvent(ServerStoppedEvent event) {
        LOGGER.info("{} - Server stopping - Graceful close connection to Kafka", //
            KafkaMod.class.getSimpleName());
        KafkaMod.getProducer().ifPresent( e -> e.close());
    }

}
