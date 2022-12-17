package org.bedracket.mixin.event;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.bedracket.entity_events.player.PlayerJoinEvent;
import org.bedracket.eventbus.BedRacket;
import org.bedracket.eventbus.EventException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class MixinPlayerManager {


    @Inject(method = "onPlayerConnect",
            at = @At(value = "INVOKE",
                    target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                    shift = At.Shift.AFTER), cancellable = true)
    private void callPlayerJoinEvent(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) throws EventException {
        PlayerJoinEvent playerJoinEvent =
                (PlayerJoinEvent) BedRacket.EVENT_BUS.post(PlayerJoinEvent.class,
                        new PlayerJoinEvent(player));
        if (!player.networkHandler.connection.isOpen()) {
            ci.cancel();
        }
    }

}
