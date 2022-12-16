package org.bedracket.entity_events.player;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import org.bedracket.eventbus.Cancellable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerBedFailEnterEvent extends PlayerEvent implements Cancellable {

    private final FailReason failReason;
    private final Block bed;
    private boolean willExplode;
    private boolean cancelled;

    public PlayerBedFailEnterEvent(@NotNull PlayerEntity player, @NotNull FailReason failReason, @NotNull Block bed, boolean willExplode) {
        super(player);
        this.failReason = failReason;
        this.bed = bed;
        this.willExplode = willExplode;
    }

    @NotNull
    public FailReason getFailReason() {
        return failReason;
    }

    @NotNull
    public Block getBed() {
        return bed;
    }

    public boolean getWillExplode() {
        return willExplode;
    }

    public void setWillExplode(boolean willExplode) {
        this.willExplode = willExplode;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Cancel this event.
     * <p>
     * <b>NOTE: This does not cancel the player getting in the bed, but any messages/explosions
     * that may occur because of the interaction.</b>
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    public static enum FailReason {
        /**
         * The world doesn't allow sleeping (ex. Nether or The End). Entering
         * the bed is prevented and the bed explodes.
         */
        NOT_POSSIBLE_HERE,
        /**
         * Entering the bed is prevented due to it not being night nor
         * thundering currently.
         * <p>
         * If the event is forcefully allowed during daytime, the player will
         * enter the bed (and set its bed location), but might get immediately
         * thrown out again.
         */
        NOT_POSSIBLE_NOW,
        /**
         * Entering the bed is prevented due to the player being too far away.
         */
        TOO_FAR_AWAY,
        /**
         * Bed is obstructed.
         */
        OBSTRUCTED,
        /**
         * Entering the bed is prevented due to there being some other problem.
         */
        OTHER_PROBLEM,
        /**
         * Entering the bed is prevented due to there being monsters nearby.
         */
        NOT_SAFE;

        public static final FailReason[] VALUES = values();
    }
}
