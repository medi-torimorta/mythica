package medi.makiba.mythica.mixin.terrablender;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.ImmutableList;

import medi.makiba.mythica.compat.terrablender.DimensionTypeTags;
import medi.makiba.mythica.compat.terrablender.MythicaRegionType;
import medi.makiba.mythica.compat.terrablender.MythicaRuleCategory;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import terrablender.api.RegionType;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;
import terrablender.core.TerraBlender;
import terrablender.util.LevelUtils;
import terrablender.worldgen.IExtendedBiomeSource;
import terrablender.worldgen.IExtendedNoiseGeneratorSettings;
import terrablender.worldgen.IExtendedParameterList;

@Mixin(value = LevelUtils.class)
public class LevelUtilsMixin {

    @Shadow
    public static boolean shouldApplyToBiomeSource(BiomeSource biomeSource) {
        return false;
    }

    @Shadow
    public static RegionType getRegionTypeForDimension(Holder<DimensionType> dimensionType) {
        return null;
    }

    @Inject(at = @At("HEAD"), cancellable = true, method = "getRegionTypeForDimension(Lnet/minecraft/core/Holder;)Lterrablender/api/RegionType;")
    private static void addMythica(Holder<DimensionType> dimensionType, CallbackInfoReturnable<RegionType> cir) {
        if (dimensionType.is(DimensionTypeTags.MYTHICA_REGIONS)) {
            cir.setReturnValue(MythicaRegionType.MYTHICA);
        }
    }

    @Inject(at = @At("HEAD"), method = "initializeBiomes(Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/core/Holder;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/level/chunk/ChunkGenerator;J)V", remap = false, cancellable = true)
    private static void initializeMythicaBiomes(RegistryAccess registryAccess, Holder<DimensionType> dimensionType, ResourceKey<LevelStem> levelResourceKey, ChunkGenerator chunkGenerator, long seed, CallbackInfo ci) {
        RegionType regionType = getRegionTypeForDimension(dimensionType);
        if (regionType == MythicaRegionType.MYTHICA) {
            if (shouldApplyToBiomeSource(chunkGenerator.getBiomeSource())) {
                NoiseBasedChunkGenerator noiseBasedChunkGenerator = (NoiseBasedChunkGenerator) chunkGenerator;

                NoiseGeneratorSettings generatorSettings = noiseBasedChunkGenerator.generatorSettings().value();

                MultiNoiseBiomeSource biomeSource = (MultiNoiseBiomeSource) chunkGenerator.getBiomeSource();
                IExtendedBiomeSource biomeSourceEx = (IExtendedBiomeSource) biomeSource;
                SurfaceRuleManager.RuleCategory ruleCategory = MythicaRuleCategory.MYTHICA;
                ((IExtendedNoiseGeneratorSettings) (Object) generatorSettings).setRuleCategory(ruleCategory);

                Climate.ParameterList parameters = biomeSource.parameters();
                IExtendedParameterList parametersEx = (IExtendedParameterList) parameters;

                parametersEx.initializeForTerraBlender(registryAccess, regionType, seed);

                Registry<Biome> biomeRegistry = registryAccess.registryOrThrow(Registries.BIOME);
                ImmutableList.Builder<Holder<Biome>> builder = ImmutableList.builder();
                Regions.get(regionType).forEach(region -> region.addBiomes(
                    biomeRegistry, pair -> builder.add(biomeRegistry.getHolderOrThrow(pair.getSecond()))));
                biomeSourceEx.appendDeferredBiomesList(builder.build());

                TerraBlender.LOGGER.info(String.format("Mythica: Initialized TerraBlender biomes for level stem %s", levelResourceKey.location()));
                ci.cancel();
            }
        }
    }
}
