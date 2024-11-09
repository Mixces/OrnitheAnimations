package me.mixces.ornitheanimations.mixin;

import net.minecraft.entity.living.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {

	@Shadow
	protected abstract int getMiningSpeedMultiplier();

	@Shadow
	public boolean handSwinging;

	@Shadow
	public int handSwingTicks;

	@Unique
	public int ornitheAnimations$getMiningSpeedMultiplier() {
		return getMiningSpeedMultiplier();
	}
}
