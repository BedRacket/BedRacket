package org.bedracket.mixin.bugfix;

import net.minecraft.block.AbstractFireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFireBlock.class)
public class MixinAbstractFireBlock {

    /**
     * Fixes the crash that occurs when a dispenser tries to put fire on the side of an obsidian block
     */
    @Inject(method = "shouldLightPortalAt", at = @At("HEAD"), cancellable = true)
    private static void fixfireBlockPlacing(World world, BlockPos blockPos, Direction direction, CallbackInfoReturnable<Boolean> info) {
        if(direction == Direction.DOWN || direction == Direction.UP) info.setReturnValue(false);
    }
}
