
package medi.makiba.mythica.worldgen.dimension;

import medi.makiba.mythica.Mythica;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class MythicaDimensions {
    public static final ResourceKey<Level> MYTHICA_DIM = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(Mythica.MODID, "mythica"));
}