package medi.makiba.mythica.mixin.terrablender;

import net.minecraft.world.level.levelgen.SurfaceRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import medi.makiba.mythica.compat.terrablender.implementation.MythicaRuleCategory;
import medi.makiba.mythica.compat.terrablender.implementation.MythicaSurfaceRuleData;
import terrablender.api.SurfaceRuleManager;

import java.util.Map;

@Mixin(value = SurfaceRuleManager.class, remap = false)
public class SurfaceRuleManagerMixin {

    @Shadow
    private static Map<SurfaceRuleManager.RuleCategory, SurfaceRules.RuleSource> defaultSurfaceRules;

    @Inject(method = "getDefaultSurfaceRules", at = @At("HEAD"), cancellable = true)
    private static void getDefaultSurfaceRules(SurfaceRuleManager.RuleCategory category, CallbackInfoReturnable<SurfaceRules.RuleSource> cir) {
        if (defaultSurfaceRules.containsKey(category)) {
            cir.setReturnValue(defaultSurfaceRules.get(category));
        } else if (category == MythicaRuleCategory.MYTHICA) {
            cir.setReturnValue(MythicaSurfaceRuleData.mythica());
        }
    }
}
