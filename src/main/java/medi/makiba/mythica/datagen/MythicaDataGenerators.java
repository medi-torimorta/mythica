package medi.makiba.mythica.datagen;

import medi.makiba.mythica.Mythica;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;


@EventBusSubscriber(modid = Mythica.MODID)
public class MythicaDataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(event.includeClient(), new MythicaSoundDefinitions(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new MythicaLangProvider(output));
        generator.addProvider(event.includeClient(), new MythicaItemModelProvider(output, existingFileHelper));
    }
}
