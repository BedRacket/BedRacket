package org.bedracket.mixin.config;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.bedracket.util.BedRacketConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BambooBlock.class)
public class MixinBambooBlock {

    @ModifyExpressionValue(method = "randomTick",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"))
    private boolean bambooGrowthRate(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        return random.nextFloat()
                < (BedRacketConfig.bambooModifier / (100.0f * 3))
                && world.isAir(pos.up()) && world.getBaseLightLevel(pos.up(), 0) >= 9;
    }
}
