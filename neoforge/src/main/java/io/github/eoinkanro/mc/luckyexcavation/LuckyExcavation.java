package io.github.eoinkanro.mc.luckyexcavation;


import static io.github.eoinkanro.mc.luckyexcavation.conf.Constants.LOG;

import io.github.eoinkanro.mc.luckyexcavation.conf.Config;
import io.github.eoinkanro.mc.luckyexcavation.conf.ConfigClothScreen;
import io.github.eoinkanro.mc.luckyexcavation.conf.Constants;
import io.github.eoinkanro.mc.luckyexcavation.conf.NeoConfigLoader;
import io.github.eoinkanro.mc.luckyexcavation.handler.NeoExcavationEventHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Constants.MOD_ID)
public class LuckyExcavation {

    private NeoConfigLoader configLoader;

    public LuckyExcavation(IEventBus eventBus, ModContainer modContainer) {
        configLoader = new NeoConfigLoader();

        // Register the onLoadComplete method for modloading
        eventBus.addListener(this::onLoadComplete);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, configLoader.SPEC);
        if (FMLEnvironment.dist.isClient()) {
            modContainer.registerExtensionPoint(
                IConfigScreenFactory.class,
                (minecraft, parent) -> ConfigClothScreen.createScreen(parent)
            );
        }

    }

    private void onLoadComplete(FMLLoadCompleteEvent event) {
        LOG.info("Lucky Excavation loading...");
        Config.init(configLoader);

        NeoForge.EVENT_BUS.register(NeoExcavationEventHandler.class);
    }

}