package me.mixces.ornithe_animations.mixin;

import net.minecraft.entity.living.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {

	@Shadow
	protected abstract int getMiningSpeedMultiplier();

	@Unique
	public int ornitheAnimations$getMiningSpeedMultiplier() {
		return getMiningSpeedMultiplier();
	}
}
