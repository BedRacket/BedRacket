package org.bedracket.mixin.bugfix;

import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.Vec3d;
import org.bedracket.util.BedRacketConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BoatEntity.class)
public class MixinBoatEntity {

    @Shadow private BoatEntity.Location location;

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo info) {
        if (location == BoatEntity.Location.UNDER_FLOWING_WATER) {
            final Vec3d motion = ((Entity) (Object) this).getVelocity();
            ((Entity) (Object) this).setVelocity(
                    motion.x,
                    motion.y + 0.0007 + BedRacketConfig.boatBuoyancyUnderFlowingWater,
                    motion.z
            );
        }
    }

    @ModifyConstant(
            method = {"tick", "interact"}, constant = @Constant(floatValue = 60.0F)
    )
    private float getUnderwaterBoatPassengerEjectionDelay(float delay) {
        final int newDelay = BedRacketConfig.underwaterBoatPassengerEjectionDelayTicks;
        return newDelay == -1 ? Float.MAX_VALUE : newDelay;
    }

    @Redirect(method = "fall", at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/vehicle/BoatEntity;" +
                    "location:Lnet/minecraft/entity/vehicle/BoatEntity$Location;"
    ))
    private BoatEntity.Location getLocation(BoatEntity boat) {
        return BedRacketConfig.fixBoatFallDamage ?
                BoatEntity.Location.ON_LAND : location;
    }
}
