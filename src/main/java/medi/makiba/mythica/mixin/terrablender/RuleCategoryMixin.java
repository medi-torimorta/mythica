package medi.makiba.mythica.mixin.terrablender;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import medi.makiba.mythica.compat.terrablender.MythicaRuleCategory;
import terrablender.api.SurfaceRuleManager;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(value = SurfaceRuleManager.RuleCategory.class, remap = false)
abstract class RuleCategoryMixin {

    @Shadow
    @Final
    @Mutable
    private static SurfaceRuleManager.RuleCategory[] $VALUES;

    @SuppressWarnings("InvokerTarget")
    @Invoker("<init>")
    private static SurfaceRuleManager.RuleCategory newVariant(String internalName, int internalId) {
        throw new AssertionError();
    }

    @Inject(method = "<clinit>", at = @At(value = "FIELD", opcode = Opcodes.PUTSTATIC, target = "Lterrablender/api/SurfaceRuleManager$RuleCategory;$VALUES:[Lterrablender/api/SurfaceRuleManager$RuleCategory;", shift = At.Shift.AFTER))
    private static void addCustomVariant(CallbackInfo ci) {
        var variants = new ArrayList<>(Arrays.asList($VALUES));
        var mythica = newVariant("MYTHICA", variants.getLast().ordinal() + 1);
        MythicaRuleCategory.MYTHICA = mythica;
        variants.add(mythica);
        $VALUES = variants.toArray(new SurfaceRuleManager.RuleCategory[0]);
    }
}