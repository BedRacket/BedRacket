package org.bedracket.entity_events.living.monster;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SlimeEntity;
import org.bedracket.entity_events.EntityEvent;
import org.bedracket.eventbus.Cancellable;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a Slime splits into smaller Slimes upon death
 */
public class SlimeSplitEvent extends EntityEvent implements Cancellable {

    private boolean cancel = false;
    private int count;

    public SlimeSplitEvent(@NotNull final SlimeEntity slime, final int count) {
        super(slime);
        this.count = count;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @NotNull
    @Override
    public SlimeEntity getEntity() {
        return (SlimeEntity) entity;
    }

    /**
     * Gets the EntityType of the Entity involved in this event.
     *
     * @return EntityType of the Entity involved in this event
     */
    @NotNull
    public EntityType<?> getEntityType() {
        return entity.getType();
    }


    /**
     * Gets the amount of smaller slimes to spawn
     *
     * @return the amount of slimes to spawn
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets how many smaller slimes will spawn on the split
     *
     * @param count the amount of slimes to spawn
     */
    public void setCount(int count) {
        this.count = count;
    }
}
