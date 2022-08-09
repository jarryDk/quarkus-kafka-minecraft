package dk.jarry.kafkamod;

public class ChatRecord {

    String message;
    String username;
    KafkaModPlayer player;

    public ChatRecord(String message, String username){
        this.message = message;
        this.username = username;
    }

    public ChatRecord(String message, String username, KafkaModPlayer player){
        this.message = message;
        this.username = username;
        this.player = player;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    public KafkaModPlayer getPlayer() {
        return player;
    }

}
