package org.bedracket.mixin.event;

import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bedracket.entity_events.player.PlayerBedFailEnterEvent;
import org.bedracket.eventbus.BedRacket;
import org.bedracket.eventbus.EventException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedBlock.class)
public abstract class MixinBedBlock {

    @Shadow
    public static boolean isBedWorking(World world) {
        return false;
    }

    @Inject(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BedBlock;isBedWorking(Lnet/minecraft/world/World;)Z"), cancellable = true)
    private void callPlayerBedFailEnterEvent(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) throws EventException {
        if (cir.getReturnValue() == ActionResult.FAIL) {
            PlayerBedFailEnterEvent brEvent =
                    (PlayerBedFailEnterEvent) BedRacket.EVENT_BUS.post(
                            PlayerBedFailEnterEvent.class,
                            new PlayerBedFailEnterEvent(player,
                                    PlayerBedFailEnterEvent.FailReason.VALUES[cir.getReturnValue().ordinal()],
                                    ((BedBlock) (Object) this), !isBedWorking(world)));
            if (brEvent.isCancelled()) {
                cir.setReturnValue(ActionResult.SUCCESS);
            }
        }
    }
}
