package dk.jarry.kafkamod;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

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

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(KafkaMod.MOD_NAME)
public class KafkaMod {

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    final static String MOD_NAME = "kafkamod";
    final static String KAFKA_MOD_CHAT = "kafka-mod-chat";
    final static String KAFKA_MOD_ITEM_STACK = "kafka-mod-item-stack";
    final static String KAFKA_BOOTSTRAP_SERVERS = "localhost:9092";

    public static Map<String, Player> playerInGame = new ConcurrentHashMap<String, Player>();
    public final static ObjectMapper objectMapper = new ObjectMapper();
    private static Producer<String, JsonNode> producer;

    public KafkaMod() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        // Register KafkaModPlayerEventSubscriper for PlayerEvent we are interested in
        KafkaModPlayerEventSubscriper.register();
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

        LOGGER.info(KafkaMod.class.getSimpleName() + " - Server starting - Spin up Kafka");

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        createTopic(KAFKA_MOD_CHAT, props);
        createTopic(KAFKA_MOD_ITEM_STACK, props);
        producer = new KafkaProducer<String, JsonNode>(props);

    }

    @SubscribeEvent
    public void onServerStoppedEvent(ServerStoppedEvent event) {
        LOGGER.info(KafkaMod.class.getSimpleName() + " - Server stopping - Graceful cloase connection to Kafka");
        producer.close();
    }

    @SubscribeEvent
    public void onEntityJoinLevelEvent(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        if(entity != null){
            LOGGER.info("Entity - name:" + entity.getName() + " join the world (x:" + x + ",y:" + y + ",z:" + z + ")");
        }
    }

    @SubscribeEvent
    public void onEntityLeaveLevelEvent(EntityLeaveLevelEvent event) {
        Entity entity = event.getEntity();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        if(entity != null){
            LOGGER.info("Entity - name:" + entity.getName() + " leave the world (x:" + x + ",y:" + y + ",z:" + z + ")");
        }
    }

    @SubscribeEvent
    public void onPlayerDestroyItemEvent(PlayerDestroyItemEvent event) {
        Player player = event.getEntity();
        KafkaModPlayer kmPlayer = new KafkaModPlayer(player);
        ItemStack stack = event.getOriginal();
        LOGGER.info("Component Destroy : " + stack.getDisplayName().getString() + " " + kmPlayer);
    }

    @SubscribeEvent
    public void onChat(net.minecraftforge.event.ServerChatEvent event){

        ServerPlayer player = event.getPlayer();
        String message = event.getMessage().getString();

        KafkaModPlayer kmPlayer = new KafkaModPlayer(player);
        ChatRecord cr = new ChatRecord(kmPlayer,message);

        String key = kmPlayer.getName();
        JsonNode record = objectMapper.valueToTree(cr);

        addRecordToTopic(key, record, KAFKA_MOD_CHAT);

        LOGGER.info("Player : " + player + " - Message : " + message);

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

    public static void addRecordToTopic(String key, JsonNode record, String topic){
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

}
