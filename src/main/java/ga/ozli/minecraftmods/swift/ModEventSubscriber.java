package ga.ozli.minecraftmods.swift;

import ga.ozli.minecraftmods.swift.config.ConfigHolder;
import ga.ozli.minecraftmods.swift.config.SwiftConfig;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = Swift.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEventSubscriber {

    private static final Logger LOGGER = LogManager.getLogger(Swift.MODID + " Mod Event Subscriber");

    /**
     * This method will be called by Forge when a config changes.
     */
    @SubscribeEvent
    public static void onModConfigEvent(final ModConfig.ModConfigEvent event) {
        final ModConfig config = event.getConfig();
        // Rebake the configs when they change
        if (config.getSpec() == ConfigHolder.CLIENT_SPEC) {
            SwiftConfig.bakeClient(config);
            LOGGER.debug("Baked client config");
        }
    }
}