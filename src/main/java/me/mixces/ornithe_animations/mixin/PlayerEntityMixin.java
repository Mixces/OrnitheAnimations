package me.mixces.ornithe_animations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.living.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {

	@Shadow
	public abstract boolean isUsingItem();

	@ModifyExpressionValue(
		method = "getEyeHeight",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/entity/living/player/PlayerEntity;isSneaking()Z"
		)
	)
	private boolean ornitheAnimations$disableIsSneakingCheck(boolean original) {
		return false;
	}
}
