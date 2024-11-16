package me.mixces.ornitheanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.mixces.ornitheanimations.OrnitheAnimations;
import net.minecraft.entity.living.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
		return !OrnitheAnimations.INSTANCE.getConfig().getSMOOTH_SNEAKING().get() && original;
	}

	@Inject(
		method = "hasReducedDebugInfo",
		at = @At("HEAD"),
		cancellable = true
	)
	public void ornitheAnimations$forceReducedDebug(CallbackInfoReturnable<Boolean> cir) {
		if (OrnitheAnimations.INSTANCE.getConfig().getOLD_DEBUG_MENU().get()) {
			cir.setReturnValue(true);
		}
	}
}
