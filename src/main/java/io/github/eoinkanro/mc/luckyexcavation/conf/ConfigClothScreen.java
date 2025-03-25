package io.github.eoinkanro.mc.luckyexcavation.conf;

import static io.github.eoinkanro.mc.luckyexcavation.conf.Config.DROP_CHANCE;
import static io.github.eoinkanro.mc.luckyexcavation.conf.Config.DROP_CHANCE_MULTIPLAYER;
import static io.github.eoinkanro.mc.luckyexcavation.conf.Config.DROP_ENABLE_IN_CREATIVE;
import static io.github.eoinkanro.mc.luckyexcavation.conf.Config.DROP_NAMES;
import static io.github.eoinkanro.mc.luckyexcavation.conf.Config.EXCAVATION_BLOCK_NAMES;
import static io.github.eoinkanro.mc.luckyexcavation.conf.Config.EXCAVATION_BLOCK_TAGS;
import static io.github.eoinkanro.mc.luckyexcavation.conf.Config.LUCKY_MESSAGE;
import static io.github.eoinkanro.mc.luckyexcavation.conf.Config.LUCKY_MESSAGE_ENABLE;

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

public class ConfigClothScreen {

  public static Screen createScreen(Screen parent) {
    ConfigBuilder builder = ConfigBuilder.create()
        .setParentScreen(parent)
        .setTitle(Component.literal("Lucky Excavation Config"))
        .setSavingRunnable(() -> {
          try {
            validateConfig();
            Config.SPEC.save();
            Config.loadConfig();
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
            Component.literal(DROP_CHANCE.getSpec().getTranslationKey()),
            DROP_CHANCE.get()
        ).setDefaultValue(DROP_CHANCE.getDefault())
        .setTooltip(Component.literal(DROP_CHANCE.getSpec().getComment()))
        .setSaveConsumer(DROP_CHANCE::set)
        .setMin((Integer) DROP_CHANCE.getSpec().getRange().getMin())
        .setMax((Integer) DROP_CHANCE.getSpec().getRange().getMax())
        .build());

    general.addEntry(entryBuilder.startIntField(
            Component.literal(DROP_CHANCE_MULTIPLAYER.getSpec().getTranslationKey()),
            DROP_CHANCE_MULTIPLAYER.get()
        ).setDefaultValue(DROP_CHANCE_MULTIPLAYER.getDefault())
        .setTooltip(Component.literal(DROP_CHANCE_MULTIPLAYER.getSpec().getComment()))
        .setSaveConsumer(DROP_CHANCE_MULTIPLAYER::set)
        .setMin((Integer) DROP_CHANCE_MULTIPLAYER.getSpec().getRange().getMin())
        .setMax((Integer) DROP_CHANCE_MULTIPLAYER.getSpec().getRange().getMax())
        .build());

    general.addEntry(entryBuilder.startBooleanToggle(
        Component.literal(DROP_ENABLE_IN_CREATIVE.getSpec().getTranslationKey()),
        DROP_ENABLE_IN_CREATIVE.get()
        ).setDefaultValue(DROP_ENABLE_IN_CREATIVE.getDefault())
        .setTooltip(Component.literal(DROP_ENABLE_IN_CREATIVE.getSpec().getComment()))
        .setSaveConsumer(DROP_ENABLE_IN_CREATIVE::set)
        .build());

    general.addEntry(entryBuilder.startStrList(
            Component.literal(EXCAVATION_BLOCK_TAGS.getSpec().getTranslationKey()),
            (List<String>) EXCAVATION_BLOCK_TAGS.get()
        ).setDefaultValue((List<String>) EXCAVATION_BLOCK_TAGS.getDefault())
        .setTooltip(Component.literal(EXCAVATION_BLOCK_TAGS.getSpec().getComment()))
        .setSaveConsumer(EXCAVATION_BLOCK_TAGS::set)
        .setInsertButtonEnabled(true)
        .build());

    general.addEntry(entryBuilder.startStrList(
            Component.literal(EXCAVATION_BLOCK_NAMES.getSpec().getTranslationKey()),
            (List<String>) EXCAVATION_BLOCK_NAMES.get()
        ).setDefaultValue((List<String>) EXCAVATION_BLOCK_NAMES.getDefault())
        .setTooltip(Component.literal(EXCAVATION_BLOCK_NAMES.getSpec().getComment()))
        .setSaveConsumer(EXCAVATION_BLOCK_NAMES::set)
        .setInsertButtonEnabled(true)
        .build());

    general.addEntry(entryBuilder.startStrList(
            Component.literal(DROP_NAMES.getSpec().getTranslationKey()),
            (List<String>) DROP_NAMES.get()
        ).setDefaultValue((List<String>) DROP_NAMES.getDefault())
        .setTooltip(Component.literal(DROP_NAMES.getSpec().getComment()))
        .setSaveConsumer(DROP_NAMES::set)
        .setInsertButtonEnabled(true)
        .build());

    general.addEntry(entryBuilder.startStrField(
            Component.literal(LUCKY_MESSAGE.getSpec().getTranslationKey()),
            LUCKY_MESSAGE.get()
        ).setDefaultValue(LUCKY_MESSAGE.getDefault())
        .setTooltip(Component.literal(LUCKY_MESSAGE.getSpec().getComment()))
        .setSaveConsumer(LUCKY_MESSAGE::set)
        .build());

    general.addEntry(entryBuilder.startBooleanToggle(
            Component.literal(LUCKY_MESSAGE_ENABLE.getSpec().getTranslationKey()),
            LUCKY_MESSAGE_ENABLE.get()
        ).setDefaultValue(LUCKY_MESSAGE_ENABLE.getDefault())
        .setTooltip(Component.literal(LUCKY_MESSAGE_ENABLE.getSpec().getComment()))
        .setSaveConsumer(LUCKY_MESSAGE_ENABLE::set)
        .build());

    return builder.build();
  }

  private static void validateConfig() {
    //TODO check tags somehow
    parseLocations(EXCAVATION_BLOCK_TAGS.get());

    List<ResourceLocation> blockNames = parseLocations(EXCAVATION_BLOCK_NAMES.get());
    validateResourceLocations(BuiltInRegistries.BLOCK, blockNames);

    List<ResourceLocation> itemNames = parseLocations(DROP_NAMES.get());
    validateResourceLocations(BuiltInRegistries.ITEM, itemNames);
  }

  private static List<ResourceLocation> parseLocations(List<? extends String> locations) {
    return Optional.ofNullable(locations)
        .orElse(Collections.emptyList())
        .stream()
        .map(it -> {
          String[] namespace = it.split(Config.NAMESPACE_SEPARATOR);
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
