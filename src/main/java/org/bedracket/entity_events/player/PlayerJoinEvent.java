package org.bedracket.entity_events.player;

import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player joins a server
 */
public class PlayerJoinEvent extends PlayerEvent {

    private String joinMessage;

    public PlayerJoinEvent(@NotNull final PlayerEntity playerJoined) {
        super(playerJoined);
    }

}
