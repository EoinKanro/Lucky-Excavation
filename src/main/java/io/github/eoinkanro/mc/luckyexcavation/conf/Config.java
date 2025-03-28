package io.github.eoinkanro.mc.luckyexcavation.conf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Config {

    public static final String NAMESPACE_SEPARATOR = ":";

    public static int MIN_CHANCE = 0;
    public static int MAX_CHANCE = 1000;

    public static int MIN_CHANCE_MULTIPLAYER = 1;
    public static int MAX_CHANCE_MULTIPLAYER = 64;

    private static final Logger LOGGER = LogManager.getLogger();

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    static final ModConfigSpec.IntValue DROP_CHANCE = BUILDER
        .comment("Drop chance during excavation")
        .translation("Drop chance")
        .defineInRange("dropChance", 20, MIN_CHANCE, MAX_CHANCE);

    static final ModConfigSpec.IntValue DROP_CHANCE_MULTIPLAYER = BUILDER
        .comment("Max number of drop items. So on lucky event it will spawn random count of items from 1 to the number")
        .translation("Drop chance multiplayer")
        .defineInRange("dropChanceMultiplayer", MIN_CHANCE_MULTIPLAYER, MIN_CHANCE_MULTIPLAYER, MAX_CHANCE_MULTIPLAYER);

    static final ModConfigSpec.BooleanValue DROP_ENABLE_IN_CREATIVE = BUILDER
        .comment("Will mod work in creative")
        .translation("Enable in creative")
        .define("dropEnableInCreative", false);

    static final ModConfigSpec.ConfigValue<List<? extends String>> EXCAVATION_BLOCK_TAGS = BUILDER
        .comment("List of blocks' tags lucky items will drop from on destroying")
        .translation("Excavation block tags")
        .defineListAllowEmpty("excavationBlockTags",
            List.of(getNamespaceAndName(BlockTags.BASE_STONE_OVERWORLD),
                getNamespaceAndName(BlockTags.BASE_STONE_NETHER)),
            __ -> true);

    static final ModConfigSpec.ConfigValue<List<? extends String>> EXCAVATION_BLOCK_NAMES = BUILDER
        .comment("List of blocks' names lucky items will drop from on destroying")
        .translation("Excavation block names")
        .defineListAllowEmpty("excavationBlockNames",
            List.of(),
            __ -> true);

    static final ModConfigSpec.ConfigValue<List<? extends String>> DROP_NAMES = BUILDER
        .comment("List of lucky items' names that will drop from blocks on destroying")
        .translation("Lucky item names")
        .defineListAllowEmpty("dropNames",
            List.of(getNamespaceAndName(Items.DIAMOND),
                getNamespaceAndName(Items.EMERALD),
                getNamespaceAndName(Items.RAW_GOLD),
                getNamespaceAndName(Items.RAW_IRON),
                getNamespaceAndName(Items.RAW_COPPER),
                getNamespaceAndName(Items.COAL),
                getNamespaceAndName(Items.REDSTONE),
                getNamespaceAndName(Items.LAPIS_LAZULI)),
            __ -> true);

    static final ModConfigSpec.ConfigValue<String> LUCKY_MESSAGE = BUILDER
        .comment("Message when player gets a lucky item")
        .translation("Chat notification")
        .define("luckyMessage", "You've found something. Lucky you!");

    static final ModConfigSpec.BooleanValue LUCKY_MESSAGE_ENABLE = BUILDER
        .comment("Will the lucky message appear")
        .translation("Enable chat notifications")
        .define("luckyMessageEnable", true);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static int dropChance;
    public static int dropChanceMultiplayer;
    public static boolean dropEnableInCreative;

    public static Set<TagKey<Block>> excavationBlockTags;
    public static Set<ResourceLocation> excavationBlockNames;
    public static List<Item> dropNames;

    public static String luckyMessage;
    public static boolean luckyMessageEnable;

    private static String getNamespaceAndName(TagKey<Block> block) {
        return block.location().toString();
    }

    private static String getNamespaceAndName(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).toString();
    }

    public static void loadConfig() {
        LOGGER.info("Configuring Lucky Excavation...");

        dropChance = DROP_CHANCE.get();
        dropChanceMultiplayer = DROP_CHANCE_MULTIPLAYER.get();
        dropEnableInCreative = DROP_ENABLE_IN_CREATIVE.get();

        excavationBlockTags = stringsToTags(EXCAVATION_BLOCK_TAGS.get());
        excavationBlockNames = stringsToLocations(EXCAVATION_BLOCK_NAMES.get());
        dropNames = stringsToItems(DROP_NAMES.get());

        luckyMessage = LUCKY_MESSAGE.get();
        luckyMessageEnable = LUCKY_MESSAGE_ENABLE.get();

        validateResourceLocations(BuiltInRegistries.BLOCK, excavationBlockNames);
        validateResourceLocations(BuiltInRegistries.ITEM, excavationBlockNames);

        LOGGER.info("Someone is going to be lucky ;)");
    }

    private static Set<TagKey<Block>> stringsToTags(List<? extends String> strings) {
        return parseLocations(strings)
            .stream()
            .map(BlockTags::create)
            .collect(Collectors.toSet());
    }

    private static Set<ResourceLocation> stringsToLocations(List<? extends String> strings) {
        return new HashSet<>(parseLocations(strings));
    }

    private static List<Item> stringsToItems(List<? extends String> strings) {
        return parseLocations(strings)
            .stream()
            .map(BuiltInRegistries.ITEM::get)
            .collect(Collectors.toSet())
            .stream()
            .toList();
    }

    private static List<ResourceLocation> parseLocations(List<? extends String> strings) {
        if (strings == null || strings.isEmpty()) {
            return Collections.emptyList();
        }

        List<ResourceLocation> result = new ArrayList<>();
        for (String string : strings) {
            if (!string.contains(NAMESPACE_SEPARATOR)) {
                LOGGER.warn("Wrong namespace: {}, skipping", string);
                continue;
            }
            result.add(ResourceLocation.tryParse(string));
        }
        return result;
    }

    static void validateResourceLocations(DefaultedRegistry<?> registry, Iterable<ResourceLocation> locations) {
        for (ResourceLocation element : locations) {
            if (!registry.containsKey(element)) {
                LOGGER.warn("Unknown element: {}", element);
            }
        }
    }

}
