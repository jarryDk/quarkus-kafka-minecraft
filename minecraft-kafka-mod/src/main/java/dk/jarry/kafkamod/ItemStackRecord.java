package dk.jarry.kafkamod;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ItemStackRecord {

    KafkaModPlayer player;
    String typeOfEvent;
    ItemStack stack;

    public ItemStackRecord(KafkaModPlayer player, String typeOfEvent, ItemStack stack){
        this.player = player;
        this.typeOfEvent = typeOfEvent;
        this.stack = stack;
    }

    public String getTypeOfEvent() {
        return typeOfEvent;
    }

    public String getDisplayName(){
        Component component = stack.getDisplayName();
        String regex = "[!^\\[\\]]";
        return component.getString().replaceAll(regex, "");

    }

    public KafkaModPlayer getPlayer() {
        return player;
    }

    public static void main(String[] args) {
        String reg = "[!^\\[\\]]";
        String test = "[Dirt]";
        System.out.println( test.replaceAll(reg, ""));
    }

}
