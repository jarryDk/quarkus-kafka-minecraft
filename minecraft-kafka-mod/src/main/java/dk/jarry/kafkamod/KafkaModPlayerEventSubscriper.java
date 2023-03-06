package dk.jarry.kafkamod;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.mojang.logging.LogUtils;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KafkaModPlayerEventSubscriper {

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    private KafkaModPlayerEventSubscriper() {
    }

    public static void register() {
        KafkaModPlayerEventSubscriper eventSubscriper = new KafkaModPlayerEventSubscriper();
        MinecraftForge.EVENT_BUS.register(eventSubscriper);
        LOGGER.info(eventSubscriper.getClass().getSimpleName() + " add to The core Forge EventBusses");
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {

        PlayerEventRecord playerEventRecord = new PlayerEventRecord(event);

        String key = playerEventRecord.getName();
        KafkaMod.playerInGame.put(playerEventRecord.getStringUUID(), playerEventRecord.getPlayer());

        KafkaMod.addRecordToTopic(key, playerEventRecord.toJsonNode(), KafkaProperties.KAFKA_MOD_PLAYER_EVENT);

        LOGGER.info(KafkaModPlayerEventSubscriper.class.getSimpleName() + " - Client connected: "
                + playerEventRecord.getPlayer());

    }

    @SubscribeEvent
    public void onPlayerLoginOut(PlayerEvent.PlayerLoggedOutEvent event) {

        PlayerEventRecord playerEventRecord = new PlayerEventRecord(event);

        String key = playerEventRecord.getName();
        KafkaMod.playerInGame.remove(playerEventRecord.getStringUUID());

        KafkaMod.addRecordToTopic(key, playerEventRecord.toJsonNode(), KafkaProperties.KAFKA_MOD_PLAYER_EVENT);

        LOGGER.info(KafkaModPlayerEventSubscriper.class.getSimpleName() + " - Client disconnected: "
                + playerEventRecord.getPlayer());
    }

    @SubscribeEvent
    public void onPlayerChangedDimensionEvent(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        KafkaModPlayer pl = new KafkaModPlayer(player);
        LOGGER.info("ChangedDimension - " + pl);

        KafkaMod.playerInGame.values().stream().forEach(p -> {
            p.displayClientMessage(Component.literal("ChangedDimension of player - " + pl), true);
        });
    }

    @SubscribeEvent
    public void onPlayerRespawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        KafkaModPlayer pl = new KafkaModPlayer(player);
        LOGGER.info("Respawn - " + pl);

        KafkaMod.playerInGame.values().stream().forEach(p -> {
            p.displayClientMessage(Component.literal("Respawn of player - " + pl), true);
        });

        // If you like some fun when respawn !!!
        // KafkaMod.dropTnTByPlayer(player, 3);

    }

    @SubscribeEvent
    public void onItemCraftedEvent(PlayerEvent.ItemCraftedEvent event) {
        Player player = event.getEntity();
        KafkaModPlayer kmPlayer = new KafkaModPlayer(player);

        ItemStack stack = event.getCrafting();
        LOGGER.info("Component Crafted : " + stack.getDisplayName().getString());

        ItemStackRecord cr = new ItemStackRecord(kmPlayer, "Crafted", stack);
        String key = kmPlayer.getName();
        JsonNode record = KafkaMod.objectMapper.valueToTree(cr);

        KafkaMod.addRecordToTopic(key, record, KafkaProperties.KAFKA_MOD_ITEM_STACK);

    }

    @SubscribeEvent
    public void onItemPickupEvent(PlayerEvent.ItemPickupEvent event) {
        Player player = event.getEntity();
        KafkaModPlayer kmPlayer = new KafkaModPlayer(player);

        ItemStack stack = event.getStack();
        LOGGER.info("Component Pickup : " + stack.getDisplayName().getString());

        ItemStackRecord cr = new ItemStackRecord(kmPlayer, "Pickup", stack);
        String key = kmPlayer.getName();
        JsonNode record = KafkaMod.objectMapper.valueToTree(cr);

        KafkaMod.addRecordToTopic(key, record, KafkaProperties.KAFKA_MOD_ITEM_STACK);

    }

}
