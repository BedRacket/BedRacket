package org.bedracket.mixin.config;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import org.bedracket.util.BedRacketConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CropBlock.class)
public abstract class MixinCropBlock {

    @Shadow
    protected static float getAvailableMoisture(Block block, BlockView world, BlockPos pos) {
        return 0;
    }

    @ModifyExpressionValue(method = "randomTick",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"))
    private boolean cropGrowthRate(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        int modifier;
        if (((CropBlock) (Object) this) == Blocks.BEETROOTS) {
            modifier = BedRacketConfig.beetrootModifier;
            return  random.nextFloat() < (modifier / (100.0f * (Math.floor((25.0F / getAvailableMoisture(((CropBlock) (Object) this), world, pos)) + 1)))); // Spigot - SPIGOT-7159: Better modifier resolution
        }
        if (((CropBlock) (Object) this) == Blocks.CARROTS) {
            modifier = BedRacketConfig.carrotModifier;
            return  random.nextFloat() < (modifier / (100.0f * (Math.floor((25.0F / getAvailableMoisture(((CropBlock) (Object) this), world, pos)) + 1)))); // Spigot - SPIGOT-7159: Better modifier resolution
        }
        if (((CropBlock) (Object) this) == Blocks.POTATOES) {
            modifier = BedRacketConfig.potatoModifier;
            return  random.nextFloat() < (modifier / (100.0f * (Math.floor((25.0F / getAvailableMoisture(((CropBlock) (Object) this), world, pos)) + 1)))); // Spigot - SPIGOT-7159: Better modifier resolution

        }
        if (((CropBlock) (Object) this) == Blocks.WHEAT) {
            modifier = BedRacketConfig.wheatModifier;
            return random.nextFloat() < (modifier / (100.0f * (Math.floor((25.0F / getAvailableMoisture(((CropBlock) (Object) this), world, pos)) + 1)))); // Spigot - SPIGOT-7159: Better modifier resolution
        } else {
            return random.nextInt((int) (25.0F / getAvailableMoisture(((CropBlock) (Object) this), world, pos)) + 1) == 0;
        }
    }
}
