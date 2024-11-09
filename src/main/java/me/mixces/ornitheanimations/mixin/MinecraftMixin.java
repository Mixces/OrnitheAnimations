package me.mixces.ornitheanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.mixces.ornitheanimations.OrnitheAnimations;
import me.mixces.ornitheanimations.shared.ISwing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.living.player.LocalClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

	@Shadow
	private int attackCooldown;

	@Shadow
	public LocalClientPlayerEntity player;

	@ModifyExpressionValue(
		method = "tickBlockMining",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/entity/living/player/LocalClientPlayerEntity;isUsingItem()Z"
		)
	)
	private boolean ornitheAnimations$disableUsingItemCheck(boolean original) {
		return !OrnitheAnimations.config.getBLOCK_HITTING().get() && original;
	}

	@ModifyExpressionValue(
		method = "doUse",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/ClientPlayerInteractionManager;isMiningBlock()Z"
		)
	)
	private boolean ornitheAnimations$disableIsHittingCheck(boolean original) {
		return !OrnitheAnimations.config.getBLOCK_HITTING().get() && original;
	}

	@Inject(
		method = "doAttack",
		at = @At(
			value = "RETURN",
			ordinal = 0
		)
	)
	private void ornitheAnimations$addLeftClickCheck(CallbackInfo ci) {
		if (OrnitheAnimations.config.getHIDE_MISS_PENALTY().get()) {
			/* fake swing during miss penalty to appear like 1.7 */
			((ISwing) player).fakeSwingItem();
		}
	}
}
