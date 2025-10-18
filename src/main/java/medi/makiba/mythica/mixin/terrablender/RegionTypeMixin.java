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

import medi.makiba.mythica.compat.terrablender.MythicaRegionType;
import terrablender.api.RegionType;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(value = RegionType.class, remap = false)
abstract class RegionTypeMixin {

    @Shadow
    @Final
    @Mutable
    private static RegionType[] $VALUES;

    @SuppressWarnings("InvokerTarget")
    @Invoker("<init>")
    private static RegionType newVariant(String internalName, int internalId) {
        throw new AssertionError();
    }

    @Inject(method = "<clinit>", at = @At(value = "FIELD", opcode = Opcodes.PUTSTATIC, target = "Lterrablender/api/RegionType;$VALUES:[Lterrablender/api/RegionType;", shift = At.Shift.AFTER))
    private static void addCustomVariant(CallbackInfo ci) {
        var variants = new ArrayList<>(Arrays.asList(RegionType.values()));
        var mythica = newVariant("MYTHICA", variants.getLast().ordinal() + 1);
        MythicaRegionType.MYTHICA = mythica;
        variants.add(mythica);
        $VALUES = variants.toArray(new RegionType[0]);
    }
}
