package medi.makiba.mythica.mixin.terrablender;

import org.spongepowered.asm.mixin.Mixin;

import medi.makiba.mythica.MythicaConfig;
import medi.makiba.mythica.compat.terrablender.implementation.DefaultMythicaRegion;
import terrablender.api.Regions;

import static terrablender.api.Regions.register;

@Mixin(value = Regions.class, remap = false)
public abstract class RegionsMixin {
    static {
        register(new DefaultMythicaRegion(MythicaConfig.OVERWORLD_REGION_WEIGHT.get()));
    }
}
