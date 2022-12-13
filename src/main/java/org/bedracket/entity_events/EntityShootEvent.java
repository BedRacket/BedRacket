package org.bedracket.entity_events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;
import org.bedracket.eventbus.Cancellable;

public class EntityShootEvent extends EntityEvent implements Cancellable {

    private final World world;
    private final ProjectileEntity projectile;
    private boolean cancelled;

    public EntityShootEvent(Entity entity, World world, ProjectileEntity projectile) {
        super(entity);
        this.world = world;
        this.projectile = projectile;
    }

    public World getWorld() {
        return world;
    }

    public ProjectileEntity getProjectile() {
        return projectile;
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
