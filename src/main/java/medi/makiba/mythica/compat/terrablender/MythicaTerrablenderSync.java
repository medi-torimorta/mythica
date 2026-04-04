package medi.makiba.mythica.compat.terrablender;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import medi.makiba.mythica.Mythica;
import medi.makiba.mythica.MythicaConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.SurfaceRules;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;
import terrablender.api.SurfaceRuleManager.RuleCategory;
import terrablender.worldgen.DefaultOverworldRegion;

public final class MythicaTerrablenderSync {
    private static final Map<ResourceLocation, ObservedRegion> OBSERVED_OVERWORLD_REGIONS = new LinkedHashMap<>();
    private static final Map<String, SurfaceRules.RuleSource> OBSERVED_OVERWORLD_SURFACE_RULES = new LinkedHashMap<>();
    private static final Set<String> APPLIED_MYTHICA_SURFACE_RULES = new LinkedHashSet<>();

    private static boolean commonConfigLoaded;
    private static boolean applying;

    private MythicaTerrablenderSync() {
    }

    public static synchronized void observeRegionRegistration(ResourceLocation name, Region region) {
        observeRegionRegistration(name, region, null);
    }

    public static synchronized boolean shouldCancelOriginalRegionRegistration(ResourceLocation name, Region region) {
        return shouldCancelOriginalRegionRegistration(name, region, null);
    }

    public static synchronized boolean shouldCancelOriginalRegionRegistration(ResourceLocation name, Region region, Integer index) {
        if (!commonConfigLoaded || !isObservableOverworldRegion(name, region)) {
            return false;
        }

        if (MythicaConfig.MODDED_BIOME_COPY_MODE.get() != MythicaConfig.ModdedBiomeCopyModes.COPY_REMOVE
            || MythicaConfig.MOD_BLACKLIST.get().contains(name.getNamespace())) {
            return false;
        }

        observeRegionRegistration(name, region, index);
        return true;
    }

    public static synchronized void observeRegionRegistration(ResourceLocation name, Region region, Integer index) {
        if (applying || !isObservableOverworldRegion(name, region)) {
            return;
        }

        ObservedRegion observedRegion = new ObservedRegion(name, region, index);
        OBSERVED_OVERWORLD_REGIONS.put(name, observedRegion);
        if (commonConfigLoaded) {
            applying = true;
            try {
                syncObservedRegion(observedRegion);
            } finally {
                applying = false;
            }
        }
    }

    public static synchronized void observeSurfaceRulesAdded(RuleCategory category, String namespace, SurfaceRules.RuleSource rules) {
        if (applying || category != RuleCategory.OVERWORLD || Mythica.MODID.equals(namespace)) {
            return;
        }

        OBSERVED_OVERWORLD_SURFACE_RULES.put(namespace, rules);
        if (commonConfigLoaded) {
            applying = true;
            try {
                syncSurfaceRule(namespace);
            } finally {
                applying = false;
            }
        }
    }

    public static synchronized void observeSurfaceRulesRemoved(RuleCategory category, String namespace) {
        if (applying || category != RuleCategory.OVERWORLD || Mythica.MODID.equals(namespace)) {
            return;
        }

        OBSERVED_OVERWORLD_SURFACE_RULES.remove(namespace);
        if (commonConfigLoaded) {
            applying = true;
            try {
                syncSurfaceRule(namespace);
            } finally {
                applying = false;
            }
        }
    }

    public static synchronized void onCommonConfigLoaded() {
        commonConfigLoaded = true;
        applyCurrentConfig();
    }

    private static void applyCurrentConfig() {
        if (!commonConfigLoaded || MythicaRegionType.MYTHICA == null || MythicaRuleCategory.MYTHICA == null) {
            return;
        }

        applying = true;
        try {
            syncDefaultRegion();
            syncObservedRegions();
            syncSurfaceRules();
        } finally {
            applying = false;
        }
    }

