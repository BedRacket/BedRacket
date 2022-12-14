package org.bedracket.mixin.event;

import com.mojang.datafixers.util.Either;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import org.bedracket.entity_events.player.PlayerLevelUpEvent;
import org.bedracket.entity_events.player.PlayerSleepEvent;
import org.bedracket.eventbus.BedRacket;
import org.bedracket.eventbus.EventException;
import org.bedracket.eventbus.EventInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@EventInfo(events = "PlayerSleepEvent, PlayerLevelUpEvent")
@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity {

    @Inject(method = "trySleep",at=@At("HEAD"),cancellable = true)
    private void callPlayerSleepEvent(BlockPos pos, CallbackInfoReturnable<Either<PlayerEntity.SleepFailureReason, Unit>> cir) throws EventException {
        PlayerSleepEvent bedracketEvent = (PlayerSleepEvent) BedRacket.EVENT_BUS.post(PlayerSleepEvent.class,
                new PlayerSleepEvent(((PlayerEntity) (Object) this), pos,
                        ((PlayerEntity) (Object) this).getWorld().getTime()));
        if (bedracketEvent.isCancelled()) {
            cir.cancel();
        }
    }

    @Inject(method = "addExperienceLevels",at=@At("HEAD"),cancellable = true)
    private void callPlayerLevelUpEvent(int levels, CallbackInfo ci) throws EventException {
        PlayerLevelUpEvent bedracketEvent =
                (PlayerLevelUpEvent) BedRacket.EVENT_BUS.post(PlayerLevelUpEvent.class, new PlayerLevelUpEvent(((PlayerEntity) (Object) this), ((PlayerEntity) (Object) this).getWorld()));
        if (bedracketEvent.isCancelled()) {
            ci.cancel();
        }
    }
}
