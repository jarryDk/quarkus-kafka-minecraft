package dk.jarry.kafkamod;

public class ChatRecord {

    KafkaModPlayer player;
    String message;

    public ChatRecord(String message, String username) {
        this.message = message;
    }

    public ChatRecord(KafkaModPlayer player, String message) {
        this.player = player;
        this.message = message;
    }

    public KafkaModPlayer getPlayer() {
        return player;
    }

    public String getMessage() {
        return message;
    }

}
