package org.bedracket.entity_events.living;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.bedracket.eventbus.Cancellable;

public class LivingHurtEvent extends LivingEvent implements Cancellable {

    private final DamageSource source;
    private float amount;
    private boolean cancelled = false;

    public LivingHurtEvent(LivingEntity livingEntity, DamageSource source, float amount) {
        super(livingEntity);
        this.source = source;
        this.amount = amount;
    }

    public DamageSource getSource() {
        return source;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setDamage(float amount) {
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
