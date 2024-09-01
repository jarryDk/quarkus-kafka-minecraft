package dk.jarry.kafkamod.entity;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;

abstract class Location {

    double x;
    double y;
    double z;

    public Location(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location(EntityEvent event) {
        Entity entity = event.getEntity();
        this.x = entity.getX();
        this.y = entity.getY();
        this.z = entity.getZ();
    }

    public Location(ServerPlayer player){
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

}
