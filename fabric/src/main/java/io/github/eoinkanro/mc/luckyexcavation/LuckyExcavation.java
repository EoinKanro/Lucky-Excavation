package io.github.eoinkanro.mc.luckyexcavation;

import static io.github.eoinkanro.mc.luckyexcavation.conf.Constants.LOG;

import io.github.eoinkanro.mc.luckyexcavation.conf.Config;
import io.github.eoinkanro.mc.luckyexcavation.handler.FabricExcavationEventHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;

public class LuckyExcavation implements ModInitializer {

    public static Config CONFIG;
    
    @Override
    public void onInitialize() {
        CONFIG = new Config(FabricLoader.getInstance().getConfigDir());

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> loadConfig());
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            loadConfig();
            new FabricExcavationEventHandler(CONFIG).register();
        });
    }

    private void loadConfig() {
        LOG.info("Lucky Excavation loading...");
        CONFIG.reload();
    }
}
