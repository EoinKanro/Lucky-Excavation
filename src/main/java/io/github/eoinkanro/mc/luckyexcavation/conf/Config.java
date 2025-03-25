package io.github.eoinkanro.mc.luckyexcavation.conf;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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

    public static int MIN_CHANCE = 0;
    public static int MAX_CHANCE = 1000;

    public static int MIN_CHANCE_MULTIPLAYER = 1;
    public static int MAX_CHANCE_MULTIPLAYER = 64;

    private static final Logger LOGGER = LogManager.getLogger();

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue DROP_CHANCE = BUILDER
        .comment("Drop chance during excavation")
        .defineInRange("dropChance", 20, MIN_CHANCE, MAX_CHANCE);

    private static final ModConfigSpec.IntValue DROP_CHANCE_MULTIPLAYER = BUILDER
        .comment("Max number of drop items. So on lucky event it will spawn random count of items from 1 to the number")
        .defineInRange("dropChanceMultiplayer", MIN_CHANCE_MULTIPLAYER, MIN_CHANCE_MULTIPLAYER, MAX_CHANCE_MULTIPLAYER);

    private static final ModConfigSpec.BooleanValue DROP_ENABLE_IN_CREATIVE = BUILDER
        .comment("Will drop chance work in creative")
        .define("dropEnableInCreative", false);

    private static final ModConfigSpec.ConfigValue<List<? extends String>> EXCAVATION_BLOCK_TAGS = BUILDER
        .comment("List of blocks' tags lucky items will drop from on destroying.")
        .defineListAllowEmpty("excavationBlockTags",
            List.of(getNamespaceAndName(BlockTags.BASE_STONE_OVERWORLD),
                getNamespaceAndName(BlockTags.BASE_STONE_NETHER)),
            __ -> true);

    private static final ModConfigSpec.ConfigValue<List<? extends String>> EXCAVATION_BLOCK_NAMES = BUILDER
        .comment("List of blocks' names lucky items will drop from on destroying")
        .defineListAllowEmpty("excavationBlockNames",
            List.of(),
            __ -> true);

    private static final ModConfigSpec.ConfigValue<List<? extends String>> DROP_NAMES = BUILDER
        .comment("List of lucky items' names that will drop from blocks on destroying")
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

    private static final ModConfigSpec.ConfigValue<String> LUCKY_MESSAGE = BUILDER
        .comment("Message when player gets a lucky item")
        .define("luckyMessage", "You've found something. Lucky you!");

    private static final ModConfigSpec.BooleanValue LUCKY_MESSAGE_ENABLE = BUILDER
        .comment("Will the lucky message appear")
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

        LOGGER.info("Someone is going to be lucky ;)");
    }

    private static Set<TagKey<Block>> stringsToTags(List<? extends String> strings) {
        return Optional.ofNullable(strings)
            .orElse(Collections.emptyList())
            .stream()
            .map(ResourceLocation::parse)
            .map(BlockTags::create)
            .collect(Collectors.toSet());
    }

    private static Set<ResourceLocation> stringsToLocations(List<? extends String> strings) {
        return Optional.ofNullable(strings)
            .orElse(Collections.emptyList())
            .stream()
            .map(ResourceLocation::parse)
            .collect(Collectors.toSet());
    }

    private static List<Item> stringsToItems(List<? extends String> strings) {
        return Optional.ofNullable(strings)
            .orElse(Collections.emptyList())
            .stream()
            .map(ResourceLocation::parse)
            .map(BuiltInRegistries.ITEM::get)
            .collect(Collectors.toSet())
            .stream()
            .toList();
    }

}
