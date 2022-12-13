package org.bedracket.mixin.event;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.bedracket.entity_events.EntityJoinWorldEvent;
import org.bedracket.eventbus.BedRacket;
import org.bedracket.eventbus.EventException;
import org.bedracket.eventbus.EventInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@EventInfo(events = "EntityJoinWorldEvent")
@Mixin(ClientWorld.class)
public class MixinClientWorld {

    @Inject(method = "addEntityPrivate", at = @At("HEAD"), cancellable = true)
    private void onEntityAdded(int id, Entity entity, CallbackInfo ci) throws EventException {
        EntityJoinWorldEvent bedracketEvent =
                (EntityJoinWorldEvent) BedRacket.EVENT_BUS.post(EntityJoinWorldEvent.class,
                        new EntityJoinWorldEvent(entity, entity.getWorld()));
        if (bedracketEvent.isCancelled()) {
            ci.cancel();
        }
    }
}
