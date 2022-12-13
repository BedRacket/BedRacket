package org.bedracket.entity_events.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.bedracket.eventbus.Cancellable;

public class PlayerCraftEvent extends PlayerEvent implements Cancellable {

    private final ItemStack stack;
    private final World world;
    private boolean cancel;

    public PlayerCraftEvent(PlayerEntity player, ItemStack stack, World world) {
        super(player);
        this.stack = stack;
        this.world = world;
    }

    public ItemStack getStack() {
        return stack;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
