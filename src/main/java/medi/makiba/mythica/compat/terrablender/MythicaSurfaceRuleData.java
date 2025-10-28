package medi.makiba.mythica.compat.terrablender;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;

import medi.makiba.mythica.Mythica;
import medi.makiba.mythica.mixin.terrablender.SurfaceRuleManagerAccessor;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import terrablender.api.SurfaceRuleManager;
import terrablender.api.SurfaceRuleManager.RuleCategory;
import terrablender.api.SurfaceRuleManager.RuleStage;
import terrablender.worldgen.surface.NamespacedSurfaceRuleSource;

public class MythicaSurfaceRuleData {
    private static final SurfaceRules.RuleSource AIR;
    private static final SurfaceRules.RuleSource BEDROCK;
    private static final SurfaceRules.RuleSource WHITE_TERRACOTTA;
    private static final SurfaceRules.RuleSource ORANGE_TERRACOTTA;
    private static final SurfaceRules.RuleSource TERRACOTTA;
    private static final SurfaceRules.RuleSource RED_SAND;
    private static final SurfaceRules.RuleSource RED_SANDSTONE;
    private static final SurfaceRules.RuleSource STONE;
    private static final SurfaceRules.RuleSource DEEPSLATE;
    private static final SurfaceRules.RuleSource DIRT;
    private static final SurfaceRules.RuleSource PODZOL;
    private static final SurfaceRules.RuleSource COARSE_DIRT;
    private static final SurfaceRules.RuleSource MYCELIUM;
    private static final SurfaceRules.RuleSource GRASS_BLOCK;
    private static final SurfaceRules.RuleSource CALCITE;
    private static final SurfaceRules.RuleSource GRAVEL;
    private static final SurfaceRules.RuleSource SAND;
    private static final SurfaceRules.RuleSource SANDSTONE;
    private static final SurfaceRules.RuleSource PACKED_ICE;
    private static final SurfaceRules.RuleSource SNOW_BLOCK;
    private static final SurfaceRules.RuleSource MUD;
    private static final SurfaceRules.RuleSource POWDER_SNOW;
    private static final SurfaceRules.RuleSource ICE;
    private static final SurfaceRules.RuleSource WATER;

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }

    private static final ResourceKey<Biome> MYTHICA_BEACH = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(Mythica.MODID, "beach"));
    private static final ResourceKey<Biome> MYTHICA_DEEP_FROZEN_OCEAN = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(Mythica.MODID, "deep_frozen_ocean"));
    private static final ResourceKey<Biome> MYTHICA_DEEP_LUKEWARM_OCEAN = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(Mythica.MODID, "deep_lukewarm_ocean"));
    private static final ResourceKey<Biome> MYTHICA_DRIPSTONE_CAVES = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(Mythica.MODID, "dripstone_caves"));
    private static final ResourceKey<Biome> MYTHICA_FROZEN_OCEAN = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(Mythica.MODID, "frozen_ocean"));
    private static final ResourceKey<Biome> MYTHICA_LUKEWARM_OCEAN = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(Mythica.MODID, "lukewarm_ocean"));
    private static final ResourceKey<Biome> MYTHICA_SNOWY_BEACH = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(Mythica.MODID, "snowy_beach"));
    private static final ResourceKey<Biome> MYTHICA_STONY_SHORE = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(Mythica.MODID, "stony_shore"));
    private static final ResourceKey<Biome> MYTHICA_WARM_OCEAN = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(Mythica.MODID, "warm_ocean"));

    public static SurfaceRules.RuleSource mythica() {
        SurfaceRules.ConditionSource isBlockAboveY97WithVariationAbove = SurfaceRules
                .yBlockCheck(VerticalAnchor.absolute(97), 2);
        SurfaceRules.ConditionSource isBlockAboveY256 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(256), 0);
        SurfaceRules.ConditionSource isSurfaceAbove63WithVariationBelow = SurfaceRules
                .yStartCheck(VerticalAnchor.absolute(63), -1);
        SurfaceRules.ConditionSource isSurfaceAboveY74WithVariationAbove = SurfaceRules
                .yStartCheck(VerticalAnchor.absolute(74), 1);
        SurfaceRules.ConditionSource isBlockAboveY60 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(60), 0);
        SurfaceRules.ConditionSource isBlockAboveY62 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(62), 0);
        SurfaceRules.ConditionSource isBlockAboveY63 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(63), 0);
        SurfaceRules.ConditionSource isInOrAboveShallowWater = SurfaceRules.waterBlockCheck(-1, 0);
        SurfaceRules.ConditionSource isAboveWater = SurfaceRules.waterBlockCheck(0, 0);
        SurfaceRules.ConditionSource isInOrAboveDeepWaterWithVariationBelow = SurfaceRules.waterStartCheck(-6, -1);
        SurfaceRules.ConditionSource isHole = SurfaceRules.hole();
        SurfaceRules.ConditionSource isFrozenOcean = SurfaceRules.isBiome(Biomes.FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN,
                MYTHICA_FROZEN_OCEAN, MYTHICA_DEEP_FROZEN_OCEAN);
        SurfaceRules.ConditionSource isSteep = SurfaceRules.steep();
        SurfaceRules.RuleSource surfaceGrassOrDirtIfSubmerged = SurfaceRules
                .sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(isAboveWater, GRASS_BLOCK), DIRT });
        SurfaceRules.RuleSource sandOrSandstoneCeiling = SurfaceRules.sequence(
                new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, SANDSTONE), SAND });
        SurfaceRules.RuleSource gravelOrStoneCeiling = SurfaceRules.sequence(
                new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, STONE), GRAVEL });
        SurfaceRules.ConditionSource isSandyShoreOrOcean = SurfaceRules
                .isBiome(Biomes.WARM_OCEAN, Biomes.BEACH, Biomes.SNOWY_BEACH, MYTHICA_WARM_OCEAN, MYTHICA_BEACH,
                        MYTHICA_SNOWY_BEACH);
        SurfaceRules.ConditionSource isDesert = SurfaceRules.isBiome(Biomes.DESERT);
        SurfaceRules.RuleSource onAndUnderFloorSurfaceRules = SurfaceRules
                .sequence(
                        new SurfaceRules.RuleSource[] {
                                SurfaceRules
                                        .ifTrue(SurfaceRules.isBiome(Biomes.STONY_PEAKS),
                                                SurfaceRules
                                                        .sequence(new SurfaceRules.RuleSource[] {
                                                                SurfaceRules.ifTrue(SurfaceRules.noiseCondition(
                                                                        Noises.CALCITE, -0.0125, 0.0125), CALCITE),
                                                                STONE })),
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.STONY_SHORE, MYTHICA_STONY_SHORE),
                                        SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(
                                                SurfaceRules.noiseCondition(Noises.GRAVEL, -0.05, 0.05),
                                                gravelOrStoneCeiling), STONE })),
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WINDSWEPT_HILLS),
                                        SurfaceRules.ifTrue(surfaceNoiseAbove(1.0), STONE)),
                                SurfaceRules.ifTrue(isSandyShoreOrOcean, sandOrSandstoneCeiling),
                                SurfaceRules.ifTrue(isDesert, sandOrSandstoneCeiling), SurfaceRules.ifTrue(
                                        SurfaceRules.isBiome(Biomes.DRIPSTONE_CAVES, MYTHICA_DRIPSTONE_CAVES), STONE) });
        SurfaceRules.RuleSource powderedSnowSmallPatches = SurfaceRules.ifTrue(
                SurfaceRules.noiseCondition(Noises.POWDER_SNOW, 0.45, 0.58),
                SurfaceRules.ifTrue(isAboveWater, POWDER_SNOW));
        SurfaceRules.RuleSource powderedSnowLargePatches = SurfaceRules.ifTrue(
                SurfaceRules.noiseCondition(Noises.POWDER_SNOW, 0.35, 0.6),
                SurfaceRules.ifTrue(isAboveWater, POWDER_SNOW));
        SurfaceRules.RuleSource underFloorSurfaceRules = SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
                SurfaceRules.ifTrue(
                        SurfaceRules.isBiome(Biomes.FROZEN_PEAKS),
                        SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(isSteep, PACKED_ICE),
                                SurfaceRules.ifTrue(
                                        SurfaceRules.noiseCondition(Noises.PACKED_ICE, -0.5, 0.2), PACKED_ICE),
                                SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.ICE, -0.0625, 0.025), ICE),
                                SurfaceRules.ifTrue(isAboveWater, SNOW_BLOCK) })),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.SNOWY_SLOPES),
                        SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(isSteep, STONE),
                                powderedSnowSmallPatches, SurfaceRules.ifTrue(isAboveWater, SNOW_BLOCK) })),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.JAGGED_PEAKS), STONE),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.GROVE),
                        SurfaceRules.sequence(new SurfaceRules.RuleSource[] { powderedSnowSmallPatches, DIRT })),
                onAndUnderFloorSurfaceRules,
                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WINDSWEPT_SAVANNA),
                        SurfaceRules.ifTrue(surfaceNoiseAbove(1.75), STONE)),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WINDSWEPT_GRAVELLY_HILLS),
                        SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
                                SurfaceRules.ifTrue(surfaceNoiseAbove(2.0), gravelOrStoneCeiling),
                                SurfaceRules.ifTrue(surfaceNoiseAbove(1.0), STONE),
                                SurfaceRules.ifTrue(surfaceNoiseAbove(-1.0), DIRT), gravelOrStoneCeiling })),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.MANGROVE_SWAMP), MUD), DIRT });
        SurfaceRules.RuleSource shallowFloorSurfaceRules = SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
                SurfaceRules.ifTrue(
                        SurfaceRules.isBiome(Biomes.FROZEN_PEAKS),
                        SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(isSteep, PACKED_ICE),
                                SurfaceRules.ifTrue(
                                        SurfaceRules.noiseCondition(Noises.PACKED_ICE, 0.0, 0.2), PACKED_ICE),
                                SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.ICE, 0.0, 0.025), ICE),
                                SurfaceRules.ifTrue(isAboveWater, SNOW_BLOCK) })),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.SNOWY_SLOPES),
                        SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(isSteep, STONE),
                                powderedSnowLargePatches, SurfaceRules.ifTrue(isAboveWater, SNOW_BLOCK) })),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.JAGGED_PEAKS),
                        SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(isSteep, STONE),
                                SurfaceRules.ifTrue(isAboveWater, SNOW_BLOCK) })),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.GROVE),
                        SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
                                powderedSnowLargePatches, SurfaceRules.ifTrue(isAboveWater, SNOW_BLOCK) })),
                onAndUnderFloorSurfaceRules,
                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WINDSWEPT_SAVANNA),
                        SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
                                SurfaceRules.ifTrue(surfaceNoiseAbove(1.75), STONE),
                                SurfaceRules.ifTrue(surfaceNoiseAbove(-0.5), COARSE_DIRT) })),
                SurfaceRules
                        .ifTrue(SurfaceRules.isBiome(Biomes.WINDSWEPT_GRAVELLY_HILLS),
                                SurfaceRules
                                        .sequence(new SurfaceRules.RuleSource[] {
                                                SurfaceRules.ifTrue(surfaceNoiseAbove(2.0), gravelOrStoneCeiling),
                                                SurfaceRules.ifTrue(surfaceNoiseAbove(1.0), STONE),
                                                SurfaceRules.ifTrue(surfaceNoiseAbove(-1.0),
                                                        surfaceGrassOrDirtIfSubmerged),
                                                gravelOrStoneCeiling })),
                SurfaceRules.ifTrue(
                        SurfaceRules.isBiome(Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA),
                        SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
                                SurfaceRules.ifTrue(surfaceNoiseAbove(1.75), COARSE_DIRT),
                                SurfaceRules.ifTrue(surfaceNoiseAbove(-0.95), PODZOL) })),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.ICE_SPIKES),
                        SurfaceRules.ifTrue(isAboveWater, SNOW_BLOCK)),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.MANGROVE_SWAMP), MUD),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.MUSHROOM_FIELDS), MYCELIUM),
                surfaceGrassOrDirtIfSubmerged });
        SurfaceRules.ConditionSource isSuitableSurfaceNoiseLower = SurfaceRules.noiseCondition(Noises.SURFACE, -0.909,
                -0.5454);
        SurfaceRules.ConditionSource isSuitableSurfaceNoiseMid = SurfaceRules.noiseCondition(Noises.SURFACE, -0.1818,
                0.1818);
        SurfaceRules.ConditionSource isSuitableSurfaceNoiseUpper = SurfaceRules.noiseCondition(Noises.SURFACE, 0.5454,
                0.909);
        SurfaceRules.RuleSource surfaceRules = SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                        SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WOODED_BADLANDS),
                                        SurfaceRules.ifTrue(isBlockAboveY97WithVariationAbove,
                                                SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
                                                        SurfaceRules.ifTrue(isSuitableSurfaceNoiseLower, COARSE_DIRT),
                                                        SurfaceRules.ifTrue(isSuitableSurfaceNoiseMid, COARSE_DIRT),
                                                        SurfaceRules.ifTrue(isSuitableSurfaceNoiseUpper, COARSE_DIRT),
                                                        surfaceGrassOrDirtIfSubmerged }))),
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.SWAMP),
                                        SurfaceRules.ifTrue(isBlockAboveY62, SurfaceRules.ifTrue(
                                                SurfaceRules.not(isBlockAboveY63),
                                                SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.SWAMP, 0.0),
                                                        WATER)))),
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.MANGROVE_SWAMP),
                                        SurfaceRules.ifTrue(isBlockAboveY60, SurfaceRules.ifTrue(
                                                SurfaceRules.not(isBlockAboveY63),
                                                SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.SWAMP, 0.0),
                                                        WATER)))) })),
                SurfaceRules.ifTrue(
                        SurfaceRules.isBiome(Biomes.BADLANDS, Biomes.ERODED_BADLANDS, Biomes.WOODED_BADLANDS),
                        SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                                SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
                                        SurfaceRules.ifTrue(isBlockAboveY256, ORANGE_TERRACOTTA), SurfaceRules.ifTrue(
                                                isSurfaceAboveY74WithVariationAbove,
                                                SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules
                                                        .ifTrue(isSuitableSurfaceNoiseLower, TERRACOTTA),
                                                        SurfaceRules.ifTrue(isSuitableSurfaceNoiseMid, TERRACOTTA),
                                                        SurfaceRules.ifTrue(isSuitableSurfaceNoiseUpper, TERRACOTTA),
                                                        SurfaceRules.bandlands() })),
                                        SurfaceRules.ifTrue(isInOrAboveShallowWater,
                                                SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
                                                        SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, RED_SANDSTONE),
                                                        RED_SAND })),
                                        SurfaceRules.ifTrue(SurfaceRules.not(isHole), ORANGE_TERRACOTTA),
                                        SurfaceRules.ifTrue(isInOrAboveDeepWaterWithVariationBelow, WHITE_TERRACOTTA),
                                        gravelOrStoneCeiling })),
                                SurfaceRules
                                        .ifTrue(isSurfaceAbove63WithVariationBelow,
                                                SurfaceRules
                                                        .sequence(new SurfaceRules.RuleSource[] {
                                                                SurfaceRules.ifTrue(isBlockAboveY63,
                                                                        SurfaceRules.ifTrue(SurfaceRules.not(
                                                                                isSurfaceAboveY74WithVariationAbove),
                                                                                ORANGE_TERRACOTTA)),
                                                                SurfaceRules.bandlands() })),
                                SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR,
                                        SurfaceRules.ifTrue(isInOrAboveDeepWaterWithVariationBelow,
                                                WHITE_TERRACOTTA)) })),
                SurfaceRules
                        .ifTrue(SurfaceRules.ON_FLOOR,
                                SurfaceRules.ifTrue(isInOrAboveShallowWater,
                                        SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
                                                SurfaceRules.ifTrue(isFrozenOcean,
                                                        SurfaceRules.ifTrue(isHole,
                                                                SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
                                                                        SurfaceRules.ifTrue(isAboveWater, AIR),
                                                                        SurfaceRules.ifTrue(SurfaceRules.temperature(),
                                                                                ICE),
                                                                        WATER }))),
                                                shallowFloorSurfaceRules }))),
                SurfaceRules
                        .ifTrue(isInOrAboveDeepWaterWithVariationBelow,
                                SurfaceRules
                                        .sequence(new SurfaceRules.RuleSource[] {
                                                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                                                        SurfaceRules.ifTrue(isFrozenOcean,
                                                                SurfaceRules.ifTrue(isHole, WATER))),
                                                SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, underFloorSurfaceRules),
                                                SurfaceRules.ifTrue(isSandyShoreOrOcean,
                                                        SurfaceRules.ifTrue(SurfaceRules.DEEP_UNDER_FLOOR, SANDSTONE)),
                                                SurfaceRules.ifTrue(isDesert,
                                                        SurfaceRules.ifTrue(SurfaceRules.VERY_DEEP_UNDER_FLOOR,
                                                                SANDSTONE)) })),
                SurfaceRules
                        .ifTrue(SurfaceRules.ON_FLOOR,
                                SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
                                        SurfaceRules.ifTrue(
                                                SurfaceRules.isBiome(Biomes.FROZEN_PEAKS, Biomes.JAGGED_PEAKS),
                                                STONE),
                                        SurfaceRules.ifTrue(
                                                SurfaceRules.isBiome(Biomes.WARM_OCEAN,
                                                        Biomes.LUKEWARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN,
                                                        MYTHICA_WARM_OCEAN, MYTHICA_LUKEWARM_OCEAN,
                                                        MYTHICA_DEEP_LUKEWARM_OCEAN),
                                                sandOrSandstoneCeiling),
                                        gravelOrStoneCeiling })) });
        ImmutableList.Builder<SurfaceRules.RuleSource> builder = ImmutableList.builder();
        builder.addAll(SurfaceRuleManager.getDefaultSurfaceRuleAdditionsForStage(RuleCategory.OVERWORLD,
                RuleStage.BEFORE_BEDROCK));

        builder.add(SurfaceRules.ifTrue(
                SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)),
                BEDROCK));

        List<SurfaceRules.RuleSource> afterBedrockRules = SurfaceRuleManager
                .getDefaultSurfaceRuleAdditionsForStage(RuleCategory.OVERWORLD, RuleStage.AFTER_BEDROCK);
        if (!afterBedrockRules.isEmpty()) {
            ImmutableList.Builder<SurfaceRules.RuleSource> newSurfaceRules = ImmutableList.builder();
            newSurfaceRules.addAll(afterBedrockRules);
            newSurfaceRules.add(surfaceRules);
            surfaceRules = SurfaceRules.sequence((SurfaceRules.RuleSource[]) newSurfaceRules.build().toArray((x$0) -> {
                return new SurfaceRules.RuleSource[x$0];
            }));
        }

        SurfaceRules.RuleSource surfaceRulesWithPreliminarySurfaceCheck = SurfaceRules
                .ifTrue(SurfaceRules.abovePreliminarySurface(), surfaceRules);
        builder.add(surfaceRulesWithPreliminarySurfaceCheck);
        builder.add(SurfaceRules.ifTrue(
                SurfaceRules.verticalGradient("deepslate", VerticalAnchor.absolute(0), VerticalAnchor.absolute(8)),
                DEEPSLATE));
        return SurfaceRules.sequence((SurfaceRules.RuleSource[]) builder.build().toArray((count) -> {
            return new SurfaceRules.RuleSource[count];
        }));
    }

    private static SurfaceRules.ConditionSource surfaceNoiseAbove(double value) {
        return SurfaceRules.noiseCondition(Noises.SURFACE, value / 8.25, Double.MAX_VALUE);
    }

    static {
        AIR = makeStateRule(Blocks.AIR);
        BEDROCK = makeStateRule(Blocks.BEDROCK);
        WHITE_TERRACOTTA = makeStateRule(Blocks.WHITE_TERRACOTTA);
        ORANGE_TERRACOTTA = makeStateRule(Blocks.ORANGE_TERRACOTTA);
        TERRACOTTA = makeStateRule(Blocks.TERRACOTTA);
        RED_SAND = makeStateRule(Blocks.RED_SAND);
        RED_SANDSTONE = makeStateRule(Blocks.RED_SANDSTONE);
        STONE = makeStateRule(Blocks.STONE);
        DEEPSLATE = makeStateRule(Blocks.DEEPSLATE);
        DIRT = makeStateRule(Blocks.DIRT);
        PODZOL = makeStateRule(Blocks.PODZOL);
        COARSE_DIRT = makeStateRule(Blocks.COARSE_DIRT);
        MYCELIUM = makeStateRule(Blocks.MYCELIUM);
        GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
        CALCITE = makeStateRule(Blocks.CALCITE);
        GRAVEL = makeStateRule(Blocks.GRAVEL);
        SAND = makeStateRule(Blocks.SAND);
        SANDSTONE = makeStateRule(Blocks.SANDSTONE);
        PACKED_ICE = makeStateRule(Blocks.PACKED_ICE);
        SNOW_BLOCK = makeStateRule(Blocks.SNOW_BLOCK);
        MUD = makeStateRule(Blocks.MUD);
        POWDER_SNOW = makeStateRule(Blocks.POWDER_SNOW);
        ICE = makeStateRule(Blocks.ICE);
        WATER = makeStateRule(Blocks.WATER);
    }

    public static SurfaceRules.RuleSource getMythicaNamespacedRules(SurfaceRuleManager.RuleCategory category,
            SurfaceRules.RuleSource fallback) {
        ImmutableMap.Builder<String, SurfaceRules.RuleSource> builder = ImmutableMap.builder();
        builder.put("mythica", SurfaceRuleManager.getDefaultSurfaceRules(category));
        builder.putAll(SurfaceRuleManagerAccessor.getSurfaceRules().get(category));
        return new NamespacedSurfaceRuleSource(fallback, builder.build());
    }
}