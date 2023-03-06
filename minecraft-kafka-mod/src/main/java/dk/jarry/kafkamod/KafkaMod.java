package dk.jarry.kafkamod;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.TopicExistsException;
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mojang.logging.LogUtils;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(KafkaMod.MOD_NAME)
public class KafkaMod {

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    static final String MOD_NAME = "kafkamod";

    KafkaModConfig kafkaModConfig = new KafkaModConfig();
    private static KafkaProperties kafkaProperties = new KafkaProperties();

    public static Map<String, Player> playerInGame = new ConcurrentHashMap<String, Player>();
    public final static ObjectMapper objectMapper = new ObjectMapper();
    static Producer<String, JsonNode> producer;
    private static int producerConnectionAttempts = 0;

    public KafkaMod() {

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        if (kafkaModConfig.isPlayerEventEnabled()) {
            createTopic(KafkaProperties.KAFKA_MOD_PLAYER_EVENT);
            createTopic(KafkaProperties.KAFKA_MOD_ITEM_STACK);
            // Register KafkaModPlayerEventSubscriper for PlayerEvent we are interested in
            KafkaModPlayerEventSubscriper.register();
        }

        if (kafkaModConfig.isEntityEventEnabled()) {
            createTopic(KafkaProperties.KAFKA_MOD_ENTITY_EVENT);
            // Register KafkaModEntityEventSubscriper for EntityEvent we are interested in
            KafkaModEntityEventSubscriper.register();
        }

        KafkaModServerSubscriper.register();
        KafkaModPlayerSubscriper.register();
        createTopic(KafkaProperties.KAFKA_MOD_CHAT);
        KafkaModEventSubscriper.register();

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
        LOGGER.info("New KafkaProducer in the making");
        producer = new KafkaProducer<String, JsonNode>(kafkaProperties);
    }

    private void createTopic(final String topic) {
        LOGGER.info("Creating topic if needed - " + topic + " - numPartitions:" + kafkaModConfig.getTopicNumPartitions()
                + ", replicationFactor:" + kafkaModConfig.getTopicReplicationFactor());
        Optional<Integer> numPartitions = Optional.of(kafkaModConfig.getTopicNumPartitions()); // Default is 1
        Optional<Short> replicationFactor = Optional.of(kafkaModConfig.getTopicReplicationFactor()); // Default is 1
        final NewTopic newTopic = new NewTopic(topic, numPartitions, replicationFactor);
        // Map<String,String> conf = new HashMap<>();
        // conf.putIfAbsent("min.insync.replicas", "2");
        // newTopic.configs(conf);
        try (final AdminClient adminClient = AdminClient.create(kafkaProperties)) {
            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
        } catch (final InterruptedException | ExecutionException e) {
            if (!(e.getCause() instanceof TopicExistsException)) {
                throw new RuntimeException(e);
            }
        }
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
