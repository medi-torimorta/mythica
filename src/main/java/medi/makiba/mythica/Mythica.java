package medi.makiba.mythica;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import medi.makiba.mythica.compat.CreateCompat;
import medi.makiba.mythica.compat.terrablender.ITerraBlenderCompat;
import medi.makiba.mythica.compat.terrablender.TerraBlenderFallbackImpl;
import medi.makiba.mythica.compat.terrablender.implementation.TerraBlenderCompat;
import medi.makiba.mythica.registry.MythicaBlocks;
import medi.makiba.mythica.registry.MythicaItems;
import medi.makiba.mythica.registry.MythicaParticleTypes;
import medi.makiba.mythica.registry.MythicaPointOfInterests;
import medi.makiba.mythica.registry.MythicaSoundEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(Mythica.MODID)
public class Mythica {
    public static final String MODID = "mythica";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static ITerraBlenderCompat TERRABLENDER_COMPAT;


    public Mythica(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        MythicaBlocks.BLOCKS.register(modEventBus);
        MythicaItems.ITEMS.register(modEventBus);

        MythicaPointOfInterests.POI.register(modEventBus);
        MythicaSoundEvents.SOUND_EVENTS.register(modEventBus);
        MythicaParticleTypes.PARTICLES.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, MythicaConfig.SPEC);
    }

  private void commonSetup(FMLCommonSetupEvent event) {
        if (ModList.get().isLoaded("create")) {
            LOGGER.info("Create detected, loading compat");
            CreateCompat.register();
        }
        if (ModList.get().isLoaded("terrablender")) {
            TERRABLENDER_COMPAT = new TerraBlenderCompat();
            LOGGER.info("TerraBlender detected, enabling compatibility.");
        } else {
            TERRABLENDER_COMPAT = new TerraBlenderFallbackImpl();
            LOGGER.info("TerraBlender not detected, compatibility disabled.");
        }
    }
}