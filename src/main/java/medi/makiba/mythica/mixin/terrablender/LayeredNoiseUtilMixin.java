package medi.makiba.mythica.mixin.terrablender;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import medi.makiba.mythica.MythicaConfig;
import medi.makiba.mythica.compat.terrablender.MythicaRegionType;
import terrablender.api.RegionType;
import terrablender.worldgen.noise.Area;
import terrablender.worldgen.noise.AreaTransformer0;
import terrablender.worldgen.noise.InitialLayer;
import terrablender.worldgen.noise.LayeredNoiseUtil;

@Mixin(value = LayeredNoiseUtil.class, remap = false)
public abstract class LayeredNoiseUtilMixin {

    @Shadow
    public static Area createZoomedArea(long seed, int zooms, AreaTransformer0 initialTransformer) {
        return null;
    }

    @Inject(at = @At("HEAD"), method = "finalUniqueness", cancellable = true)
    private static void finalUniqueness(RegionType regionType, long seed, InitialLayer initialLayer, CallbackInfoReturnable<Area> cir) {
        int numZooms = MythicaConfig.MYTHICA_REGION_SIZE.get();
        if (regionType == MythicaRegionType.MYTHICA) {
            cir.setReturnValue(createZoomedArea(seed, numZooms, initialLayer));
        }
    }
}