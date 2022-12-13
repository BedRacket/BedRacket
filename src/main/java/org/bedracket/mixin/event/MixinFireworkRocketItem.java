package org.bedracket.mixin.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.bedracket.entity_events.player.PlayerFireworkEvent;
import org.bedracket.eventbus.BedRacket;
import org.bedracket.eventbus.EventException;
import org.bedracket.eventbus.EventInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@EventInfo(events = "PlayerFireworkEvent")
@Mixin(FireworkRocketItem.class)
public class MixinFireworkRocketItem {

    @Inject(method = "use",at=@At("HEAD"),cancellable = true)
    private void onFireworkUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) throws EventException {
        PlayerFireworkEvent brEvent =
                (PlayerFireworkEvent) BedRacket.EVENT_BUS.post(PlayerFireworkEvent.class,
                new PlayerFireworkEvent(user, world, ((FireworkRocketItem) (Object) this)));
        if (brEvent.isCancelled()) {
            cir.setReturnValue(TypedActionResult.fail(user.getStackInHand(hand)));
        }
    }

    @Inject(method = "useOnBlock",at=@At("HEAD"),cancellable = true)
    private void onUseOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) throws EventException {
        PlayerFireworkEvent brEvent =
                (PlayerFireworkEvent) BedRacket.EVENT_BUS.post(PlayerFireworkEvent.class,
                        new PlayerFireworkEvent(context.getPlayer(), context.getWorld(),
                                ((FireworkRocketItem) (Object) this)));
        if (brEvent.isCancelled()) {
            cir.setReturnValue(ActionResult.FAIL);
        }
    }
}
