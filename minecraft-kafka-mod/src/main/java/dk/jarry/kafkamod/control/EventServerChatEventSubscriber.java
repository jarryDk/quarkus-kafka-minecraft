package dk.jarry.kafkamod.control;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import dk.jarry.kafkamod.KafkaMod;
import dk.jarry.kafkamod.KafkaProperties;
import dk.jarry.kafkamod.entity.ChatRecord;
import dk.jarry.kafkamod.entity.KafkaModPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventServerChatEventSubscriber {

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    private EventServerChatEventSubscriber() {
    }

    public static void register() {
        EventServerChatEventSubscriber eventSubscriber = new EventServerChatEventSubscriber();
        MinecraftForge.EVENT_BUS.register(eventSubscriber);
        LOGGER.info("{} add to The core Forge EventBusses", //
            eventSubscriber.getClass().getSimpleName());
    }

    @SubscribeEvent
    public void onChat(net.minecraftforge.event.ServerChatEvent event) {

        ServerPlayer player = event.getPlayer();
        String message = event.getMessage().getString();

        KafkaModPlayer kmPlayer = new KafkaModPlayer(player);
        ChatRecord cr = new ChatRecord(kmPlayer, message);

        String key = kmPlayer.getName();

        KafkaMod.addRecordToTopic(key, cr.toJsonNode(), KafkaProperties.KAFKA_MOD_CHAT);

        LOGGER.info("Player : {} - Message : {}", player, message);

    }

}
