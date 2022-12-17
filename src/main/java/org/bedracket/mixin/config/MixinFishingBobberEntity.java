package org.bedracket.mixin.config;

import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.math.BlockPos;
import org.bedracket.util.BedRacketConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingBobberEntity.class)
public class MixinFishingBobberEntity {

    @Inject(method = "isOpenOrWaterAround",
            cancellable = true,
            at = @At("RETURN"))
    private void isOpenOrWater(BlockPos pos, CallbackInfoReturnable<Boolean> cir){
        if (BedRacketConfig.oldFishingFarm) {
            cir.setReturnValue(true);
        }
    }

}
