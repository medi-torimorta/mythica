package medi.makiba.mythica.compat.terrablender;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.dimension.DimensionType;
import terrablender.core.TerraBlender;

public class DimensionTypeTags {
    public static final TagKey<DimensionType> MYTHICA_REGIONS = create("mythica_regions");

    private static TagKey<DimensionType> create(String id) {
        return TagKey.create(Registries.DIMENSION_TYPE, ResourceLocation.fromNamespaceAndPath(TerraBlender.MOD_ID, id));
    }
}