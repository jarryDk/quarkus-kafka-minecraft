package dk.jarry.kafkamod.control;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import dk.jarry.kafkamod.KafkaMod;
import dk.jarry.kafkamod.entity.KafkaModPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerSubscriber {

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    private PlayerSubscriber() {
    }

    public static void register() {
        PlayerSubscriber eventSubscriber = new PlayerSubscriber();
        MinecraftForge.EVENT_BUS.register(eventSubscriber);
        LOGGER.info("{} add to The core Forge EventBusses", //
            eventSubscriber.getClass().getSimpleName());
    }

    @SubscribeEvent
    public void onPlayerDestroyItemEvent(PlayerDestroyItemEvent event) {
        Player player = event.getEntity();
        KafkaModPlayer kmPlayer = new KafkaModPlayer(player);
        ItemStack stack = event.getOriginal();
        LOGGER.info("Component Destroy : {} {}", //
                stack.getDisplayName().getString(), kmPlayer);
    }

    @SubscribeEvent
    public void onLivingDeathEvent(LivingDeathEvent event){
        LivingEntity entity = event.getEntity();
        if(entity instanceof Player){
            Player player = (Player) entity;
            KafkaModPlayer kmPlayer = new KafkaModPlayer(player);
            LOGGER.info("Player is death : {}", kmPlayer);

            // player.sendSystemMessage(
            //    Component.literal("<"+kmPlayer.getName()+"> I died - x=" + player.getX() + " y=" + player.getY() + " z=" + player.getZ()));

            KafkaMod.playerInGame.values().stream().forEach(p -> {
                p.displayClientMessage(Component.literal("Death of player - " + player), true);
            });

        }
    }
}