    private static void syncDefaultRegion() {
        if (isRegionPresent(MythicaRegionType.MYTHICA, DefaultMythicaRegion.LOCATION)) {
            Regions.remove(MythicaRegionType.MYTHICA, DefaultMythicaRegion.LOCATION);
        }

        Regions.register(new DefaultMythicaRegion(MythicaConfig.VANILLA_REGION_WEIGHT.get()));
    }

    private static void syncObservedRegions() {
        for (ObservedRegion observedRegion : OBSERVED_OVERWORLD_REGIONS.values()) {
            syncObservedRegion(observedRegion);
        }
    }

    private static void syncSurfaceRules() {
        for (String namespace : APPLIED_MYTHICA_SURFACE_RULES) {
            SurfaceRuleManager.removeSurfaceRules(MythicaRuleCategory.MYTHICA, namespace);
        }
        APPLIED_MYTHICA_SURFACE_RULES.clear();

        for (String namespace : OBSERVED_OVERWORLD_SURFACE_RULES.keySet()) {
            syncSurfaceRule(namespace);
        }
    }

    private static void syncObservedRegion(ObservedRegion observedRegion) {
        MythicaConfig.ModdedBiomeCopyModes copyMode = MythicaConfig.MODDED_BIOME_COPY_MODE.get();
        List<? extends String> modBlacklist = MythicaConfig.MOD_BLACKLIST.get();
        ResourceLocation copiedRegionName = CopiedRegion.getCopiedRegionName(observedRegion.name());

        if (isRegionPresent(MythicaRegionType.MYTHICA, copiedRegionName)) {
            Regions.remove(MythicaRegionType.MYTHICA, copiedRegionName);
        }

        boolean shouldCopy = copyMode != MythicaConfig.ModdedBiomeCopyModes.NO_COPY
            && !modBlacklist.contains(observedRegion.name().getNamespace());
        boolean shouldKeepOriginal = !shouldCopy || copyMode != MythicaConfig.ModdedBiomeCopyModes.COPY_REMOVE;
        boolean originalPresent = isRegionPresent(RegionType.OVERWORLD, observedRegion.name());

        if (!shouldKeepOriginal && originalPresent) {
            Regions.remove(RegionType.OVERWORLD, observedRegion.name());
        }

        if (shouldKeepOriginal && !originalPresent) {
            registerObservedRegion(observedRegion);
        }

        if (shouldCopy) {
            Regions.register(new CopiedRegion(observedRegion.region(), observedRegion.name(), observedRegion.region().getWeight()));
        }
    }

    private static void syncSurfaceRule(String namespace) {
        SurfaceRuleManager.removeSurfaceRules(MythicaRuleCategory.MYTHICA, namespace);
        APPLIED_MYTHICA_SURFACE_RULES.remove(namespace);

        SurfaceRules.RuleSource rules = OBSERVED_OVERWORLD_SURFACE_RULES.get(namespace);
        if (rules == null || MythicaConfig.MOD_BLACKLIST.get().contains(namespace)) {
            return;
        }

        SurfaceRuleManager.addSurfaceRules(MythicaRuleCategory.MYTHICA, namespace, rules);
        APPLIED_MYTHICA_SURFACE_RULES.add(namespace);
    }

    private static void registerObservedRegion(ObservedRegion observedRegion) {
        if (observedRegion.index() == null) {
            Regions.register(observedRegion.name(), observedRegion.region());
            return;
        }

        Regions.register(observedRegion.name(), observedRegion.index(), observedRegion.region());
    }

    private static boolean isObservableOverworldRegion(ResourceLocation name, Region region) {
        return region.getType() == RegionType.OVERWORLD
            && !(region instanceof DefaultOverworldRegion)
            && !(region instanceof CopiedRegion)
            && !Mythica.MODID.equals(name.getNamespace());
    }

    private static boolean isRegionPresent(RegionType regionType, ResourceLocation name) {
        for (Region region : Regions.get(regionType)) {
            if (region.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    private record ObservedRegion(ResourceLocation name, Region region, Integer index) {
    }
}