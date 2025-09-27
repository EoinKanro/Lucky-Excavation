package io.github.eoinkanro.mc.luckyexcavation.conf;

import static io.github.eoinkanro.mc.kanrommon.utils.ResourceUtils.getNamespaceAndName;
import static io.github.eoinkanro.mc.luckyexcavation.conf.Constants.MOD_ID;

import io.github.eoinkanro.mc.kanrommon.conf.ConfigManager;
import io.github.eoinkanro.mc.kanrommon.conf.impl.ConfigBooleanDefinition;
import io.github.eoinkanro.mc.kanrommon.conf.impl.ConfigIntDefinition;
import io.github.eoinkanro.mc.kanrommon.conf.impl.ConfigListDefinition;
import io.github.eoinkanro.mc.kanrommon.conf.impl.ConfigStringDefinition;
import io.github.eoinkanro.mc.kanrommon.utils.ResourceUtils;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

@Getter
public class Config {

  private final ConfigIntDefinition dropChance =
      ConfigIntDefinition.builder()
          .name("Drop chance")
          .description("Drop chance during excavation")
          .parameterName("dropChance")
          .defaultValue(20)
          .minValue(0)
          .maxValue(1000)
          .build();

  private final ConfigIntDefinition dropChanceMultiplayer =
      ConfigIntDefinition.builder()
          .name("Drop chance multiplayer")
          .description(
              "Max number of drop items. So on lucky event it will spawn random count of items from 1 to the number")
          .parameterName("dropChanceMultiplayer")
          .defaultValue(1)
          .minValue(1)
          .maxValue(64)
          .build();

  private final ConfigBooleanDefinition dropEnableInCreative =
      ConfigBooleanDefinition.builder()
          .name("Enable in creative")
          .description("Will mod work in creative")
          .parameterName("dropEnableInCreative")
          .defaultValue(false)
          .build();

  private final ConfigListDefinition excavationBlockTags =
      ConfigListDefinition.builder()
          .name("Excavation block tags")
          .description("List of blocks' tags lucky items will drop from on destroying")
          .parameterName("excavationBlockTags")
          .defaultValue(List.of(
              getNamespaceAndName(BlockTags.BASE_STONE_OVERWORLD),
              getNamespaceAndName(BlockTags.BASE_STONE_NETHER)))
          .build();

  private final ConfigListDefinition excavationBlockNames =
      ConfigListDefinition.builder()
          .name("Excavation block names")
          .description("List of blocks' names lucky items will drop from on destroying")
          .parameterName("excavationBlockNames")
          .defaultValue(List.of())
          .build();

  private final ConfigListDefinition dropNames =
      ConfigListDefinition.builder()
          .name("Lucky item names")
          .description("List of lucky items' names that will drop from blocks on destroying")
          .parameterName("dropNames")
          .defaultValue(List.of(getNamespaceAndName(Items.DIAMOND),
              getNamespaceAndName(Items.EMERALD),
              getNamespaceAndName(Items.RAW_GOLD),
              getNamespaceAndName(Items.RAW_IRON),
              getNamespaceAndName(Items.RAW_COPPER),
              getNamespaceAndName(Items.COAL),
              getNamespaceAndName(Items.REDSTONE),
              getNamespaceAndName(Items.LAPIS_LAZULI)))
          .build();

  private final ConfigStringDefinition luckyMessage =
      ConfigStringDefinition.builder()
          .name("Chat notification")
          .description("Message when player gets a lucky item")
          .parameterName("luckyMessage")
          .defaultValue("You've found something. Lucky you!")
          .build();

  private final ConfigBooleanDefinition luckyMessageEnable =
      ConfigBooleanDefinition.builder()
          .name("Enable chat notifications")
          .description("Will the lucky message appear")
          .parameterName("luckyMessageEnable")
          .defaultValue(true)
          .build();

  public Set<TagKey<Block>> parsedExcavationBlockTags;
  public Set<ResourceLocation> parsedExcavationBlockNames;
  public List<Item> parsedDropNames;

  private final String configName = MOD_ID;
  private final Path configFolderPath;

  @Getter(AccessLevel.PRIVATE)
  private final ConfigManager configManager;

  public Config(@NonNull Path configFolderPath) {
    this.configFolderPath = configFolderPath;
    this.configManager = new ConfigManager(configFolderPath, configName);
  }

  public void reload() {
    configManager.reload();

    configManager.read(dropChance);
    configManager.read(dropChanceMultiplayer);
    configManager.read(dropEnableInCreative);
    configManager.read(excavationBlockTags);
    configManager.read(excavationBlockNames);
    configManager.read(dropNames);
    configManager.read(luckyMessage);
    configManager.read(luckyMessageEnable);

    parsedExcavationBlockTags = ResourceUtils.stringsToTags(excavationBlockTags.getCurrentValue());
    parsedExcavationBlockNames = ResourceUtils.stringsToLocations(excavationBlockNames.getCurrentValue());
    parsedDropNames = ResourceUtils.stringsToItems(dropNames.getCurrentValue());
  }

  public void save() {
    configManager.write(dropChance);
    configManager.write(dropChanceMultiplayer);
    configManager.write(dropEnableInCreative);
    configManager.write(excavationBlockTags);
    configManager.write(excavationBlockNames);
    configManager.write(dropNames);
    configManager.write(luckyMessage);
    configManager.write(luckyMessageEnable);

    configManager.save();
  }


}
