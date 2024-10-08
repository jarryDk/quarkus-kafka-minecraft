package dk.jarry.kafkamod.control;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.mojang.logging.LogUtils;

import dk.jarry.kafkamod.KafkaMod;
import dk.jarry.kafkamod.KafkaProperties;
import dk.jarry.kafkamod.entity.ItemStackRecord;
import dk.jarry.kafkamod.entity.KafkaModPlayer;
import dk.jarry.kafkamod.entity.PlayerEventRecord;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerEventSubscriber {

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    private PlayerEventSubscriber() {
    }

    public static void register() {
        PlayerEventSubscriber eventSubscriber = new PlayerEventSubscriber();
        MinecraftForge.EVENT_BUS.register(eventSubscriber);
        LOGGER.info("{} add to The core Forge EventBusses", //
            eventSubscriber.getClass().getSimpleName());
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {

        PlayerEventRecord playerEventRecord = new PlayerEventRecord(event);

        String key = playerEventRecord.getName();
        KafkaMod.playerInGame.put(playerEventRecord.getStringUUID(), playerEventRecord.getPlayer());

        KafkaMod.addRecordToTopic(key, playerEventRecord.toJsonNode(), KafkaProperties.KAFKA_MOD_PLAYER_EVENT);

        LOGGER.info("{} - Client connected: {}", //
            PlayerEventSubscriber.class.getSimpleName(), playerEventRecord.getPlayer());

    }

    @SubscribeEvent
    public void onPlayerLoginOut(PlayerEvent.PlayerLoggedOutEvent event) {

        PlayerEventRecord playerEventRecord = new PlayerEventRecord(event);

        String key = playerEventRecord.getName();
        KafkaMod.playerInGame.remove(playerEventRecord.getStringUUID());

        KafkaMod.addRecordToTopic(key, playerEventRecord.toJsonNode(), KafkaProperties.KAFKA_MOD_PLAYER_EVENT);

        LOGGER.info("{} - Client disconnected: {}", //
            PlayerEventSubscriber.class.getSimpleName(), playerEventRecord.getPlayer());
    }

    @SubscribeEvent
    public void onPlayerChangedDimensionEvent(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        KafkaModPlayer pl = new KafkaModPlayer(player);
        LOGGER.info("ChangedDimension - {}", pl);

        KafkaMod.playerInGame.values().stream().forEach(p -> {
            p.displayClientMessage(Component.literal("ChangedDimension of player - " + pl), true);
        });
    }

    @SubscribeEvent
    public void onPlayerRespawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        KafkaModPlayer pl = new KafkaModPlayer(player);
        LOGGER.info("Respawn - {}", pl);

        KafkaMod.playerInGame.values().stream().forEach(p -> {
            p.displayClientMessage(Component.literal("Respawn of player - " + pl), true);
        });

        // If you like some fun when respawn !!!
        // Handout.dropTnTByPlayer(player, 3);

    }

    @SubscribeEvent
    public void onItemCraftedEvent(PlayerEvent.ItemCraftedEvent event) {
        Player player = event.getEntity();
        KafkaModPlayer kmPlayer = new KafkaModPlayer(player);

        ItemStack stack = event.getCrafting();
        LOGGER.info("Component Crafted : {}", //
                stack.getDisplayName().getString());

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
        LOGGER.info("Component Pickup by {}: {}", //
            kmPlayer.getName(), //
            stack.getDisplayName().getString());

        ItemStackRecord cr = new ItemStackRecord(kmPlayer, "Pickup", stack);
        String key = kmPlayer.getName();
        JsonNode record = KafkaMod.objectMapper.valueToTree(cr);

        KafkaMod.addRecordToTopic(key, record, KafkaProperties.KAFKA_MOD_ITEM_STACK);

    }

}
