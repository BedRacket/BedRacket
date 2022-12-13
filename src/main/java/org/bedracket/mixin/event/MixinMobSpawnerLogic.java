package org.bedracket.mixin.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.MobSpawnerLogic;
import net.minecraft.world.World;
import org.bedracket.entity_events.EntitySpawnLogicEvent;
import org.bedracket.eventbus.BedRacket;
import org.bedracket.eventbus.EventException;
import org.bedracket.eventbus.EventInfo;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@EventInfo(events = "EntitySpawnLogicEvent")
@Mixin(MobSpawnerLogic.class)
public class MixinMobSpawnerLogic {

    @Shadow @Nullable private Entity renderedEntity;

    @Inject(method = "updateSpawns",at = @At("HEAD"),cancellable = true)
    private void callEntitySpawnLogicEvent(World world, BlockPos pos, CallbackInfo ci) throws EventException {
        for(PlayerEntity player: world.getPlayers()) {
            EntitySpawnLogicEvent brEvent =
                    (EntitySpawnLogicEvent) BedRacket.EVENT_BUS.post(EntitySpawnLogicEvent.class,
                    new EntitySpawnLogicEvent(this.renderedEntity, world, player));
            if (brEvent.isCancelled()) {
                ci.cancel();
            }
        }
    }
}
