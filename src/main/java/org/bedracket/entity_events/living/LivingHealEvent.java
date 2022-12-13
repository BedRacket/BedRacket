package org.bedracket.entity_events.living;

import net.minecraft.entity.LivingEntity;
import org.bedracket.eventbus.Cancellable;

public class LivingHealEvent extends LivingEvent implements Cancellable {

    private float amount;
    private boolean cancelled = false;

    public LivingHealEvent(LivingEntity livingEntity, float amount) {
        super(livingEntity);
        this.setAmount(amount);
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
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
