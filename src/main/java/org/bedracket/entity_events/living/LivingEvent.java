package org.bedracket.entity_events.living;

import net.minecraft.entity.LivingEntity;
import org.bedracket.eventbus.Event;

public class LivingEvent extends Event {

    private final LivingEntity livingEntity;

    public LivingEvent(LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
    }

    public LivingEntity getLivingEntity() {
        return livingEntity;
    }
}
