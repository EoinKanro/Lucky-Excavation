package io.github.eoinkanro.mc.luckyexcavation;

import io.github.eoinkanro.mc.luckyexcavation.conf.Config;
import io.github.eoinkanro.mc.luckyexcavation.handler.ExcavationEventHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(LuckyExcavation.MODID)
public class LuckyExcavation {

    public static final String MODID = "luckyexcavation";
    private static final Logger LOGGER = LogManager.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public LuckyExcavation(IEventBus modEventBus, ModContainer modContainer) {
        // Register the onLoadComplete method for modloading
        modEventBus.addListener(this::onLoadComplete);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    private void onLoadComplete(FMLLoadCompleteEvent event) {
        LOGGER.info("Lucky Excavation loading...");
        Config.loadConfig();

        NeoForge.EVENT_BUS.register(ExcavationEventHandler.class);
    }


}
