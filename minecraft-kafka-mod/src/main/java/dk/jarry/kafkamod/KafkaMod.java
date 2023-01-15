package dk.jarry.kafkamod;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
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

    public static Map<String, Player> playerInGame = new ConcurrentHashMap<String, Player>();
    public final static ObjectMapper objectMapper = new ObjectMapper();
    private static Producer<String, JsonNode> producer;
    private static int producerConnectionAttempts = 0;

    public KafkaMod() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        // Register KafkaModPlayerEventSubscriper for PlayerEvent we are interested in
        KafkaModPlayerEventSubscriper.register();
        // Register KafkaModEntityEventSubscriper for EntityEvent we are interested in
        KafkaModEntityEventSubscriper.register();
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info(KafkaMod.class.getSimpleName() + " - Server starting - Spin up coneection to Kafka");
    }

    @SubscribeEvent
    public void onServerStoppedEvent(ServerStoppedEvent event) {
        LOGGER.info(KafkaMod.class.getSimpleName() + " - Server stopping - Graceful close connection to Kafka");
        producer.close();
    }

    @SubscribeEvent
    public void onPlayerDestroyItemEvent(PlayerDestroyItemEvent event) {
        Player player = event.getEntity();
        KafkaModPlayer kmPlayer = new KafkaModPlayer(player);
        ItemStack stack = event.getOriginal();
        LOGGER.info("Component Destroy : " + stack.getDisplayName().getString() + " " + kmPlayer);
    }

    @SubscribeEvent
    public void onChat(net.minecraftforge.event.ServerChatEvent event) {

        ServerPlayer player = event.getPlayer();
        String message = event.getMessage().getString();

        KafkaModPlayer kmPlayer = new KafkaModPlayer(player);
        ChatRecord cr = new ChatRecord(kmPlayer, message);

        String key = kmPlayer.getName();
        JsonNode record = objectMapper.valueToTree(cr);

        addRecordToTopic(key, record, KafkaProperties.KAFKA_MOD_CHAT);

        LOGGER.info("Player : " + player + " - Message : " + message);

    }

    static void createTopic(final String topic, final Properties config) {
        final NewTopic newTopic = new NewTopic(topic, Optional.empty(), Optional.empty());
        try (final AdminClient adminClient = AdminClient.create(config)) {
            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
        } catch (final InterruptedException | ExecutionException e) {
            if (!(e.getCause() instanceof TopicExistsException)) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void addRecordToTopic(String key, JsonNode record, String topic) {
        LOGGER.debug("Producing record: %s\t%s%n", key, record);

        getProducer().ifPresent(
                producer -> {
                    producer.send(new ProducerRecord<String, JsonNode>(topic, key, record), new Callback() {
                        @Override
                        public void onCompletion(RecordMetadata m, Exception e) {
                            if (e != null) {
                                e.printStackTrace();
                            } else {
                                LOGGER.debug("Produced record to topic %s partition [%d] @ offset %d%n",
                                        m.topic(),
                                        m.partition(),
                                        m.offset());
                            }
                        }
                    });
                });

    }

    private static Optional<Producer<String, JsonNode>> getProducer() {
        if (producer == null) {
            LOGGER.warn("Someone did not start Kafka ...");
            if (producerConnectionAttempts % 10 == 0) {
                producerConnectionAttempts = 0;
                setProducer();
            }
            if (producer == null) {
                producerConnectionAttempts++;
                return Optional.empty();
            }
        }
        return Optional.of(producer);
    }

    private static void setProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.KAFKA_BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        createTopic(KafkaProperties.KAFKA_MOD_CHAT, props);
        createTopic(KafkaProperties.KAFKA_MOD_ITEM_STACK, props);
        createTopic(KafkaProperties.KAFKA_MOD_ENTITY_EVENT, props);
        createTopic(KafkaProperties.KAFKA_MOD_PLAYER_EVENT, props);
        producer = new KafkaProducer<String, JsonNode>(props);
    }

    public static void dropTnTByPlayer(Player player, int num) {
        KafkaModPlayer kafkaModPlayer = new KafkaModPlayer(player);

        if (num < 1) {
            throw new IllegalStateException("Num need to be bigger then 0 !");
        }

        Vec3 pos = getPositionInFrontOfPlayer(player, 3);
        Vec3 posTnT = getPositionInFrontOfPlayer(player, 10);
        Level world = player.getCommandSenderWorld();
        Stream.iterate(0, i -> i).limit(num).forEach(i -> {

            PrimedTnt primedTnt = EntityType.TNT.create(world);
            primedTnt.setPos(posTnT);
            primedTnt.setInvisible(Boolean.TRUE);
            world.addFreshEntity(primedTnt);

            // PrimedTnt newEntity = EntityType.TNT.create(world);
            // MinecartTNT newEntity = EntityType.TNT_MINECART.create(world);
            Llama newEntity = EntityType.LLAMA.create(world);
            newEntity.setPos(pos);
            newEntity.setInvisible(Boolean.TRUE);
            world.addFreshEntity(newEntity);

            ItemStack newItem = new ItemStack(Items.TNT);
            player.getInventory().add(newItem);

            LOGGER.info(newEntity.getName() + " was added x:" + pos.x() + ", y:" + pos.y() + ", z:" + pos.z());
            LOGGER.info(kafkaModPlayer.toString());
        });

        Stream.iterate(0, i -> i).limit(10).forEach(i -> {
            ItemStack newItem = new ItemStack(Items.FIRE_CHARGE);
            player.getInventory().add(newItem);
        });

    }

    private static Vec3 getPositionInFrontOfPlayer(Player player, int distance) {
        double x = player.getX() + distance * player.getLookAngle().x;
        double y = player.getY() + distance * player.getLookAngle().y;
        double z = player.getZ() + distance * player.getLookAngle().z;
        Vec3 pos = new Vec3(x, y, z);
        return pos;
    }

}
