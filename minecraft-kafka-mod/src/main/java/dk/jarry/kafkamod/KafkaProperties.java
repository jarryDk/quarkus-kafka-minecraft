package dk.jarry.kafkamod;

import java.util.Properties;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaProperties extends Properties {

    public final static String KAFKA_BOOTSTRAP_SERVERS = "localhost:9092";

    public final static String KAFKA_MOD_CHAT           = "kafka-mod-chat";
    public final static String KAFKA_MOD_ITEM_STACK     = "kafka-mod-item-stack";
    public final static String KAFKA_MOD_ENTITY_EVENT   = "kafka-mod-entity-event";
    public final static String KAFKA_MOD_PLAYER_EVENT   = "kafka-mod-player-event";

    public KafkaProperties(){
        put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.KAFKA_BOOTSTRAP_SERVERS);
        put(ProducerConfig.ACKS_CONFIG, "all");
        put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
    }
}
