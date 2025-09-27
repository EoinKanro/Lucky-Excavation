package io.github.eoinkanro.mc.luckyexcavation.conf;

import static io.github.eoinkanro.mc.kanrommon.utils.ResourceUtils.getNamespaceAndName;

import io.github.eoinkanro.mc.kanrommon.conf.ConfigDefinition;
import io.github.eoinkanro.mc.kanrommon.conf.ConfigLoader;
import io.github.eoinkanro.mc.kanrommon.conf.ConfigNumberDefinition;
import io.github.eoinkanro.mc.kanrommon.utils.ResourceUtils;
import java.util.List;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

public class Config {

    public static int MIN_CHANCE = 0;
    public static int MAX_CHANCE = 1000;

    public static int MIN_CHANCE_MULTIPLAYER = 1;
    public static int MAX_CHANCE_MULTIPLAYER = 64;

    static final ConfigNumberDefinition<Integer> DROP_CHANCE =
        ConfigNumberDefinition.<Integer>builder()
            .name("Drop chance")
            .description("Drop chance during excavation")
            .parameterName("dropChance")
            .defaultValue(20)
            .minValue(MIN_CHANCE)
            .maxValue(MAX_CHANCE)
            .build();

    static final ConfigNumberDefinition<Integer> DROP_CHANCE_MULTIPLAYER =
        ConfigNumberDefinition.<Integer>builder()
            .name("Drop chance multiplayer")
            .description("Max number of drop items. So on lucky event it will spawn random count of items from 1 to the number")
            .parameterName("dropChanceMultiplayer")
            .defaultValue(MIN_CHANCE_MULTIPLAYER)
            .minValue(MIN_CHANCE_MULTIPLAYER)
            .maxValue(MAX_CHANCE_MULTIPLAYER)
            .build();

    static final ConfigDefinition<Boolean> DROP_ENABLE_IN_CREATIVE =
        ConfigDefinition.<Boolean>builder()
            .name("Enable in creative")
            .description("Will mod work in creative")
            .parameterName("dropEnableInCreative")
            .defaultValue(false)
            .build();

    static final ConfigDefinition<List<String>> EXCAVATION_BLOCK_TAGS =
        ConfigDefinition.<List<String>>builder()
            .name("Excavation block tags")
            .description("List of blocks' tags lucky items will drop from on destroying")
            .parameterName("excavationBlockTags")
            .defaultValue(List.of(
                getNamespaceAndName(BlockTags.BASE_STONE_OVERWORLD),
                getNamespaceAndName(BlockTags.BASE_STONE_NETHER)))
            .build();

    static final ConfigDefinition<List<String>> EXCAVATION_BLOCK_NAMES =
        ConfigDefinition.<List<String>>builder()
            .name("Excavation block names")
            .description("List of blocks' names lucky items will drop from on destroying")
            .parameterName("excavationBlockNames")
            .defaultValue(List.of())
            .build();

    static final ConfigDefinition<List<String>> DROP_NAMES =
        ConfigDefinition.<List<String>>builder()
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

    static final ConfigDefinition<String> LUCKY_MESSAGE =
        ConfigDefinition.<String>builder()
            .name("Chat notification")
            .description("Message when player gets a lucky item")
            .parameterName("luckyMessage")
            .defaultValue("You've found something. Lucky you!")
            .build();

    static final ConfigDefinition<Boolean> LUCKY_MESSAGE_ENABLE =
        ConfigDefinition.<Boolean>builder()
            .name("Enable chat notifications")
            .description("Will the lucky message appear")
            .parameterName("luckyMessageEnable")
            .defaultValue(true)
            .build();

    public static int dropChance;
    public static int dropChanceMultiplayer;
    public static boolean dropEnableInCreative;

    public static Set<TagKey<Block>> excavationBlockTags;
    public static Set<ResourceLocation> excavationBlockNames;
    public static List<Item> dropNames;

    public static String luckyMessage;
    public static boolean luckyMessageEnable;

    public static ConfigLoader configLoader;

    public static void init(ConfigLoader loader) {
        configLoader = loader;
        loader.load();
    }

    public static void setDropChance(Integer integer) {
        DROP_CHANCE.setCurrentValue(integer);
        dropChance = integer;
    }

    public static void setDropChanceMultiplayer(Integer integer) {
        DROP_CHANCE_MULTIPLAYER.setCurrentValue(integer);
        dropChanceMultiplayer = integer;
    }

    public static void setDropEnableInCreative(Boolean bool) {
        DROP_ENABLE_IN_CREATIVE.setCurrentValue(bool);
        dropEnableInCreative = bool;
    }

    public static void setExcavationBlockTags(List<String> list) {
        EXCAVATION_BLOCK_TAGS.setCurrentValue(list);
        excavationBlockTags = ResourceUtils.stringsToTags(list);
    }

    public static void setExcavationBlockNames(List<String> list) {
        EXCAVATION_BLOCK_NAMES.setCurrentValue(list);
        excavationBlockNames = ResourceUtils.stringsToLocations(list);
    }

    public static void setDropNames(List<String> list) {
        DROP_NAMES.setCurrentValue(list);
        dropNames = ResourceUtils.stringsToItems(list);
    }

    public static void setLuckyMessage(String message) {
        LUCKY_MESSAGE.setCurrentValue(message);
        luckyMessage = message;
    }

    public static void setLuckyMessageEnable(boolean enable) {
        LUCKY_MESSAGE_ENABLE.setCurrentValue(enable);
        luckyMessageEnable = enable;
    }

}
