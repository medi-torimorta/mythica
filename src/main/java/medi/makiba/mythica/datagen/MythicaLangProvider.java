package medi.makiba.mythica.datagen;

import medi.makiba.mythica.Mythica;
import medi.makiba.mythica.registry.MythicaBlocks;
import medi.makiba.mythica.registry.MythicaItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;


public class MythicaLangProvider extends LanguageProvider {

    public MythicaLangProvider(PackOutput output) {
        super(output, Mythica.MODID, "ja_jp");
    }

    protected void addSubtitle(String category, String subtitleName, String name) {
		add("subtitles." + category + "." + subtitleName, name);
	}

    protected void addConfig(String configName, String name) {
		add("config.mythica." + configName, name);
	}

    protected void addBiome(String biomeName, String name) {
        add("biome.mythica." + biomeName, name);
    }
    
    protected void addTag(String tagName, String name) {
        add("tag.mythica." + tagName, name);
    }

    
    @Override
    protected void addTranslations() {

        this.add("mythica.configuration.title", "設定");
        this.add("mythica.configuration.section.mythica.server.toml.title", "サーバー設定");
        this.add("mythica.configuration.section.mythica.common.toml.title", "共通設定");
        this.add("mythica.configuration.mythica_general_settings", "全般設定");
        this.add("mythica.configuration.terrablender_biome_settings", "Terrablender設定");

        this.addItem(MythicaItems.MYTHIC_DUST, "神秘の粉");

        this.addBlock(MythicaBlocks.MYTHICA_PORTAL, "ミシカポータル");

        this.addSubtitle("block","mythica_portal.ambient", "ミシカポータルがきらめく");
        this.addSubtitle("block", "mythica_portal.activate", "ミシカポータルが起動する");
        this.addSubtitle("block", "mythica_portal.travel", "ミシカポータルを通過する");

        this.addConfig("return_portal_frame_block_id", "帰還ポータルの枠ブロック");
        this.addConfig("seed_mode", "シード設定モード");
        this.addConfig("seed_value", "シード設定値");
        this.addConfig("modded_biome_copy_mode", "MODバイオームのコピーモード");
        this.addConfig("mythica_region_size", "各リージョンの大きさ");
        this.addConfig("vanilla_region_weight", "バニラリージョンのウェイト");
        this.addConfig("mod_blacklist", "バイオームMODブラックリスト");

        this.addBiome("beach", "砂浜");
        this.addBiome("cold_ocean", "冷たい海");
        this.addBiome("deep_cold_ocean", "冷たい深海");
        this.addBiome("deep_frozen_ocean", "凍った深海");
        this.addBiome("deep_lukewarm_ocean", "ぬるい深海");
        this.addBiome("deep_ocean", "深海");
        this.addBiome("frozen_ocean", "凍った海");
        this.addBiome("frozen_river", "凍った川");
        this.addBiome("lukewarm_ocean", "ぬるい海");
        this.addBiome("ocean", "海");
        this.addBiome("river", "河川");
        this.addBiome("snowy_beach", "雪の砂浜");
        this.addBiome("stony_shore", "石だらけの海岸");
        this.addBiome("warm_ocean", "暖かい海");

        this.addTag("is_mythica", "ミシカのバイオーム");
        this.addTag("is_beach", "ミシカの砂浜");
        this.addTag("is_ocean", "ミシカの海");
        this.addTag("is_river", "ミシカの河川");
    }
}