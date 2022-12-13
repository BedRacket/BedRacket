package org.bedracket.mixin.event;

import net.minecraft.block.BlockState;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bedracket.entity_events.EntityPortalEvent;
import org.bedracket.eventbus.BedRacket;
import org.bedracket.eventbus.EventException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndPortalBlock.class)
public class MixinEndPortalBlock {

    @Inject(method = "onEntityCollision", at = @At("HEAD"), cancellable = true)
    private void callEntityPortalEvent(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) throws EventException {
        EntityPortalEvent brEvent =
                (EntityPortalEvent) BedRacket.EVENT_BUS.post(EntityPortalEvent.class,
                        new EntityPortalEvent(entity, pos, world, state));
        if (brEvent.isCancelled()) {
            ci.cancel();
        }
    }
}
