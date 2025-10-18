package medi.makiba.mythica.compat.terrablender;

import java.util.function.Consumer;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import terrablender.api.Region;

public class DefaultMythicaRegion extends Region{
   public static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath("mythica", "mythica");

   public DefaultMythicaRegion(int weight) {
      super(LOCATION, MythicaRegionType.MYTHICA, weight);
   }

   @Override
   public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
      (new OverworldBiomeBuilder()).addBiomes(mapper);
   }
}
