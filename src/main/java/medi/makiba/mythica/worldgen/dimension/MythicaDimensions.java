
package medi.makiba.mythica.worldgen.dimension;

import medi.makiba.mythica.Mythica;
import medi.makiba.mythica.MythicaConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class MythicaDimensions {
    public static final ResourceKey<Level> MYTHICA_DIM = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(Mythica.MODID, "mythica"));
    public static Long getSeed(Long worldSeed){
        return switch (MythicaConfig.SEED_MODE.get()) {
            case WORLD_SEED -> worldSeed;
            case VALUE -> MythicaConfig.SEED_VALUE.get();
            case OFFSET_VALUE -> worldSeed + MythicaConfig.SEED_VALUE.get();
        };
    }
}