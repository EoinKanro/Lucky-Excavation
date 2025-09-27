package io.github.eoinkanro.mc.luckyexcavation;

import io.github.eoinkanro.mc.luckyexcavation.conf.Config;
import io.github.eoinkanro.mc.luckyexcavation.conf.ConfigClothScreen;
import io.github.eoinkanro.mc.luckyexcavation.conf.Constants;
import io.github.eoinkanro.mc.luckyexcavation.handler.ForgeExcavationEventHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Constants.MOD_ID)
public class LuckyExcavation {

    private final Config config;
    private ConfigClothScreen clothScreen;

    public LuckyExcavation() {
        config = new Config(FMLPaths.CONFIGDIR.get());

        // Register the onLoadComplete method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onLoadComplete);

        //Config Menu
        if (FMLEnvironment.dist == Dist.CLIENT) {
            clothScreen = new ConfigClothScreen(config);

            ModLoadingContext.get().registerExtensionPoint(
                net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory(
                    (mc, parent) -> clothScreen.createScreen(parent)
                )
            );
        }
    }

    private void onLoadComplete(final FMLLoadCompleteEvent event) {
        Constants.LOG.info("Lucky Excavation loading...");
        config.reload();

        // Register gameplay-related handlers
        MinecraftForge.EVENT_BUS.register(new ForgeExcavationEventHandler(config));
    }
}