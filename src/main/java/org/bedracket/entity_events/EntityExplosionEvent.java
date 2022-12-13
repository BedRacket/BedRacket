package org.bedracket.entity_events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.bedracket.eventbus.Cancellable;

public class EntityExplosionEvent extends EntityEvent implements Cancellable {

    private final DamageSource damageSource;
    private final ExplosionBehavior behavior;
    private final double explodeX;
    private final double explodeY;
    private final double explodeZ;
    private final float power;
    private final boolean createFire;
    private final World.ExplosionSourceType explosionSourceType;
    private boolean cancelled;


    public EntityExplosionEvent(Entity entity, DamageSource damageSource, ExplosionBehavior behavior, double explodeX, double explodeY, double explodeZ, float power, boolean createFire, World.ExplosionSourceType explosionSourceType) {
        super(entity);
        this.damageSource = damageSource;
        this.behavior = behavior;
        this.explodeX = explodeX;
        this.explodeY = explodeY;
        this.explodeZ = explodeZ;
        this.power = power;
        this.createFire = createFire;
        this.explosionSourceType = explosionSourceType;
    }

    public DamageSource getDamageSource() {
        return damageSource;
    }

    public ExplosionBehavior getBehavior() {
        return behavior;
    }

    public double getExplodeX() {
        return explodeX;
    }

    public double getExplodeY() {
        return explodeY;
    }

    public double getExplodeZ() {
        return explodeZ;
    }

    public float getPower() {
        return power;
    }

    public boolean isCreateFire() {
        return createFire;
    }

    public World.ExplosionSourceType getExplosionSourceType() {
        return explosionSourceType;
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
