package dk.jarry.kafkamod;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KafkaModEventSubscriper {

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    private KafkaModEventSubscriper() {
    }

    public static void register() {
        KafkaModEventSubscriper eventSubscriper = new KafkaModEventSubscriper();
        MinecraftForge.EVENT_BUS.register(eventSubscriper);
        LOGGER.info(eventSubscriper.getClass().getSimpleName() + " add to The core Forge EventBusses");
    }

    @SubscribeEvent
    public void onChat(net.minecraftforge.event.ServerChatEvent event) {

        ServerPlayer player = event.getPlayer();
        String message = event.getMessage().getString();

        KafkaModPlayer kmPlayer = new KafkaModPlayer(player);
        ChatRecord cr = new ChatRecord(kmPlayer, message);

        String key = kmPlayer.getName();

        KafkaMod.addRecordToTopic(key, cr.toJsonNode(), KafkaProperties.KAFKA_MOD_CHAT);

        LOGGER.info("Player : " + player + " - Message : " + message);

    }

}
