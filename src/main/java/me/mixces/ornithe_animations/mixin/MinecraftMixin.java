package me.mixces.ornithe_animations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.options.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

	@Shadow
	public Screen screen;

	@Shadow
	public GameOptions options;

	@Shadow
	public boolean focused;

	@Shadow
	private int attackCooldown;

	@ModifyExpressionValue(
		method = "tickBlockMining",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/entity/living/player/LocalClientPlayerEntity;isUsingItem()Z"
		)
	)
	private boolean ornitheAnimations$disableUsingItemCheck(boolean original) {
		return false;
	}

	@ModifyExpressionValue(
		method = "doUse",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/ClientPlayerInteractionManager;isMiningBlock()Z"
		)
	)
	private boolean ornitheAnimations$disableIsHittingCheck(boolean original) {
		return false;
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/entity/living/player/LocalClientPlayerEntity;isUsingItem()Z",
			ordinal = 0
		)
	)
	private void ornitheAnimations$addLeftClickCheck(CallbackInfo ci) {
		if (screen != null || !options.attackKey.isPressed() || !focused) {
			attackCooldown = 0;
		}
	}
}
