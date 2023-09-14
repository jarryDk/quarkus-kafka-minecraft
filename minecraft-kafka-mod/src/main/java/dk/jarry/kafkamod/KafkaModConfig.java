package dk.jarry.kafkamod;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

public class KafkaModConfig {

     // Directly reference a slf4j logger
     private static final Logger LOGGER = LogUtils.getLogger();

    private static final String KAFKA_MOD_PLAYER_EVENT_ENABLE = "kafka-mod-player-event-enable";
    private static final String KAFKA_MOD_ENTITY_EVENT_ENABLE = "kafka-mod-entity-event-enable";
    private static final String KAFKA_MOD_SERVER_CHAT_EVENT_ENABLE = "kafka-mod-server-chat-event-enable";

    private static final String KAFKA_MOD_TOPIC_NUM_PARTITIONS = "kafka-mod-topic-num-partitions";
    private static final String KAFKA_MOD_TOPIC_REPLICATION_FACTOR = "kafka-mod-topic-replication-factor";

    private static final String KAFKA_MOD_KAFKA_BROKERS = "kafka-mod-kafka-brokers";

    private Properties serverProperties = new Properties();

    public KafkaModConfig() {
        // load server.properties into serverProperties
        loadServerProperties();
    }

    private void loadServerProperties() {
        // load server.properties into serverProperties
        try (InputStream input = new FileInputStream("server.properties")) {
            serverProperties.load(input);
        } catch (IOException e) {
            LOGGER.error("Not able to load server.properties .. strange !", e);
        }
        LOGGER.info("Config loaded from server.properties");
    }

    public Boolean isPlayerEventEnabled() {
        return Boolean.parseBoolean(
            serverProperties.getOrDefault(KAFKA_MOD_PLAYER_EVENT_ENABLE, "True").toString()
        );
    }

    public Boolean isEntityEventEnabled() {
        return Boolean.parseBoolean(
            serverProperties.getOrDefault(KAFKA_MOD_ENTITY_EVENT_ENABLE, "True").toString()
        );
    }

    public Boolean isServerChatEventEnabled() {
        return Boolean.parseBoolean(
            serverProperties.getOrDefault(KAFKA_MOD_SERVER_CHAT_EVENT_ENABLE, "True").toString()
        );
    }

    public int getTopicNumPartitions() {
        return Integer.parseInt( //
            serverProperties.getProperty(KAFKA_MOD_TOPIC_NUM_PARTITIONS, "1"));
    }

    public Short getTopicReplicationFactor() {
       return Short.parseShort( //
            serverProperties.getProperty(KAFKA_MOD_TOPIC_REPLICATION_FACTOR, "1"));
    }

    public String getBrokers() {
        return serverProperties.getProperty( //
            KAFKA_MOD_KAFKA_BROKERS,"localhost:9092");
    }
}
