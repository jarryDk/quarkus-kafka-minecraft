package dk.jarry.kafkamod;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KafkaModPlayerSubscriper {

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    private KafkaModPlayerSubscriper() {
    }

    public static void register() {
        KafkaModPlayerSubscriper eventSubscriper = new KafkaModPlayerSubscriper();
        MinecraftForge.EVENT_BUS.register(eventSubscriper);
        LOGGER.info(eventSubscriper.getClass().getSimpleName() + " add to The core Forge EventBusses");
    }

    @SubscribeEvent
    public void onPlayerDestroyItemEvent(PlayerDestroyItemEvent event) {
        Player player = event.getEntity();
        KafkaModPlayer kmPlayer = new KafkaModPlayer(player);
        ItemStack stack = event.getOriginal();
        LOGGER.info("Component Destroy : " + stack.getDisplayName().getString() + " " + kmPlayer);
    }

}
