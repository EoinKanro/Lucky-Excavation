package io.github.eoinkanro.mc.luckyexcavation.conf;

import static io.github.eoinkanro.mc.luckyexcavation.conf.Config.DROP_CHANCE;
import static io.github.eoinkanro.mc.luckyexcavation.conf.Config.DROP_CHANCE_MULTIPLAYER;
import static io.github.eoinkanro.mc.luckyexcavation.conf.Config.DROP_ENABLE_IN_CREATIVE;
import static io.github.eoinkanro.mc.luckyexcavation.conf.Config.DROP_NAMES;
import static io.github.eoinkanro.mc.luckyexcavation.conf.Config.EXCAVATION_BLOCK_NAMES;
import static io.github.eoinkanro.mc.luckyexcavation.conf.Config.EXCAVATION_BLOCK_TAGS;
import static io.github.eoinkanro.mc.luckyexcavation.conf.Config.LUCKY_MESSAGE;
import static io.github.eoinkanro.mc.luckyexcavation.conf.Config.LUCKY_MESSAGE_ENABLE;
import static io.github.eoinkanro.mc.luckyexcavation.conf.Config.configLoader;

import io.github.eoinkanro.mc.kanrommon.utils.ResourceUtils;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * In-game config screen of mod for client
 */
public class ConfigClothScreen {

  public static Screen createScreen(Screen parent) {
    ConfigBuilder builder = ConfigBuilder.create()
        .setParentScreen(parent)
        .setTitle(Component.literal("Lucky Excavation Config"))
        .setSavingRunnable(() -> {
          try {
            validateConfig();
            configLoader.save();
            configLoader.load();
          } catch (Exception e) {
            Minecraft.getInstance().execute(() -> {
              ToastComponent toast = Minecraft.getInstance().getToasts();
              SystemToast.add(
                  toast,
                  SystemToast.SystemToastId.PACK_LOAD_FAILURE,
                  Component.literal("Config Error"),
                  Component.literal(e.getMessage())
              );
            });
          }
        });

    ConfigEntryBuilder entryBuilder = builder.entryBuilder();
    ConfigCategory general = builder.getOrCreateCategory(Component.literal("General"));

    // Add standard option
    general.addEntry(entryBuilder.startIntField(
            Component.literal(DROP_CHANCE.getName()),
            DROP_CHANCE.getCurrentValue()
        ).setDefaultValue(DROP_CHANCE.getDefaultValue())
        .setTooltip(Component.literal(DROP_CHANCE.getDescription()))
        .setSaveConsumer(DROP_CHANCE::setCurrentValue)
        .setMin(DROP_CHANCE.getMinValue())
        .setMax(DROP_CHANCE.getMaxValue())
        .build());

    general.addEntry(entryBuilder.startIntField(
            Component.literal(DROP_CHANCE_MULTIPLAYER.getName()),
            DROP_CHANCE_MULTIPLAYER.getCurrentValue()
        ).setDefaultValue(DROP_CHANCE_MULTIPLAYER.getDefaultValue())
        .setTooltip(Component.literal(DROP_CHANCE_MULTIPLAYER.getDescription()))
        .setSaveConsumer(DROP_CHANCE_MULTIPLAYER::setCurrentValue)
        .setMin(DROP_CHANCE_MULTIPLAYER.getMinValue())
        .setMax(DROP_CHANCE_MULTIPLAYER.getMaxValue())
        .build());

    general.addEntry(entryBuilder.startBooleanToggle(
            Component.literal(DROP_ENABLE_IN_CREATIVE.getName()),
            DROP_ENABLE_IN_CREATIVE.getCurrentValue()
        ).setDefaultValue(DROP_ENABLE_IN_CREATIVE.getDefaultValue())
        .setTooltip(Component.literal(DROP_ENABLE_IN_CREATIVE.getDescription()))
        .setSaveConsumer(DROP_ENABLE_IN_CREATIVE::setCurrentValue)
        .build());

    general.addEntry(entryBuilder.startStrList(
            Component.literal(EXCAVATION_BLOCK_TAGS.getName()),
            EXCAVATION_BLOCK_TAGS.getCurrentValue()
        ).setDefaultValue(EXCAVATION_BLOCK_TAGS.getDefaultValue())
        .setTooltip(Component.literal(EXCAVATION_BLOCK_TAGS.getDescription()))
        .setSaveConsumer(EXCAVATION_BLOCK_TAGS::setCurrentValue)
        .setInsertButtonEnabled(true)
        .build());

    general.addEntry(entryBuilder.startStrList(
            Component.literal(EXCAVATION_BLOCK_NAMES.getName()),
            EXCAVATION_BLOCK_NAMES.getCurrentValue()
        ).setDefaultValue(EXCAVATION_BLOCK_NAMES.getDefaultValue())
        .setTooltip(Component.literal(EXCAVATION_BLOCK_NAMES.getDescription()))
        .setSaveConsumer(EXCAVATION_BLOCK_NAMES::setCurrentValue)
        .setInsertButtonEnabled(true)
        .build());

    general.addEntry(entryBuilder.startStrList(
            Component.literal(DROP_NAMES.getName()),
            DROP_NAMES.getCurrentValue()
        ).setDefaultValue(DROP_NAMES.getDefaultValue())
        .setTooltip(Component.literal(DROP_NAMES.getDescription()))
        .setSaveConsumer(DROP_NAMES::setCurrentValue)
        .setInsertButtonEnabled(true)
        .build());

    general.addEntry(entryBuilder.startStrField(
            Component.literal(LUCKY_MESSAGE.getName()),
            LUCKY_MESSAGE.getCurrentValue()
        ).setDefaultValue(LUCKY_MESSAGE.getDefaultValue())
        .setTooltip(Component.literal(LUCKY_MESSAGE.getDescription()))
        .setSaveConsumer(LUCKY_MESSAGE::setCurrentValue)
        .build());

    general.addEntry(entryBuilder.startBooleanToggle(
            Component.literal(LUCKY_MESSAGE_ENABLE.getName()),
            LUCKY_MESSAGE_ENABLE.getCurrentValue()
        ).setDefaultValue(LUCKY_MESSAGE_ENABLE.getDefaultValue())
        .setTooltip(Component.literal(LUCKY_MESSAGE_ENABLE.getDescription()))
        .setSaveConsumer(LUCKY_MESSAGE_ENABLE::setCurrentValue)
        .build());

    return builder.build();
  }

