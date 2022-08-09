package dk.jarry.kafkamod;

import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonSerializer implements Serializer<JsonNode> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonSerializer() {
        // Nothing to do
    }

    @Override
    public void configure(Map<String, ?> config, boolean isKey) {
        // Nothing to Configure
    }

    @Override
    public byte[] serialize(String topic, JsonNode data) {
        if (data == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error serializing JSON message", e);
        }
    }

    @Override
    public void close() {
    }

}