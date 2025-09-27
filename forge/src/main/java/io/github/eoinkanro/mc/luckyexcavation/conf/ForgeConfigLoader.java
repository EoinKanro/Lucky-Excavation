package io.github.eoinkanro.mc.luckyexcavation.conf;

import static io.github.eoinkanro.mc.luckyexcavation.conf.Constants.LOG;

import io.github.eoinkanro.mc.kanrommon.conf.ConfigLoader;
import java.util.List;
import net.minecraftforge.common.ForgeConfigSpec;

public class ForgeConfigLoader implements ConfigLoader {

  private final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

  private final ForgeConfigSpec.IntValue DROP_CHANCE = BUILDER
      .comment(Config.DROP_CHANCE.getDescription())
      .translation(Config.DROP_CHANCE.getName())
      .defineInRange(Config.DROP_CHANCE.getParameterName(),
          Config.DROP_CHANCE.getDefaultValue(),
          Config.DROP_CHANCE.getMinValue(),
          Config.DROP_CHANCE.getMaxValue());

  private final ForgeConfigSpec.IntValue DROP_CHANCE_MULTIPLAYER = BUILDER
      .comment(Config.DROP_CHANCE_MULTIPLAYER.getDescription())
      .translation(Config.DROP_CHANCE_MULTIPLAYER.getName())
      .defineInRange(Config.DROP_CHANCE_MULTIPLAYER.getParameterName(),
          Config.DROP_CHANCE_MULTIPLAYER.getDefaultValue(),
          Config.DROP_CHANCE_MULTIPLAYER.getMinValue(),
          Config.DROP_CHANCE_MULTIPLAYER.getMaxValue());

  private final ForgeConfigSpec.ConfigValue<Boolean> DROP_ENABLE_IN_CREATIVE = BUILDER
      .comment(Config.DROP_ENABLE_IN_CREATIVE.getDescription())
      .translation(Config.DROP_ENABLE_IN_CREATIVE.getName())
      .define(Config.DROP_ENABLE_IN_CREATIVE.getParameterName(),
          Config.DROP_ENABLE_IN_CREATIVE.getDefaultValue());

  private final ForgeConfigSpec.ConfigValue<List<? extends String>> EXCAVATION_BLOCK_TAGS = BUILDER
      .comment(Config.EXCAVATION_BLOCK_TAGS.getDescription())
      .translation(Config.EXCAVATION_BLOCK_TAGS.getName())
      .defineListAllowEmpty(Config.EXCAVATION_BLOCK_TAGS.getParameterName(),
          Config.EXCAVATION_BLOCK_TAGS.getDefaultValue(),
          __ -> true);

  private final ForgeConfigSpec.ConfigValue<List<? extends String>> EXCAVATION_BLOCK_NAMES = BUILDER
      .comment(Config.EXCAVATION_BLOCK_NAMES.getDescription())
      .translation(Config.EXCAVATION_BLOCK_NAMES.getName())
      .defineListAllowEmpty(Config.EXCAVATION_BLOCK_NAMES.getParameterName(),
          Config.EXCAVATION_BLOCK_NAMES.getDefaultValue(),
          __ -> true);

  private final ForgeConfigSpec.ConfigValue<List<? extends String>> DROP_NAMES = BUILDER
      .comment(Config.DROP_NAMES.getDescription())
      .translation(Config.DROP_NAMES.getName())
      .defineListAllowEmpty(Config.DROP_NAMES.getParameterName(),
          Config.DROP_NAMES.getDefaultValue(),
          __ -> true);

  private final ForgeConfigSpec.ConfigValue<String> LUCKY_MESSAGE = BUILDER
      .comment(Config.LUCKY_MESSAGE.getDescription())
      .translation(Config.LUCKY_MESSAGE.getName())
      .define(Config.LUCKY_MESSAGE.getParameterName(),
          Config.LUCKY_MESSAGE.getDefaultValue());

  private final ForgeConfigSpec.ConfigValue<Boolean> LUCKY_MESSAGE_ENABLE = BUILDER
      .comment(Config.LUCKY_MESSAGE_ENABLE.getDescription())
      .translation(Config.LUCKY_MESSAGE_ENABLE.getName())
      .define(Config.LUCKY_MESSAGE_ENABLE.getParameterName(),
          Config.LUCKY_MESSAGE_ENABLE.getDefaultValue());

  public final ForgeConfigSpec SPEC = BUILDER.build();

  @Override
  public void load() {
    Config.setDropChance(DROP_CHANCE.get());
    Config.setDropChanceMultiplayer(DROP_CHANCE_MULTIPLAYER.get());
    Config.setDropEnableInCreative(DROP_ENABLE_IN_CREATIVE.get());
    Config.setExcavationBlockTags((List<String>) EXCAVATION_BLOCK_TAGS.get());
    Config.setExcavationBlockNames((List<String>) EXCAVATION_BLOCK_NAMES.get());
    Config.setDropNames((List<String>) DROP_NAMES.get());
    Config.setLuckyMessage(LUCKY_MESSAGE.get());
    Config.setLuckyMessageEnable(LUCKY_MESSAGE_ENABLE.get());
    LOG.info("Lucky config loaded");
  }

  @Override
  public void save() {
    DROP_CHANCE.set(Config.DROP_CHANCE.getCurrentValue());
    DROP_CHANCE_MULTIPLAYER.set(Config.DROP_CHANCE_MULTIPLAYER.getCurrentValue());
    DROP_ENABLE_IN_CREATIVE.set(Config.DROP_ENABLE_IN_CREATIVE.getCurrentValue());
    EXCAVATION_BLOCK_TAGS.set(Config.EXCAVATION_BLOCK_TAGS.getCurrentValue());
    EXCAVATION_BLOCK_NAMES.set(Config.EXCAVATION_BLOCK_NAMES.getCurrentValue());
    DROP_NAMES.set(Config.DROP_NAMES.getCurrentValue());
    LUCKY_MESSAGE.set(Config.LUCKY_MESSAGE.getCurrentValue());
    LUCKY_MESSAGE_ENABLE.set(Config.LUCKY_MESSAGE_ENABLE.getCurrentValue());
    SPEC.save();
  }

}