  private static void validateConfig() {
    //TODO check tags somehow
    parseLocations(EXCAVATION_BLOCK_TAGS.getCurrentValue());

    List<ResourceLocation> blockNames = parseLocations(EXCAVATION_BLOCK_NAMES.getCurrentValue());
    validateResourceLocations(BuiltInRegistries.BLOCK, blockNames);

    List<ResourceLocation> itemNames = parseLocations(DROP_NAMES.getCurrentValue());
    validateResourceLocations(BuiltInRegistries.ITEM, itemNames);
  }

  private static List<ResourceLocation> parseLocations(List<? extends String> locations) {
    return Optional.ofNullable(locations)
        .orElse(Collections.emptyList())
        .stream()
        .map(it -> {
          String[] namespace = it.split(ResourceUtils.NAMESPACE_SEPARATOR);
          if (namespace.length != 2) {
            throw new IllegalArgumentException("Wrong namespace " + it);
          }
          return ResourceLocation.fromNamespaceAndPath(namespace[0], namespace[1]);
        })
        .toList();
  }

  private static void validateResourceLocations(DefaultedRegistry<?> registry, List<ResourceLocation> locations) {
    for (ResourceLocation blockName : locations) {
      if (!registry.containsKey(blockName)) {
        throw new IllegalArgumentException("Can't find name: " + blockName);
      }
    }
  }

}
