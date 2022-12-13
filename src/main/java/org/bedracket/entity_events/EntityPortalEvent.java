package org.bedracket.entity_events;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bedracket.eventbus.Cancellable;

public class EntityPortalEvent extends EntityEvent implements Cancellable {

    private final BlockPos pos;
    private final World world;
    private final BlockState state;
    private boolean cancelled;

    public EntityPortalEvent(Entity entity, BlockPos pos, World world, BlockState state) {
        super(entity);
        this.pos = pos;
        this.world = world;
        this.state = state;
    }

    public BlockPos getPos() {
        return pos;
    }

    public World getWorld() {
        return world;
    }

    public BlockState getState() {
        return state;
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
