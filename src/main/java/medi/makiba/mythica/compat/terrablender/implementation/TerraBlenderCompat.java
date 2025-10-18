package medi.makiba.mythica.compat.terrablender.implementation;

import com.google.common.collect.ImmutableMap;

import medi.makiba.mythica.compat.terrablender.ITerraBlenderCompat;
import medi.makiba.mythica.mixin.terrablender.SurfaceRuleManagerAccessor;
import net.minecraft.world.level.levelgen.SurfaceRules;
import terrablender.api.SurfaceRuleManager;
import terrablender.worldgen.surface.NamespacedSurfaceRuleSource;

public class TerraBlenderCompat implements ITerraBlenderCompat {
    public static SurfaceRules.RuleSource getMythicaNamespacedRules(SurfaceRuleManager.RuleCategory category, SurfaceRules.RuleSource fallback) {
        ImmutableMap.Builder<String, SurfaceRules.RuleSource> builder = ImmutableMap.builder();
        builder.put("mythica", SurfaceRuleManager.getDefaultSurfaceRules(category));
        builder.putAll(SurfaceRuleManagerAccessor.getSurfaceRules().get(category));
        return new NamespacedSurfaceRuleSource(fallback, builder.build());
    }
}
