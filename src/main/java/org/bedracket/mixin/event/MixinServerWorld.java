package org.bedracket.mixin.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.bedracket.entity_events.EntityExplosionEvent;
import org.bedracket.entity_events.EntityJoinWorldEvent;
import org.bedracket.entity_events.item.ItemSpawnEvent;
import org.bedracket.eventbus.BedRacket;
import org.bedracket.eventbus.EventException;
import org.bedracket.eventbus.EventInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@EventInfo(events = "EntityJoinWorldEvent, ItemSpawnEvent, EntityExplosionEvent")
@Mixin(ServerWorld.class)
public abstract class MixinServerWorld {

    @Shadow @Final private static int field_35653;

    @Inject(method = "addEntity", at = @At("HEAD"), cancellable = true)
    private void onAddEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) throws EventException {
        EntityJoinWorldEvent bedracketEvent =
                (EntityJoinWorldEvent) BedRacket.EVENT_BUS.post(EntityJoinWorldEvent.class,
                        new EntityJoinWorldEvent(entity, entity.getWorld()));
        if (bedracketEvent.isCancelled()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "addPlayer", at = @At("HEAD"), cancellable = true)
    private void onAddPlayer(ServerPlayerEntity player, CallbackInfo ci) throws EventException {
        EntityJoinWorldEvent bedracketEvent =
                (EntityJoinWorldEvent) BedRacket.EVENT_BUS.post(EntityJoinWorldEvent.class,
                        new EntityJoinWorldEvent(player, player.getWorld()));
        if (bedracketEvent.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "spawnEntity", at = @At("HEAD"))
    private void onSpawnEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) throws EventException {
        if (entity instanceof ItemEntity) {
            BedRacket.EVENT_BUS.post(ItemSpawnEvent.class, new ItemSpawnEvent((ItemEntity) entity));
        }
    }

    @Inject(method = "createExplosion", at = @At("HEAD"), cancellable = true)
    private void callEntityExplosionEvent(Entity entity, DamageSource damageSource, ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, World.ExplosionSourceType explosionSourceType, CallbackInfoReturnable<Explosion> cir) throws EventException {
        EntityExplosionEvent brEvent =
                (EntityExplosionEvent) BedRacket.EVENT_BUS.post(EntityExplosionEvent.class,
                        new EntityExplosionEvent(entity, damageSource, behavior, x, y, z, power, createFire, explosionSourceType));
        if (brEvent.isCancelled()) {
            cir.cancel();
        }
    }
}
