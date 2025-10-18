package medi.makiba.mythica;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.ModConfigSpec;


public class MythicaConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<String> RETURN_PORTAL_FRAME_BLOCK_ID = BUILDER
        .comment("""
            Determines what block the game will generate Mythica return portals out of
            Use the block tag mythica:portal_frame_blocks to determine what blocks portals can be built with
            If value entered here is not a valid block it will default to generating minecraft:reinforced_deepslate""")
        .translation("config.mythica.return_portal_frame_block_id")
        .define("Return Portal Frame Block ID", BuiltInRegistries.BLOCK.getKey(Blocks.REINFORCED_DEEPSLATE).toString());

    public static final ModConfigSpec.ConfigValue<Integer> MYTHICA_REGION_SIZE = BUILDER
        .comment("""
            Determines the size of the Mythica region when Terrablender is installed""")
        .translation("config.mythica.mythica_region_size")
        .defineInRange("Mythica Region Size", 3, 2, 6);

    public static final ModConfigSpec.ConfigValue<Integer> OVERWORLD_REGION_WEIGHT = BUILDER
        .comment("""
            Determines the weight of the vanilla overworld region when Terrablender is installed""")
        .translation("config.mythica.overworld_region_weight")
        .defineInRange("Vanilla Overworld Region Weight", 10, 1, Integer.MAX_VALUE);

    static final ModConfigSpec SPEC = BUILDER.build();
}
