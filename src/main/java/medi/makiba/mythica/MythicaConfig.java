package medi.makiba.mythica;

import java.util.List;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;


public class MythicaConfig {
    static final ModConfigSpec SPEC;
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<String> RETURN_PORTAL_FRAME_BLOCK_ID;
    public static final ModConfigSpec.ConfigValue<Integer> MYTHICA_REGION_SIZE;
    public static final ModConfigSpec.ConfigValue<Integer> VANILLA_REGION_WEIGHT;
    public static final ModConfigSpec.ConfigValue<ModdedBiomeCopyModes> MODDED_BIOME_COPY_MODE;
    public static final ConfigValue<List<? extends String>> MOD_BLACKLIST;

    public enum ModdedBiomeCopyModes{
        NO_COPY,
        COPY,
        COPY_REMOVE
    }


    static {
        BUILDER.push("mythica_general_settings");
        RETURN_PORTAL_FRAME_BLOCK_ID = BUILDER
            .comment("""
                Determines what block the game will generate Mythica return portals out of
                Use the block tag mythica:portal_frame_blocks to determine what blocks portals can be built with
                If value entered here is not a valid block it will default to generating minecraft:reinforced_deepslate""")
            .translation("config.mythica.return_portal_frame_block_id")
            .define("Return Portal Frame Block ID", BuiltInRegistries.BLOCK.getKey(Blocks.REINFORCED_DEEPSLATE).toString());
        BUILDER.pop();
        BUILDER.push("terrablender_biome_settings");
        MODDED_BIOME_COPY_MODE = BUILDER
            .comment("""
                Determines how modded biomes from other mods using terrablender are handled in mythica
                NO_COPY will not copy any modded biomes to mythica, resulting in only vanilla biomes generating
                COPY will simply copy modded biomes from the overworld to mythica
                COPY_REMOVE will transfer modded biomes to mythica, removing them from the overworld""")
            .translation("config.mythica.modded_biome_copy_mode")
            .gameRestart()
            .defineEnum("Modded Biome Copy Mode", ModdedBiomeCopyModes.COPY);
        MYTHICA_REGION_SIZE = BUILDER
            .comment("""
                Determines the size of regions from each mods that use terrablender, in mythica""")
            .translation("config.mythica.mythica_region_size")
            .gameRestart()
            .defineInRange("Mythica Region Size", 3, 2, 6);

        VANILLA_REGION_WEIGHT = BUILDER
            .comment("""
                Determines the weight of the vanilla region in mythica""")
            .translation("config.mythica.vanilla_region_weight")
            .gameRestart()
            .defineInRange("Vanilla Region Weight", 10, 1, Integer.MAX_VALUE);
        MOD_BLACKLIST = BUILDER
            .comment("""
                A list of mod ids to blacklist from having their biomes copied to mythica
                Example: ["modid1", "modid2"]
                if a region is registered under a namespace other than the modid, that should be used instead""")
            .translation("config.mythica.mod_blacklist")
            .gameRestart()
            .defineListAllowEmpty("Mod Blacklist", List.of(), () -> "", o -> o instanceof String);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    
}
