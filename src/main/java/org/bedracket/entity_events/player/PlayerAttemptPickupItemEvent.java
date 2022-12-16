package org.bedracket.entity_events.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import org.bedracket.eventbus.Cancellable;
import org.jetbrains.annotations.NotNull;

/**
 * Thrown when a player attempts to pick an item up from the ground
 */
public class PlayerAttemptPickupItemEvent extends PlayerEvent implements Cancellable {

    @NotNull private final Item item;
    private boolean flyAtPlayer = true;
    private boolean isCancelled = false;
    private final int remaining;

    @Deprecated
    public PlayerAttemptPickupItemEvent(@NotNull final PlayerEntity player, @NotNull final Item item) {
        this(player, item, 0);
    }

    public PlayerAttemptPickupItemEvent(@NotNull final PlayerEntity player, @NotNull final Item item, final int remaining) {
        super(player);
        this.item = item;
        this.remaining = remaining;
    }

    /**
     * Gets the Item attempted by the player.
     *
     * @return Item
     */
    @NotNull
    public Item getItem() {
        return item;
    }

    /**
     * Gets the amount that will remain on the ground, if any
     *
     * @return amount that will remain on the ground
     */
    public int getRemaining() {
        return remaining;
    }

    /**
     * Set if the item will fly at the player
     * <p>Cancelling the event will set this value to false.</p>
     *
     * @param flyAtPlayer True for item to fly at player
     */
    public void setFlyAtPlayer(boolean flyAtPlayer) {
        this.flyAtPlayer = flyAtPlayer;
    }

    /**
     * Gets if the item will fly at the player
     *
     * @return True if the item will fly at the player
     */
    public boolean getFlyAtPlayer() {
        return this.flyAtPlayer;
    }


    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
        this.flyAtPlayer = !cancel;
    }

}
