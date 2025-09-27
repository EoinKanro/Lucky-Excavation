package io.github.eoinkanro.mc.luckyexcavation.conf;

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

  private final Config backup;
  private final Config mainConfig;

  public ConfigClothScreen(Config mainConfig) {
    this.mainConfig = mainConfig;
    this.backup = new Config(mainConfig.getConfigFolderPath());
    this.backup.reload();
  }

  public Screen createScreen(Screen parent) {
    ConfigBuilder builder = ConfigBuilder.create()
        .setParentScreen(parent)
        .setTitle(Component.literal("Lucky Excavation Config"))
        .setSavingRunnable(() -> {
          try {
            validateConfig();
            backup.save();
            mainConfig.reload();
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
            Component.literal(backup.getDropChance().getName()),
            backup.getDropChance().getCurrentValue()
        ).setDefaultValue(backup.getDropChance().getDefaultValue())
        .setTooltip(Component.literal(backup.getDropChance().getDescription()))
        .setSaveConsumer(backup.getDropChance()::setCurrentValue)
        .setMin(backup.getDropChance().getMinValue())
        .setMax(backup.getDropChance().getMaxValue())
        .build());

    general.addEntry(entryBuilder.startIntField(
            Component.literal(backup.getDropChanceMultiplayer().getName()),
            backup.getDropChanceMultiplayer().getCurrentValue()
        ).setDefaultValue(backup.getDropChanceMultiplayer().getDefaultValue())
        .setTooltip(Component.literal(backup.getDropChanceMultiplayer().getDescription()))
        .setSaveConsumer(backup.getDropChanceMultiplayer()::setCurrentValue)
        .setMin(backup.getDropChanceMultiplayer().getMinValue())
        .setMax(backup.getDropChanceMultiplayer().getMaxValue())
        .build());

    general.addEntry(entryBuilder.startBooleanToggle(
            Component.literal(backup.getDropEnableInCreative().getName()),
            backup.getDropEnableInCreative().getCurrentValue()
        ).setDefaultValue(backup.getDropEnableInCreative().getDefaultValue())
        .setTooltip(Component.literal(backup.getDropEnableInCreative().getDescription()))
        .setSaveConsumer(backup.getDropEnableInCreative()::setCurrentValue)
        .build());

    general.addEntry(entryBuilder.startStrList(
            Component.literal(backup.getExcavationBlockTags().getName()),
            backup.getExcavationBlockTags().getCurrentValue()
        ).setDefaultValue(backup.getExcavationBlockTags().getDefaultValue())
        .setTooltip(Component.literal(backup.getExcavationBlockTags().getDescription()))
        .setSaveConsumer(backup.getExcavationBlockTags()::setCurrentValue)
        .setInsertButtonEnabled(true)
        .build());

    general.addEntry(entryBuilder.startStrList(
            Component.literal(backup.getExcavationBlockNames().getName()),
            backup.getExcavationBlockNames().getCurrentValue()
        ).setDefaultValue(backup.getExcavationBlockNames().getDefaultValue())
        .setTooltip(Component.literal(backup.getExcavationBlockNames().getDescription()))
        .setSaveConsumer(backup.getExcavationBlockNames()::setCurrentValue)
        .setInsertButtonEnabled(true)
        .build());

    general.addEntry(entryBuilder.startStrList(
            Component.literal(backup.getDropNames().getName()),
            backup.getDropNames().getCurrentValue()
        ).setDefaultValue(backup.getDropNames().getDefaultValue())
        .setTooltip(Component.literal(backup.getDropNames().getDescription()))
        .setSaveConsumer(backup.getDropNames()::setCurrentValue)
        .setInsertButtonEnabled(true)
        .build());

    general.addEntry(entryBuilder.startStrField(
            Component.literal(backup.getLuckyMessage().getName()),
            backup.getLuckyMessage().getCurrentValue()
        ).setDefaultValue(backup.getLuckyMessage().getDefaultValue())
        .setTooltip(Component.literal(backup.getLuckyMessage().getDescription()))
        .setSaveConsumer(backup.getLuckyMessage()::setCurrentValue)
        .build());

    general.addEntry(entryBuilder.startBooleanToggle(
            Component.literal(backup.getLuckyMessageEnable().getName()),
            backup.getLuckyMessageEnable().getCurrentValue()
        ).setDefaultValue(backup.getLuckyMessageEnable().getDefaultValue())
        .setTooltip(Component.literal(backup.getLuckyMessageEnable().getDescription()))
        .setSaveConsumer(backup.getLuckyMessageEnable()::setCurrentValue)
        .build());

    return builder.build();
  }

  private void validateConfig() {
    //TODO check tags somehow
    parseLocations(backup.getExcavationBlockTags().getCurrentValue());

    List<ResourceLocation> blockNames = parseLocations(backup.getExcavationBlockNames().getCurrentValue());
    validateResourceLocations(BuiltInRegistries.BLOCK, blockNames);

    List<ResourceLocation> itemNames = parseLocations(backup.getDropNames().getCurrentValue());
    validateResourceLocations(BuiltInRegistries.ITEM, itemNames);
  }

  private List<ResourceLocation> parseLocations(List<? extends String> locations) {
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

  private void validateResourceLocations(DefaultedRegistry<?> registry, List<ResourceLocation> locations) {
    for (ResourceLocation blockName : locations) {
      if (!registry.containsKey(blockName)) {
        throw new IllegalArgumentException("Can't find name: " + blockName);
      }
    }
  }

}
