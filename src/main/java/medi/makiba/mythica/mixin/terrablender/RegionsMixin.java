package medi.makiba.mythica.mixin.terrablender;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import medi.makiba.mythica.MythicaConfig;
import medi.makiba.mythica.MythicaConfig.ModdedBiomeCopyModes;
import medi.makiba.mythica.compat.terrablender.CopiedRegion;
import medi.makiba.mythica.compat.terrablender.DefaultMythicaRegion;
import net.minecraft.resources.ResourceLocation;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.Regions;
import terrablender.worldgen.DefaultOverworldRegion;

import static terrablender.api.Regions.register;

@Mixin(value = Regions.class, remap = false)
public abstract class RegionsMixin {
    static {
        register(new DefaultMythicaRegion(MythicaConfig.VANILLA_REGION_WEIGHT.get()));
    }

    @Inject(at = @At("HEAD"), method = "register(Lnet/minecraft/resources/ResourceLocation;Lterrablender/api/Region;)V", cancellable = true)
    private static void copyOverworldRegionToMythica(ResourceLocation name, Region region, CallbackInfo ci) {
        MythicaConfig.ModdedBiomeCopyModes copyMode = MythicaConfig.MODDED_BIOME_COPY_MODE.get();
        if (region.getType() == RegionType.OVERWORLD
            && !(region instanceof DefaultOverworldRegion)
            && copyMode != MythicaConfig.ModdedBiomeCopyModes.NO_COPY
            && !MythicaConfig.MOD_BLACKLIST.get().contains(name.getNamespace())) {
            Region copyRegion = new CopiedRegion(region, name, region.getWeight());
            register(copyRegion);
            if(copyMode == ModdedBiomeCopyModes.COPY_REMOVE) {
                ci.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "register(Lnet/minecraft/resources/ResourceLocation;ILterrablender/api/Region;)V", cancellable = true)
    private static void copyOverworldRegionToMythicaWithIndex(ResourceLocation name, int index, Region region, CallbackInfo ci) {
        MythicaConfig.ModdedBiomeCopyModes copyMode = MythicaConfig.MODDED_BIOME_COPY_MODE.get();
        if (region.getType() == RegionType.OVERWORLD
            && copyMode != MythicaConfig.ModdedBiomeCopyModes.NO_COPY
            && !MythicaConfig.MOD_BLACKLIST.get().contains(name.getNamespace())) {
            Region copyRegion = new CopiedRegion(region, name, region.getWeight());
            register(copyRegion);//ignore index since the original index wasn't for Mythica anyway
            if(copyMode == ModdedBiomeCopyModes.COPY_REMOVE) {
                ci.cancel();
            }
        }
    }
}
