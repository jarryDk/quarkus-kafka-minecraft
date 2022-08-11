package dk.jarry.kafkamod;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

@JsonPropertyOrder(alphabetic = true)
public class KafkaModPlayer extends Location {

    Player player;

    public KafkaModPlayer(ServerPlayer player){
        super(player.getX(), player.getZ(), player.getZ());
        this.player = player;
    }

    public KafkaModPlayer(Player player){
        super(player.getX(), player.getZ(), player.getZ());
        this.player = player;
    }

    public String getIpAddress(){
        if( player instanceof ServerPlayer){
            return ((ServerPlayer)player).getIpAddress();
        }
        return "NN";
    }

    public String getName() {
        return player.getGameProfile().getName();
    }

    public String toString() {
        return "Player : " + getName() + " x=" + player.getX() + " y=" + player.getY() + " z=" + player.getZ();
    }

}
