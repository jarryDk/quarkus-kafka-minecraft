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

    private KafkaModPlayerEventSubscriper(){
    }

    public static void register(){
        KafkaModPlayerEventSubscriper eventSubscriper = new KafkaModPlayerEventSubscriper();
        MinecraftForge.EVENT_BUS.register(eventSubscriper);
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        String stringUUID = player.getStringUUID();
        KafkaMod.playerInGame.put(stringUUID, player);

        LOGGER.info(KafkaModPlayerEventSubscriper.class.getSimpleName() + " - Client connected: " + player);

    }

    @SubscribeEvent
    public void onPlayerLoginOut(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        String stringUUID = player.getStringUUID();
        KafkaMod.playerInGame.remove(stringUUID);

        LOGGER.info(KafkaModPlayerEventSubscriper.class.getSimpleName() + " - Client disconnected: " + player);
    }

    @SubscribeEvent
    public void onPlayerChangedDimensionEvent(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        KafkaModPlayer pl = new KafkaModPlayer(player);
        LOGGER.info("ChangedDimension - " + pl);

        KafkaMod.playerInGame.values().stream().forEach( p -> {
            p.displayClientMessage(Component.literal("ChangedDimension of player - " + pl), true);
        });
    }

    @SubscribeEvent
    public void onPlayerRespawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        KafkaModPlayer pl = new KafkaModPlayer(player);
        LOGGER.info("Respawn - " + pl);

        KafkaMod.playerInGame.values().stream().forEach( p -> {
            p.displayClientMessage(Component.literal("Respawn of player - " + pl), true);
        });
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
