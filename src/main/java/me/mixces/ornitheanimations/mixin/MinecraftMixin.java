package me.mixces.ornitheanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.mixces.ornitheanimations.OrnitheAnimations;
import net.minecraft.client.ClientPlayerInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.living.player.LocalClientPlayerEntity;
import net.minecraft.client.entity.particle.ParticleManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
	private int attackCooldown;

	@Shadow
	public ClientWorld world;

	@Shadow
	public ParticleManager particleManager;

	@Shadow
	public ClientPlayerInteractionManager interactionManager;

	@Inject(
		method = "tickBlockMining",
		at = @At("HEAD")
	)
	private void ornitheAnimations$fakeSwingDuringBlockhit(boolean holdingAttack, CallbackInfo ci) {
		if (!OrnitheAnimations.INSTANCE.getConfig().getBLOCK_HITTING().get()) {
			return;
		}
		if (attackCooldown <= 0 && player.isUsingItem() && holdingAttack &&
			crosshairTarget != null && crosshairTarget.type == HitResult.Type.BLOCK) {
			BlockPos blockPos = crosshairTarget.getPos();
			if (!world.isAir(blockPos)) {
				ornitheAnimations$stopMiningBlock();
				particleManager.addBlockMiningParticles(blockPos, crosshairTarget.face);
				ornitheAnimations$swingHand();
			}
		}
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
		if (crosshairTarget != null && crosshairTarget.type != HitResult.Type.BLOCK) {
			attackCooldown = 0;
		}
	}

	@Unique
	private void ornitheAnimations$swingHand() {
		/* fake swing :) */
		int handMultiplier = ((LivingEntityAccessor) player).invokeGetMiningSpeedMultiplier() / 2;
		if (!player.handSwinging || player.handSwingTicks >= handMultiplier || player.handSwingTicks < 0) {
			player.handSwingTicks = -1;
			player.handSwinging = true;
		}
	}

	@Unique
	private void ornitheAnimations$stopMiningBlock() {
		/* visually aborts mining without sending a mining abortion packet */
		ClientPlayerInteractionManagerAccessor accessor = ((ClientPlayerInteractionManagerAccessor) interactionManager);
		if (accessor.getMiningProgress() > 0 && accessor.getIsMiningBlock()) {
			accessor.setIsMiningBlock(false);
			accessor.setMiningProgress(0.0F);
			world.updateBlockMiningProgress(player.getNetworkId(), accessor.getTarget(), -1);
		}
	}
}
