package dk.jarry.kafkamod.control;

import java.util.stream.Stream;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import dk.jarry.kafkamod.entity.KafkaModPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

import net.minecraft.world.level.Level;


public class Handout {

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void dropTnTByPlayer(Player player, int num) {
        KafkaModPlayer kafkaModPlayer = new KafkaModPlayer(player);

        if (num < 1) {
            throw new IllegalStateException("Num need to be bigger then 0 !");
        }

        Vec3 pos = PositionInFrontOfPlayer.getPosition(player, 3);
        Vec3 posTnT = PositionInFrontOfPlayer.getPosition(player, 10);
        Level world = player.getCommandSenderWorld();
        Stream.iterate(0, i -> i).limit(num).forEach(i -> {

            PrimedTnt primedTnt = EntityType.TNT.create(world);
            primedTnt.setPos(posTnT);
            primedTnt.setInvisible(Boolean.TRUE);
            world.addFreshEntity(primedTnt);

            // PrimedTnt newEntity = EntityType.TNT.create(world);
            // MinecartTNT newEntity = EntityType.TNT_MINECART.create(world);
            Llama newEntity = EntityType.LLAMA.create(world);
            newEntity.setPos(pos);
            newEntity.setInvisible(Boolean.TRUE);
            world.addFreshEntity(newEntity);

            ItemStack newItem = new ItemStack(Items.TNT);
            player.getInventory().add(newItem);

            LOGGER.info(newEntity.getName() + " was added x:" + pos.x() + ", y:" + pos.y() + ", z:" + pos.z());
            LOGGER.info(kafkaModPlayer.toString());
        });

        Stream.iterate(0, i -> i).limit(10).forEach(i -> {
            ItemStack newItem = new ItemStack(Items.FIRE_CHARGE);
            player.getInventory().add(newItem);
        });

    }

}
