package org.bedracket.mixin.event;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.bedracket.entity_events.item.ItemTickEvent;
import org.bedracket.entity_events.player.PlayerAttemptPickupItemEvent;
import org.bedracket.eventbus.BedRacket;
import org.bedracket.eventbus.EventException;
import org.bedracket.eventbus.EventInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@EventInfo(events = "ItemTickEvent, PlayerAttemptPickupItemEvent")
@Mixin(ItemEntity.class)
public class MixinItemEntity {

    @Shadow private int pickupDelay;

    @Inject(method = "tick", at = @At("TAIL"))
    private void callItemTickEvent(CallbackInfo ci) throws EventException {
        BedRacket.EVENT_BUS.post(ItemTickEvent.class, new ItemTickEvent(((ItemEntity) (Object) this)));
    }

    @Inject(method = "onPlayerCollision", at = @At("TAIL"), cancellable = true)
    private void callPlayerAttemptPickupItemEvent(PlayerEntity player, CallbackInfo ci) throws EventException {
        // Paper start
        boolean flyAtPlayer = false; // Paper
        if (this.pickupDelay <= 0) {
            PlayerAttemptPickupItemEvent attemptEvent = (PlayerAttemptPickupItemEvent) BedRacket.EVENT_BUS.post(
                    PlayerAttemptPickupItemEvent.class,
                    new PlayerAttemptPickupItemEvent(player,
                            ((ItemEntity) (Object) this).getStack().getItem(),
                            ((ItemEntity) (Object) this).getItemAge()));
            flyAtPlayer = attemptEvent.getFlyAtPlayer();
            if (attemptEvent.isCancelled()) {
                if (flyAtPlayer) {
                    player.getInventory().insertStack(((ItemEntity) (Object) this).getStack());
                }
                ci.cancel();
            }
        }
    }
}
