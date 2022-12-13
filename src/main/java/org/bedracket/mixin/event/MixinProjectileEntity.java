package org.bedracket.mixin.event;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;
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

    @Inject(method = "<init>",at=@At("RETURN"),cancellable = true)
    private void callEntityShootEvent(EntityType<?> entityType, World world, CallbackInfo ci) throws EventException {
        EntityShootEvent brEvent =
                (EntityShootEvent) BedRacket.EVENT_BUS.post(EntityShootEvent.class,
                        new EntityShootEvent(((ProjectileEntity) (Object) this).getOwner(),
                                world, ((ProjectileEntity) (Object) this)));
        if (brEvent.isCancelled()) {
            ci.cancel();
        }
    }
}
