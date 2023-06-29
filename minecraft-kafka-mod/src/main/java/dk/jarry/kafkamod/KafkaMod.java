package dk.jarry.kafkamod;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
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
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mojang.logging.LogUtils;

import dk.jarry.kafkamod.control.EntityEventSubscriber;
import dk.jarry.kafkamod.control.EventServerChatEventSubscriber;
import dk.jarry.kafkamod.control.PlayerEventSubscriber;
import dk.jarry.kafkamod.control.PlayerSubscriber;
import dk.jarry.kafkamod.control.ServerSubscriber;
import net.minecraft.world.entity.player.Player;
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
            // Register PlayerEventSubscriber for PlayerEvent we are interested in
            PlayerEventSubscriber.register();
        }

        if (kafkaModConfig.isEntityEventEnabled()) {
            createTopic(KafkaProperties.KAFKA_MOD_ENTITY_EVENT);
            // Register EntityEventSubscriber for EntityEvent we are interested in
            EntityEventSubscriber.register();
        }

        if (kafkaModConfig.isServerChatEventEnabled()) {
            createTopic(KafkaProperties.KAFKA_MOD_CHAT);
            // Register EventServerChatEventSubscriber for Server chat we are interested in
            EventServerChatEventSubscriber.register();
        }

        // default is localhost:9092
        kafkaProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaModConfig.getBrokers());

        ServerSubscriber.register();
        PlayerSubscriber.register();

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

    public static Optional<Producer<String, JsonNode>> getProducer() {
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
        try (final AdminClient adminClient = AdminClient.create(kafkaProperties)) {
            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
        } catch (final InterruptedException | ExecutionException e) {
            if (!(e.getCause() instanceof TopicExistsException)) {
                throw new RuntimeException(e);
            }
        }
    }

}
