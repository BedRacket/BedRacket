package org.bedracket.mixin.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.world.World;
import org.bedracket.entity_events.living.monster.SlimeSplitEvent;
import org.bedracket.eventbus.BedRacket;
import org.bedracket.eventbus.EventException;
import org.bedracket.eventbus.EventInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@EventInfo(events = "SlimeSplitEvent")
@Mixin(SlimeEntity.class)
public abstract class MixinSlimeEntity extends MobEntity implements Monster {

    @Shadow public abstract void remove(RemovalReason reason);

    private final Random randoms = new Random();
    private boolean cancelRemove_B;
    private List<LivingEntity> slimes_B = new ArrayList<>();

    protected MixinSlimeEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"), method = "remove")
    private int callSlimeSplitEvent(net.minecraft.util.math.random.Random instance, int i) throws EventException {
        slimes_B.clear();
        int k = 2 + this.randoms.nextInt(3);
        SlimeSplitEvent brEvent =
                (SlimeSplitEvent) BedRacket.EVENT_BUS.post(SlimeSplitEvent.class,
                        new SlimeSplitEvent(((SlimeEntity) (Object) this), k));
        if (!brEvent.isCancelled() && brEvent.getCount() > 0) {
            return brEvent.getCount() - 2;
        } else {
            cancelRemove_B = true;
        }
        return k - 2;
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"), method = "remove", cancellable = true)
    public void callSlimeSplitEvent2(RemovalReason reason, CallbackInfo ci) {
        if (cancelRemove_B) {
            super.remove(reason);
            ci.cancel();
            return;
        }
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"), method = "remove")
    public boolean redirectSpawnEntity(World w, Entity e) {
        this.slimes_B.add((SlimeEntity)e);
        return false;
    }

}
