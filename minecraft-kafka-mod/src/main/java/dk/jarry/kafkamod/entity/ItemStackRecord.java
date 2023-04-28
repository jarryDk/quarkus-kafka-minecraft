package dk.jarry.kafkamod.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;

import dk.jarry.kafkamod.KafkaMod;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

@JsonPropertyOrder(alphabetic = true)
public class ItemStackRecord {

    KafkaModPlayer player;
    String typeOfEvent;
    ItemStack stack;

    public ItemStackRecord(KafkaModPlayer player, String typeOfEvent, ItemStack stack){
        this.player = player;
        this.typeOfEvent = typeOfEvent;
        this.stack = stack;
    }

    public KafkaModPlayer getPlayer() {
        return player;
    }

    public String getTypeOfEvent() {
        return typeOfEvent;
    }

    public String getDisplayName(){
        Component component = stack.getDisplayName();
        String regex = "[!^\\[\\]]";
        return component.getString().replaceAll(regex, "");
    }

    public JsonNode toJsonNode() {
        return KafkaMod.objectMapper.valueToTree(this);
    }

}
