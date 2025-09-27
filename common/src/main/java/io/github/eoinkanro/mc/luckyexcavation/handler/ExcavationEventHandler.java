package io.github.eoinkanro.mc.luckyexcavation.handler;

import static io.github.eoinkanro.mc.luckyexcavation.conf.Constants.LOG;

import io.github.eoinkanro.mc.luckyexcavation.conf.Config;
import java.util.concurrent.ThreadLocalRandom;
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
public class ExcavationEventHandler {

  public static void handle(Player player, Block destoyedBlock, BlockPos blockPos, Level level) {
    if (player == null || destoyedBlock == null || blockPos == null || level == null) {
      LOG.warn("Can't handle excavation event. Not enough data");
      return;
    }

    if (Config.dropNames == null || Config.dropNames.isEmpty()) {
      return;
    }

    if (!Config.dropEnableInCreative && player.isCreative()) {
      return;
    }

    if (!isLucky()) {
      return;
    }

    if (!isBlockMatches(destoyedBlock)) {
      return;
    }

    Item luckyItem = Config.dropNames.get(getRandom(0, Config.dropNames.size()));

    ItemStack itemStack = new ItemStack(luckyItem, getItemCount());
    ItemEntity mineralItemEntity = new ItemEntity(level, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, itemStack);
    level.addFreshEntity(mineralItemEntity);

    if (Config.luckyMessageEnable && Config.luckyMessage != null && !Config.luckyMessage.isBlank()) {
      player.sendSystemMessage(createLuckyMessage());
    }
  }

  private static boolean isLucky() {
    int random = getRandom(Config.MIN_CHANCE, Config.MAX_CHANCE + 1);
    return random <= Config.dropChance;
  }

  private static boolean isBlockMatches(Block destroyedBlock) {
    return destroyedBlock != null && (isBlockMatchesTag(destroyedBlock) || isBlockMatchesName(destroyedBlock));
  }

  /**
   * Is block matches {@link Config#excavationBlockTags}
   */
  private static boolean isBlockMatchesTag(Block destroyedBlock) {
    if (Config.excavationBlockTags == null || Config.excavationBlockTags.isEmpty()) {
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

    return Config.excavationBlockTags.stream().anyMatch(blockHolder::is);
  }

  /**
   * Is block matches {@link Config#excavationBlockNames}
   */
  private static boolean isBlockMatchesName(Block destroyedBlock) {
    if (Config.excavationBlockNames == null || Config.excavationBlockNames.isEmpty()) {
      return false;
    }

    ResourceLocation resourceLocation = BuiltInRegistries.BLOCK.getKey(destroyedBlock);
    return Config.excavationBlockNames.stream().anyMatch(resourceLocation::equals);
  }

  private static int getItemCount() {
    if (Config.dropChanceMultiplayer == Config.MIN_CHANCE_MULTIPLAYER) {
      return Config.MIN_CHANCE_MULTIPLAYER;
    }
    return getRandom(Config.MIN_CHANCE_MULTIPLAYER, Config.dropChanceMultiplayer + 1);
  }

  private static MutableComponent createLuckyMessage() {
    MutableComponent message = Component.literal(Config.luckyMessage);
    message.withStyle(ChatFormatting.GOLD);
    return message;
  }

  private static int getRandom(int min, int max) {
    return ThreadLocalRandom.current().nextInt(min, max);
  }

}
