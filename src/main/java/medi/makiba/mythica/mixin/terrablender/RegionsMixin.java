package medi.makiba.mythica.mixin.terrablender;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import medi.makiba.mythica.compat.terrablender.MythicaTerrablenderSync;
import net.minecraft.resources.ResourceLocation;
import terrablender.api.Region;
import terrablender.api.Regions;

@Mixin(value = Regions.class, remap = false)
public abstract class RegionsMixin {
    @Inject(at = @At("HEAD"), method = "register(Lnet/minecraft/resources/ResourceLocation;Lterrablender/api/Region;)V", cancellable = true)
    private static void skipOriginalOverworldRegion(ResourceLocation name, Region region, CallbackInfo ci) {
        if (MythicaTerrablenderSync.shouldCancelOriginalRegionRegistration(name, region)) {
            ci.cancel();
        }
    }

    @Inject(at = @At("TAIL"), method = "register(Lnet/minecraft/resources/ResourceLocation;Lterrablender/api/Region;)V")
    private static void observeOverworldRegion(ResourceLocation name, Region region, CallbackInfo ci) {
        MythicaTerrablenderSync.observeRegionRegistration(name, region);
    }

    @Inject(at = @At("HEAD"), method = "register(Lnet/minecraft/resources/ResourceLocation;ILterrablender/api/Region;)V", cancellable = true)
    private static void skipIndexedOriginalOverworldRegion(ResourceLocation name, int index, Region region, CallbackInfo ci) {
        if (MythicaTerrablenderSync.shouldCancelOriginalRegionRegistration(name, region, index)) {
            ci.cancel();
        }
    }

    @Inject(at = @At("TAIL"), method = "register(Lnet/minecraft/resources/ResourceLocation;ILterrablender/api/Region;)V")
    private static void observeIndexedOverworldRegion(ResourceLocation name, int index, Region region, CallbackInfo ci) {
        MythicaTerrablenderSync.observeRegionRegistration(name, region, index);
    }
}
