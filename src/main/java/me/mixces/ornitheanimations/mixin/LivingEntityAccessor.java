package me.mixces.ornitheanimations.mixin;

import net.minecraft.entity.living.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {

	@Invoker
	int invokeGetMiningSpeedMultiplier();
}
