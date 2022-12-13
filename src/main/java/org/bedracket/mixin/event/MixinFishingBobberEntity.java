package org.bedracket.mixin.event;

import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import org.bedracket.entity_events.player.PlayerFishingEvent;
import org.bedracket.eventbus.BedRacket;
import org.bedracket.eventbus.EventException;
import org.bedracket.eventbus.EventInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@EventInfo(events = "PlayerFishingEvent")
@Mixin(FishingBobberEntity.class)
public abstract class MixinFishingBobberEntity {

    @Inject(method = "use",at=@At("HEAD"),cancellable = true)
    private void callPlayerFishingEvent(ItemStack usedItem, CallbackInfoReturnable<Integer> cir) throws EventException {
        assert  ((FishingBobberEntity) (Object) this).getPlayerOwner() != null;
        PlayerFishingEvent bedracketEvent =(PlayerFishingEvent) BedRacket.EVENT_BUS.post(
                PlayerFishingEvent.class, new PlayerFishingEvent(
                        ((FishingBobberEntity) (Object) this).getPlayerOwner(),
                        ((FishingBobberEntity) (Object) this).getPlayerOwner().getWorld())
        );
        if (bedracketEvent.isCancelled()) {
            cir.setReturnValue(0);
        }
    }
}
