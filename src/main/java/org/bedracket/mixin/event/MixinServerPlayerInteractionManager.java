package org.bedracket.mixin.event;

import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.bedracket.block_events.BlockBreakEvent;
import org.bedracket.entity_events.player.RightClickBlockEvent;
import org.bedracket.eventbus.BedRacket;
import org.bedracket.eventbus.EventException;
import org.bedracket.eventbus.EventInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@EventInfo(events = "RightClickBlockEvent")
@Mixin(ServerPlayerInteractionManager.class)
public abstract class MixinServerPlayerInteractionManager {

    @Shadow @Final protected ServerPlayerEntity player;

    @Shadow protected ServerWorld world;

    @Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
    private void callRightClickBlockEvent(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) throws EventException {
        RightClickBlockEvent bedracketEvent = (RightClickBlockEvent) BedRacket.EVENT_BUS.post(RightClickBlockEvent.class,
                new RightClickBlockEvent(player, hand, hitResult.getBlockPos(), hitResult));
        if (bedracketEvent.isCancelled()) cir.setReturnValue(bedracketEvent.getCancellationResult());
    }

    @Inject(method = "tryBreakBlock", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/world/ServerWorld;" +
                    "getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;",
            shift = At.Shift.AFTER), cancellable = true)
    private void callBlockBreakEvent(BlockPos pos, CallbackInfoReturnable<Boolean> cir) throws EventException {

        if (this.player != null) {
            // Sword + Creative mode pre-cancel
            boolean isSwordNoBreak =
                    !this.player.getMainHandStack()
                            .getItem()
                            .canMine(this.world.getBlockState(pos), this.world, pos, this.player);
            // Tell client the block is gone immediately then process events
            // Don't tell the client if its a creative sword break because its not broken!
            if (world.getBlockEntity(pos) == null && !isSwordNoBreak) {
                BlockUpdateS2CPacket packet = new BlockUpdateS2CPacket(pos, Blocks.AIR.getDefaultState());
                this.player.networkHandler.sendPacket(packet);
            }

            BlockBreakEvent brEvent =
                    (BlockBreakEvent) BedRacket.EVENT_BUS.post(BlockBreakEvent.class,
                            new BlockBreakEvent(
                                    this.world.getBlockState(pos).getBlock(), player));
            // Sword + Creative mode pre-cancel
            brEvent.setCancelled(isSwordNoBreak);

            if (brEvent.isCancelled()) {
                if (isSwordNoBreak) {
                    cir.setReturnValue(false);
                }
                // Let the client know the block still exists
                this.player.networkHandler.sendPacket(new BlockUpdateS2CPacket(this.world, pos));

                // Brute force all possible updates
                for (Direction dir : Direction.values())
                    this.player.networkHandler.sendPacket(new BlockUpdateS2CPacket(world, pos.offset(dir)));

                // Update any tile entity data for this block
                BlockEntity tileentity = this.world.getBlockEntity(pos);
                if (tileentity != null)
                    this.player.networkHandler.sendPacket(tileentity.toUpdatePacket());

                cir.setReturnValue(false);
            }
        }
    }
}
