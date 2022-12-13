package org.bedracket.entity_events.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.village.raid.Raid;
import org.bedracket.eventbus.Cancellable;

public class PlayerRaidEvent extends PlayerEvent implements Cancellable {

    private final Raid raid;
    private boolean cancel;

    public PlayerRaidEvent(PlayerEntity player, Raid raid) {
        super(player);
        this.raid = raid;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public Raid getRaid() {
        return raid;
    }
}
