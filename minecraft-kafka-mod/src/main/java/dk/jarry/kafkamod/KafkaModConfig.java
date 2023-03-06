package dk.jarry.kafkamod;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class KafkaModConfig {

    private static final String KAFKA_MOD_PLAYER_EVENT_ENABLE = "kafka-mod-player-event-enable";
    private static final String KAFKA_MOD_ENTITY_EVENT_ENABLE = "kafka-mod-entity-event-enable";
    private static final String KAFKA_MOD_TOPIC_NUM_PARTITIONS = "kafka-mod-topic-num-partitions";
    private static final String KAFKA_MOD_TOPIC_REPLICATION_FACTOR = "kafka-mod-topic-replication-factor";

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
            e.printStackTrace();
        }
    }

    public Boolean isPlayerEventEnabled() {
        if (!serverProperties.containsKey(KAFKA_MOD_PLAYER_EVENT_ENABLE)) {
            return true;
        } else {
            return Boolean.parseBoolean(serverProperties.get(KAFKA_MOD_PLAYER_EVENT_ENABLE).toString());
        }
    }

    public Boolean isEntityEventEnabled() {
        if (!serverProperties.containsKey(KAFKA_MOD_ENTITY_EVENT_ENABLE)) {
            return true;
        } else {
            return Boolean.parseBoolean(serverProperties.get(KAFKA_MOD_ENTITY_EVENT_ENABLE).toString());
        }
    }

    public int getTopicNumPartitions() {
        Integer numPartitions = 1;
        if (serverProperties.contains(KAFKA_MOD_TOPIC_NUM_PARTITIONS)) {
            try {
                numPartitions = Integer.parseInt(serverProperties.getProperty(KAFKA_MOD_TOPIC_NUM_PARTITIONS));
            } catch (Exception e) {
            }
        }
        return numPartitions;
    }

    public Short getTopicReplicationFactor() {
        Short replicationFactor = 1;
        if (serverProperties.contains(KAFKA_MOD_TOPIC_REPLICATION_FACTOR)) {
            try {
                replicationFactor = Short.parseShort(serverProperties.getProperty(KAFKA_MOD_TOPIC_REPLICATION_FACTOR));
            } catch (Exception e) {
            }
        }
        return replicationFactor;
    }
}
