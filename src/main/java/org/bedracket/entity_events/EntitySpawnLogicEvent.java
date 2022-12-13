package org.bedracket.entity_events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.bedracket.eventbus.Cancellable;

public class EntitySpawnLogicEvent extends EntityEvent implements Cancellable {

    private final World world;
    private final PlayerEntity player;
    private boolean cancelled;

    public EntitySpawnLogicEvent(Entity entity, World world, PlayerEntity player) {
        super(entity);
        this.world = world;
        this.player = player;
    }

    public World getWorld() {
        return world;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
