package medi.makiba.mythica.datagen;

import medi.makiba.mythica.Mythica;
import medi.makiba.mythica.registry.MythicaSoundEvents;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class MythicaSoundDefinitions extends SoundDefinitionsProvider {
    public MythicaSoundDefinitions(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Mythica.MODID, existingFileHelper);
    }

    @Override
    public void registerSounds() {
        this.add(MythicaSoundEvents.MYTHICA_PORTAL_AMBIENT, SoundDefinition.definition()
        .with(
            sound("mythica:block/mythica_portal_ambient")
        )
        .subtitle("subtitles.block.mythica_portal.ambient"));
        
        this.add(MythicaSoundEvents.MYTHICA_PORTAL_ACTIVATE, SoundDefinition.definition()
        .with(
            sound("mythica:block/mythica_portal_activate")
        )
        .subtitle("subtitles.block.mythica_portal.activate"));

        this.add(MythicaSoundEvents.MYTHICA_PORTAL_TRAVEL, SoundDefinition.definition()
        .with(
            sound("mythica:block/mythica_portal_travel")
        )
        .subtitle("subtitles.block.mythica_portal.travel"));
    }
}