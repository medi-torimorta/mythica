package medi.makiba.mythica.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import medi.makiba.mythica.worldgen.dimension.MythicaDimensions;
import medi.makiba.mythica.worldgen.dimensiontype.MythicaDimensionTypes;
import net.minecraft.server.level.ServerLevel;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

    @Inject(at = @At("HEAD"), method = "getSeed", cancellable = true)
    public void onGetSeed(CallbackInfoReturnable<Long> cir) {
        if(((ServerLevel)(Object)this).dimensionTypeRegistration().is(MythicaDimensionTypes.MYTHICA_DIM_TYPE)) {
            cir.setReturnValue(MythicaDimensions.getSeed(((ServerLevel)(Object)this).getServer().getWorldData().worldGenOptions().seed()));
        }
    }
}
