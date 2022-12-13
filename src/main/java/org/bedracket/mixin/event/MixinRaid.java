package org.bedracket.mixin.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.village.raid.Raid;
import org.bedracket.entity_events.player.PlayerRaidEvent;
import org.bedracket.eventbus.BedRacket;
import org.bedracket.eventbus.EventException;
import org.bedracket.eventbus.EventInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@EventInfo(events = "PlayerRaidEvent")
@Mixin(Raid.class)
public class MixinRaid {

    @Inject(method = "start",at=@At("HEAD"),cancellable = true)
    private void callPlayerRaidEvent(PlayerEntity player, CallbackInfo ci) throws EventException {
        PlayerRaidEvent brEvent =
                (PlayerRaidEvent) BedRacket.EVENT_BUS.post(PlayerRaidEvent.class,
                        new PlayerRaidEvent(player, ((Raid) (Object) this)));
        if (brEvent.isCancelled()) {
            ci.cancel();
        }
    }
}
