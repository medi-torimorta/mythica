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

    
    @Override
    protected void addTranslations() {

        this.add("mythica.configuration.title", "設定");
        this.add("mythica.configuration.section.mythica.common.toml", "ミシカ");
        this.add("mythica.configuration.section.mythica.common.toml.title", "ミシカ");

        this.addItem(MythicaItems.MYTHIC_DUST, "神秘の粉");

        this.addBlock(MythicaBlocks.MYTHICA_PORTAL, "ミシカポータル");

        this.addSubtitle("block","mythica_portal.ambient", "ミシカポータルがきらめく");
        this.addSubtitle("block", "mythica_portal.activate", "ミシカポータルが起動する");
        this.addSubtitle("block", "mythica_portal.travel", "ミシカポータルを通過する");

        this.addConfig("return_portal_frame_block_id", "帰還ポータルの枠ブロック");
    }
}


