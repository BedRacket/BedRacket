package org.bedracket.entity_events.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.world.World;
import org.bedracket.eventbus.Cancellable;

public class PlayerFireworkEvent extends PlayerEvent implements Cancellable {

    private final World world;
    private final FireworkRocketItem firework;
    private boolean cancel;

    public PlayerFireworkEvent(PlayerEntity player, World world, FireworkRocketItem firework) {
        super(player);
        this.world = world;
        this.firework = firework;
    }

    public World getWorld() {
        return world;
    }

    public FireworkRocketItem getFirework() {
        return firework;
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
