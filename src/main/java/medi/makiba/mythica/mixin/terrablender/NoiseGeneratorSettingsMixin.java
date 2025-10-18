package medi.makiba.mythica.mixin.terrablender;

import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import medi.makiba.mythica.compat.terrablender.implementation.MythicaRuleCategory;
import medi.makiba.mythica.compat.terrablender.implementation.TerraBlenderCompat;
import terrablender.api.SurfaceRuleManager;
import terrablender.worldgen.IExtendedNoiseGeneratorSettings;

@Mixin(value = NoiseGeneratorSettings.class, priority = 900)
public class NoiseGeneratorSettingsMixin implements IExtendedNoiseGeneratorSettings {

    @Final
    @Shadow
    private SurfaceRules.RuleSource surfaceRule;

    @Unique
    private SurfaceRuleManager.RuleCategory mythica$ruleCategory = null;

    @Unique
    private SurfaceRules.RuleSource mythica$namespacedSurfaceRuleSource = null;

    @Inject(method = "surfaceRule", at = @At("HEAD"), cancellable = true)
    private void surfaceRule(CallbackInfoReturnable<SurfaceRules.RuleSource> cir) {
        if (this.mythica$ruleCategory == MythicaRuleCategory.MYTHICA) {
            if (this.mythica$namespacedSurfaceRuleSource == null)
                this.mythica$namespacedSurfaceRuleSource = TerraBlenderCompat.getMythicaNamespacedRules(MythicaRuleCategory.MYTHICA, this.surfaceRule);
            cir.setReturnValue(this.mythica$namespacedSurfaceRuleSource);
        }
    }

    @Override
    public void setRuleCategory(SurfaceRuleManager.RuleCategory ruleCategory) {
        this.mythica$ruleCategory = ruleCategory;
    }
}