package dk.jarry.kafkamod;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class KafkaModPlayer {

    Player player;

    public KafkaModPlayer(ServerPlayer player){
        this.player = player;
    }

    public KafkaModPlayer(Player player){
        this.player = player;
    }

    public double getX(){
        return player.getX();
    }

    public double getY(){
        return player.getY();
    }

    public double getZ(){
        return player.getZ();
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
