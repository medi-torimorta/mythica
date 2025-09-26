package medi.makiba.mythica.datagen;

import medi.makiba.mythica.Mythica;
import medi.makiba.mythica.registry.MythicaItems;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class MythicaItemModelProvider extends ItemModelProvider{

    public MythicaItemModelProvider(net.minecraft.data.PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Mythica.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(MythicaItems.MYTHIC_DUST.get());
    }
}