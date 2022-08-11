package dk.jarry.kafkamod;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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

}
