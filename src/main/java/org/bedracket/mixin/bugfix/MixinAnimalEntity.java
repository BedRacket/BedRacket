package org.bedracket.mixin.bugfix;

import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.bedracket.util.BedRacketConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AnimalEntity.class)
public class MixinAnimalEntity {

    @Redirect(method = "tickMovement", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;addParticle" +
                    "(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"
    ))
    private void addParticle(
            World world, ParticleEffect effect, double x, double y, double z,
            double xOffset, double yOffset, double zOffset
    ) {
        if (!world.isClient && BedRacketConfig.fixAnimalBreedingHearts) {
            //addParticle is not implemented in ServerWorld.
            ((ServerWorld) world).spawnParticles(
                    effect, x, y, z, 1, xOffset, yOffset, zOffset, 0.0
            );
        } else {
            world.addParticle(effect, x, y, z, xOffset, yOffset, zOffset);
        }
    }
}
