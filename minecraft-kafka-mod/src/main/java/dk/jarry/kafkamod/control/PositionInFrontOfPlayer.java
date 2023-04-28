package dk.jarry.kafkamod.control;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class PositionInFrontOfPlayer {

    /**
     *
     * @param player
     * @param distance
     * @return Vec3
     */
    public static Vec3 getPosition(Player player, int distance) {
        double x = player.getX() + distance * player.getLookAngle().x;
        double y = player.getY() + distance * player.getLookAngle().y;
        double z = player.getZ() + distance * player.getLookAngle().z;
        Vec3 pos = new Vec3(x, y, z);
        return pos;
    }

}
