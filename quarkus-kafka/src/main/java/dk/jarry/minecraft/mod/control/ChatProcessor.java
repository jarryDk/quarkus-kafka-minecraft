package dk.jarry.minecraft.mod.control;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dk.jarry.minecraft.mod.entity.Chat;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ChatProcessor {

    @Inject
    ObjectMapper objectMapper;

    @Incoming("kafka-mod-chat")
    @Outgoing("chats")
    public Chat process(String record) throws InterruptedException {

        Chat chat = null;
        try {
            JsonNode chatObj = objectMapper.readTree(record);
            chat = new Chat(chatObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chat;
    }

}
