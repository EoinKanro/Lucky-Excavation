package io.github.eoinkanro.mc.luckyexcavation;

import static io.github.eoinkanro.mc.luckyexcavation.conf.Constants.LOG;

import io.github.eoinkanro.mc.luckyexcavation.conf.Config;
import io.github.eoinkanro.mc.luckyexcavation.conf.ConfigClothScreen;
import io.github.eoinkanro.mc.luckyexcavation.conf.Constants;
import io.github.eoinkanro.mc.luckyexcavation.handler.NeoExcavationEventHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Constants.MOD_ID)
public class LuckyExcavation {

    private final Config config;
    private ConfigClothScreen clothScreen;

    public LuckyExcavation(IEventBus eventBus, ModContainer modContainer) {
        config = new Config(FMLPaths.CONFIGDIR.get());

        // Register the onLoadComplete method for modloading
        eventBus.addListener(this::onLoadComplete);

        //Config Menu
        if (FMLEnvironment.dist.isClient()) {
            clothScreen = new ConfigClothScreen(config);

            modContainer.registerExtensionPoint(
                IConfigScreenFactory.class,
                (minecraft, parent) -> clothScreen.createScreen(parent)
            );
        }
    }

    private void onLoadComplete(FMLLoadCompleteEvent event) {
        LOG.info("Lucky Excavation loading...");
        config.reload();
        config.save();

        NeoForge.EVENT_BUS.register(new NeoExcavationEventHandler(config));
    }

}