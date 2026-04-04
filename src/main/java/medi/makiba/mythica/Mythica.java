package medi.makiba.mythica;

import org.slf4j.Logger;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.mojang.logging.LogUtils;

import medi.makiba.mythica.compat.create.CreateCompat;
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
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(Mythica.MODID)
public class Mythica {
    public static final String MODID = "mythica";
    public static final Logger LOGGER = LogUtils.getLogger();


    public Mythica(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onConfigLoading);
        modEventBus.addListener(this::onConfigReloading);

        MythicaBlocks.BLOCKS.register(modEventBus);
        MythicaItems.ITEMS.register(modEventBus);

        MythicaPointOfInterests.POI.register(modEventBus);
        MythicaSoundEvents.SOUND_EVENTS.register(modEventBus);
        MythicaParticleTypes.PARTICLES.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, MythicaConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.SERVER, MythicaConfig.SERVER_SPEC);
    }

  private void commonSetup(FMLCommonSetupEvent event) {
        if (ModList.get().isLoaded("create")) {
            LOGGER.info("Create detected, loading compat");
            CreateCompat.register();
        }
    }

    private void onConfigLoading(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == MythicaConfig.SPEC && ModList.get().isLoaded("terrablender")) {
            syncTerrablenderConfig();
        }
    }

    private void onConfigReloading(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == MythicaConfig.SPEC && ModList.get().isLoaded("terrablender")) {
            syncTerrablenderConfig();
        }
    }

    private void syncTerrablenderConfig() {
        try {
            Class<?> syncClass = Class.forName("medi.makiba.mythica.compat.terrablender.MythicaTerrablenderSync");
            Method syncMethod = syncClass.getMethod("onCommonConfigLoaded");
            syncMethod.invoke(null);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException e) {
            LOGGER.warn("Terrablender compatibility is unavailable, skipping Mythica config sync.", e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof LinkageError linkageError) {
                LOGGER.warn("Terrablender compatibility is unavailable, skipping Mythica config sync.", linkageError);
                return;
            }
            throw new RuntimeException("Failed to synchronize Terrablender config", cause);
        } catch (LinkageError e) {
            LOGGER.warn("Terrablender compatibility is unavailable, skipping Mythica config sync.", e);
        }
    }
}