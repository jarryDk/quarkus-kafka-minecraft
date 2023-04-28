package dk.jarry.kafkamod.control;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import dk.jarry.kafkamod.entity.KafkaModPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
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
        LOGGER.info(eventSubscriber.getClass().getSimpleName() + " add to The core Forge EventBusses");
    }

    @SubscribeEvent
    public void onPlayerDestroyItemEvent(PlayerDestroyItemEvent event) {
        Player player = event.getEntity();
        KafkaModPlayer kmPlayer = new KafkaModPlayer(player);
        ItemStack stack = event.getOriginal();
        LOGGER.info("Component Destroy : " + stack.getDisplayName().getString() + " " + kmPlayer);
    }

}
