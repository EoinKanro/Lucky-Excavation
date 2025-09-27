package io.github.eoinkanro.mc.luckyexcavation.handler;

import static io.github.eoinkanro.mc.luckyexcavation.conf.Constants.LOG;

import io.github.eoinkanro.mc.luckyexcavation.conf.Config;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

/**
 * Handles break event and calculates item drop
 */
@RequiredArgsConstructor
public class ExcavationEventHandler {

  private final Config config;

  public void handle(Player player, Block destoyedBlock, BlockPos blockPos, Level level) {
    if (player == null || destoyedBlock == null || blockPos == null || level == null) {
      LOG.warn("Can't handle excavation event. Not enough data");
      return;
    }

    if (config.getParsedDropNames() == null || config.getParsedDropNames().isEmpty()) {
      return;
    }

    if (!config.getDropEnableInCreative().getCurrentValue() && player.isCreative()) {
      return;
    }

    if (!isLucky()) {
      return;
    }

    if (!isBlockMatches(destoyedBlock)) {
      return;
    }

    Item luckyItem = config.getParsedDropNames().get(getRandom(0, config.getParsedDropNames().size()));

    ItemStack itemStack = new ItemStack(luckyItem, getItemCount());
    ItemEntity mineralItemEntity = new ItemEntity(level, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, itemStack);
    level.addFreshEntity(mineralItemEntity);

    if (config.getLuckyMessageEnable().getCurrentValue()
        && config.getLuckyMessage().getCurrentValue() != null
        && !config.getLuckyMessage().getCurrentValue().isBlank()) {
      player.sendSystemMessage(createLuckyMessage());
    }
  }

  private boolean isLucky() {
    int random = getRandom(config.getDropChance().getMinValue(), config.getDropChance().getMaxValue() + 1);
    return random <= config.getDropChance().getCurrentValue();
  }

  private boolean isBlockMatches(Block destroyedBlock) {
    return destroyedBlock != null && (isBlockMatchesTag(destroyedBlock) || isBlockMatchesName(destroyedBlock));
  }

  /**
   * Is block matches {@link Config#parsedExcavationBlockTags}
   */
  private boolean isBlockMatchesTag(Block destroyedBlock) {
    if (config.getParsedExcavationBlockTags() == null || config.getParsedExcavationBlockTags().isEmpty()) {
      return false;
    }

    ResourceKey<Block> resourceKey = BuiltInRegistries.BLOCK.getResourceKey(destroyedBlock).orElse(null);
    if (resourceKey == null) {
      return false;
    }

    Holder<Block> blockHolder = BuiltInRegistries.BLOCK.getHolder(resourceKey).orElse(null);
    if (blockHolder == null) {
      return false;
    }

    return config.getParsedExcavationBlockTags().stream().anyMatch(blockHolder::is);
  }

  /**
   * Is block matches {@link Config#parsedExcavationBlockNames}
   */
  private boolean isBlockMatchesName(Block destroyedBlock) {
    if (config.getParsedExcavationBlockNames() == null || config.getParsedExcavationBlockNames().isEmpty()) {
      return false;
    }

    ResourceLocation resourceLocation = BuiltInRegistries.BLOCK.getKey(destroyedBlock);
    return config.getParsedExcavationBlockNames().stream().anyMatch(resourceLocation::equals);
  }

  private int getItemCount() {
    if (Objects.equals(config.getDropChanceMultiplayer().getCurrentValue(), config.getDropChanceMultiplayer().getMinValue())) {
      return config.getDropChanceMultiplayer().getMinValue();
    }
    return getRandom(config.getDropChanceMultiplayer().getMinValue(), config.getDropChanceMultiplayer().getCurrentValue() + 1);
  }

  private MutableComponent createLuckyMessage() {
    MutableComponent message = Component.literal(config.getLuckyMessage().getCurrentValue());
    message.withStyle(ChatFormatting.GOLD);
    return message;
  }

  private int getRandom(int min, int max) {
    return ThreadLocalRandom.current().nextInt(min, max);
  }

}
