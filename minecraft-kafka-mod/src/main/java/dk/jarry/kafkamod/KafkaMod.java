package dk.jarry.kafkamod;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.TopicExistsException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mojang.logging.LogUtils;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("kafkamod")
public class KafkaMod {

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    final String KAFKA_MOD_CHAT = "kafka-mod-chat";
    final String KAFKA_MOD_ITEM_STACK = "kafka-mod-item-stack";
    Producer<String, JsonNode> producer;
    final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaMod() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // Some example code to dispatch IMC to another mod
        InterModComms.sendTo("kafkamod", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        // Some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.messageSupplier().get()).
                collect(Collectors.toList()));
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

        // Do something when the server starts
        LOGGER.info(KafkaMod.class.getSimpleName() + " - HELLO from server starting");

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        createTopic(KAFKA_MOD_CHAT, props);
        createTopic(KAFKA_MOD_ITEM_STACK, props);
        producer = new KafkaProducer<String, JsonNode>(props);

    }

    public static void createTopic(final String topic, final Properties config) {
        final NewTopic newTopic = new NewTopic(topic, Optional.empty(), Optional.empty());
        try (final AdminClient adminClient = AdminClient.create(config)) {
            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
        } catch (final InterruptedException | ExecutionException e) {
            if (!(e.getCause() instanceof TopicExistsException)) {
                throw new RuntimeException(e);
            }
        }
    }

    @SubscribeEvent
    public void onChat(net.minecraftforge.event.ServerChatEvent event){

        ServerPlayer player = event.getPlayer();
        String message = event.getMessage().getString();
        String username = event.getUsername();

        LOGGER.info("Player : " + player + " - Message : " + message + " - Username : " + username);

        KafkaModPlayer kmPlayer = new KafkaModPlayer(player);
        ChatRecord cr = new ChatRecord(message, username, kmPlayer);
        String key = username;
        JsonNode record = objectMapper.valueToTree(cr);

        addRecordToTopic(key, record, KAFKA_MOD_CHAT);

    }

    @SubscribeEvent
    public void onPlayerChangedDimensionEvent(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        KafkaModPlayer pl = new KafkaModPlayer(player);
        LOGGER.info("ChangedDimension - " + pl);

        playerInGame.values().stream().forEach( p -> {
            p.displayClientMessage(Component.literal("ChangedDimension of player - " + pl), true);
        });
    }

    @SubscribeEvent
    public void onPlayerRespawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        KafkaModPlayer pl = new KafkaModPlayer(player);
        LOGGER.info("Respawn - " + pl);

        playerInGame.values().stream().forEach( p -> {
            p.displayClientMessage(Component.literal("Respawn of player - " + pl), true);
        });
    }

    @SubscribeEvent
    public void onItemPickupEvent(PlayerEvent.ItemPickupEvent event) {
        Player player = event.getEntity();
        KafkaModPlayer kmPlayer = new KafkaModPlayer(player);

        ItemStack stack = event.getStack();
        LOGGER.info("Component Pickup : " + stack.getDisplayName().getString());

        ItemStackRecord cr = new ItemStackRecord(kmPlayer, "Pickup", stack);
        String key = kmPlayer.getName();
        JsonNode record = objectMapper.valueToTree(cr);

        addRecordToTopic(key, record, KAFKA_MOD_ITEM_STACK);

    }

    @SubscribeEvent
    public void onItemCraftedEvent(PlayerEvent.ItemCraftedEvent event) {
        Player player = event.getEntity();
        KafkaModPlayer kmPlayer = new KafkaModPlayer(player);

        ItemStack stack = event.getCrafting();
        LOGGER.info("Component Crafted : " + stack.getDisplayName().getString());

        ItemStackRecord cr = new ItemStackRecord(kmPlayer, "Crafted", stack);
        String key = kmPlayer.getName();
        JsonNode record = objectMapper.valueToTree(cr);

        addRecordToTopic(key, record, KAFKA_MOD_ITEM_STACK);

    }

    private void addRecordToTopic(String key, JsonNode record, String topic){
        System.out.printf("Producing record: %s\t%s%n", key, record);
        producer.send(new ProducerRecord<String, JsonNode>(topic, key, record), new Callback() {
            @Override
            public void onCompletion(RecordMetadata m, Exception e) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    System.out.printf("Produced record to topic %s partition [%d] @ offset %d%n",
                        m.topic(),
                        m.partition(),
                        m.offset());
                }
            }
        });
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        String stringUUID = player.getStringUUID();
        playerInGame.put(stringUUID, player);

        LOGGER.info(KafkaMod.class.getSimpleName() + " - Client connected: " + player);

    }

    Map<String, Player> playerInGame = new ConcurrentHashMap<String, Player>();

    @SubscribeEvent
    public void onPlayerLoginOut(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        String stringUUID = player.getStringUUID();
        playerInGame.remove(stringUUID);

        LOGGER.info(KafkaMod.class.getSimpleName() + " - Client disconnected: " + player);
    }

}
