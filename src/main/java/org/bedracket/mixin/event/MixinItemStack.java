package org.bedracket.mixin.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.bedracket.entity_events.player.PlayerCraftEvent;
import org.bedracket.eventbus.BedRacket;
import org.bedracket.eventbus.EventException;
import org.bedracket.eventbus.EventInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@EventInfo(events = "PlayerCraftEvent")
@Mixin(ItemStack.class)
public class MixinItemStack {

    @Inject(method = "onCraft", at=@At("HEAD"),cancellable = true)
    private void callPlayerCraftEvent(World world, PlayerEntity player, int amount, CallbackInfo ci) throws EventException {
        PlayerCraftEvent brEvent =
                (PlayerCraftEvent) BedRacket.EVENT_BUS.post(PlayerCraftEvent.class,
                new PlayerCraftEvent(player, ((ItemStack)(Object)this), world));
        if (brEvent.isCancelled()) {
            ci.cancel();
        }
    }
}
