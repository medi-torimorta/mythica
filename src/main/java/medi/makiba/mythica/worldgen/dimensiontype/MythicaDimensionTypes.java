package medi.makiba.mythica.worldgen.dimensiontype;

import medi.makiba.mythica.Mythica;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.DimensionType;

public class MythicaDimensionTypes {
    public static final ResourceKey<DimensionType> MYTHICA_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.fromNamespaceAndPath(Mythica.MODID, "mythica_dimtype"));
}