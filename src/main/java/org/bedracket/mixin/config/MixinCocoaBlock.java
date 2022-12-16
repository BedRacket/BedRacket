package org.bedracket.mixin.config;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.BlockState;
import net.minecraft.block.CocoaBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.bedracket.util.BedRacketConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CocoaBlock.class)
public class MixinCocoaBlock {

    @ModifyExpressionValue(method = "randomTick",
            at = @At(value = "INVOKE",
            target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"))
    private boolean cocoaGrowthRate(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        return world.random.nextFloat() < (BedRacketConfig.cocoaModifier / (100.0f * 5));
    }
}
