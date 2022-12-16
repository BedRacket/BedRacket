package org.bedracket.mixin.event;

import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.HitResult;
import org.bedracket.entity_events.EntityShootEvent;
import org.bedracket.eventbus.BedRacket;
import org.bedracket.eventbus.EventException;
import org.bedracket.eventbus.EventInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@EventInfo(events = "EntityShootEvent")
@Mixin(ProjectileEntity.class)
public class MixinProjectileEntity {

    @Inject(method = "onCollision",at=@At("HEAD"),cancellable = true)
    private void callEntityShootEvent(HitResult hitResult, CallbackInfo ci) throws EventException {
        EntityShootEvent brEvent =
                (EntityShootEvent) BedRacket.EVENT_BUS.post(EntityShootEvent.class,
                        new EntityShootEvent(((ProjectileEntity) (Object) this).getOwner(),
                                ((ProjectileEntity) (Object) this).getWorld(), ((ProjectileEntity) (Object) this)));
        if (brEvent.isCancelled()) {
            ci.cancel();
        }
    }
}
