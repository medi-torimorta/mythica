package medi.makiba.mythica.mixin.terrablender;

import net.minecraft.world.level.levelgen.SurfaceRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import medi.makiba.mythica.compat.terrablender.MythicaRuleCategory;
import medi.makiba.mythica.compat.terrablender.MythicaSurfaceRuleData;
import medi.makiba.mythica.compat.terrablender.MythicaTerrablenderSync;
import terrablender.api.SurfaceRuleManager;
import terrablender.api.SurfaceRuleManager.RuleCategory;

import java.util.Map;

@Mixin(value = SurfaceRuleManager.class, remap = false)
public class SurfaceRuleManagerMixin {

    @Shadow
    private static Map<SurfaceRuleManager.RuleCategory, SurfaceRules.RuleSource> defaultSurfaceRules;

    @Shadow
    private static Map<RuleCategory, Map<String, SurfaceRules.RuleSource>> surfaceRules;

    @Inject(method = "getDefaultSurfaceRules", at = @At("HEAD"), cancellable = true)
    private static void getDefaultSurfaceRules(SurfaceRuleManager.RuleCategory category, CallbackInfoReturnable<SurfaceRules.RuleSource> cir) {
        if (defaultSurfaceRules.containsKey(category)) {
            cir.setReturnValue(defaultSurfaceRules.get(category));
        } else if (category == MythicaRuleCategory.MYTHICA) {
            cir.setReturnValue(MythicaSurfaceRuleData.mythica());
        }
    }

    @Inject(method = "addSurfaceRules", at = @At("TAIL"))
    private static void addSurfaceRulesToMythica(RuleCategory category, String namespace, SurfaceRules.RuleSource rules, CallbackInfo ci) {
        MythicaTerrablenderSync.observeSurfaceRulesAdded(category, namespace, rules);
   }

   @Inject(method = "removeSurfaceRules", at = @At("TAIL"))
    private static void removeSurfaceRulesToMythica(RuleCategory category, String namespace, CallbackInfo ci) {
        MythicaTerrablenderSync.observeSurfaceRulesRemoved(category, namespace);
   }
}
