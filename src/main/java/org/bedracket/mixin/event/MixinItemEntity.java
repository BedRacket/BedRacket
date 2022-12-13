package org.bedracket.mixin.event;

import net.minecraft.entity.ItemEntity;
import org.bedracket.entity_events.item.ItemTickEvent;
import org.bedracket.eventbus.BedRacket;
import org.bedracket.eventbus.EventException;
import org.bedracket.eventbus.EventInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@EventInfo(events = "ItemTickEvent")
@Mixin(ItemEntity.class)
public class MixinItemEntity {

    @Inject(method = "tick", at = @At("TAIL"))
    private void callItemTickEvent(CallbackInfo ci) throws EventException {
        BedRacket.EVENT_BUS.post(ItemTickEvent.class, new ItemTickEvent(((ItemEntity) (Object) this)));
    }
}
