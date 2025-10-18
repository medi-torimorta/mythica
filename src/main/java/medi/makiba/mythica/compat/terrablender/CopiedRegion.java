package medi.makiba.mythica.compat.terrablender;

import java.util.function.Consumer;

import com.mojang.datafixers.util.Pair;

import medi.makiba.mythica.Mythica;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;

public class CopiedRegion extends Region {
    Region originalRegion;

    public CopiedRegion(Region originalRegion, ResourceLocation location, int weight) {
        super(ResourceLocation.fromNamespaceAndPath(Mythica.MODID, location.getNamespace() + "_" + location.getPath()), MythicaRegionType.MYTHICA, weight);
        this.originalRegion = originalRegion;
    }
    
    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        originalRegion.addBiomes(registry, mapper);
    }
}
