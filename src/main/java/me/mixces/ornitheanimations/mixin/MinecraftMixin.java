package me.mixces.ornitheanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.mixces.ornitheanimations.OrnitheAnimations;
import net.minecraft.client.ClientPlayerInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.living.player.LocalClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.HitResult;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

	@Shadow
	public LocalClientPlayerEntity player;

	@Shadow
	public HitResult crosshairTarget;

	@Shadow
	public ClientWorld world;

	@Shadow
	public ClientPlayerInteractionManager interactionManager;

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
		return !OrnitheAnimations.INSTANCE.getConfig().getBLOCK_HITTING().get() && original;
	}

	@ModifyExpressionValue(
		method = "doUse",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/ClientPlayerInteractionManager;isMiningBlock()Z"
		)
	)
	private boolean ornitheAnimations$disableIsHittingCheck(boolean original) {
		return !OrnitheAnimations.INSTANCE.getConfig().getBLOCK_HITTING().get() && original;
	}

	@Inject(
		method = "doAttack",
		at = @At("HEAD")
	)
	private void ornitheAnimations$oldMissPenalty(CallbackInfo ci) {
		if (!OrnitheAnimations.INSTANCE.getConfig().getOLD_MISS_PENALTY().get()) {
			return;
		}
		if (this.crosshairTarget != null && this.crosshairTarget.type != HitResult.Type.BLOCK) {
			attackCooldown = 0;
		}
	}
}
