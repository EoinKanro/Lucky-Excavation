package io.github.eoinkanro.mc.luckyexcavation;

import io.github.eoinkanro.mc.luckyexcavation.conf.Config;
import io.github.eoinkanro.mc.luckyexcavation.conf.ConfigClothScreen;
import io.github.eoinkanro.mc.luckyexcavation.conf.Constants;
import io.github.eoinkanro.mc.luckyexcavation.conf.ForgeConfigLoader;
import io.github.eoinkanro.mc.luckyexcavation.handler.ForgeExcavationEventHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(Constants.MOD_ID)
public class LuckyExcavation {

    private final ForgeConfigLoader configLoader;

    public LuckyExcavation() {
        configLoader = new ForgeConfigLoader();

        // Register the onLoadComplete method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onLoadComplete);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, configLoader.SPEC);

        //Config Menu
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ModLoadingContext.get().registerExtensionPoint(
                net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory(
                    (mc, parent) -> ConfigClothScreen.createScreen(parent)
                )
            );
        }
    }

    private void onLoadComplete(final FMLLoadCompleteEvent event) {
        Constants.LOG.info("Lucky Excavation loading...");
        Config.init(configLoader);

        // Register gameplay-related handlers
        MinecraftForge.EVENT_BUS.register(ForgeExcavationEventHandler.class);
    }
}