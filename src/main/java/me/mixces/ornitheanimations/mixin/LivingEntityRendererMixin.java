package me.mixces.ornitheanimations.mixin;

import me.mixces.ornitheanimations.hook.DamageTint;
import me.mixces.ornitheanimations.shared.IDamageTint;
import me.mixces.ornitheanimations.util.GlHelper;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.FloatBuffer;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin implements IDamageTint {

	@Shadow
	protected abstract void renderHand(LivingEntity entity, float handSwing, float handSwingAmount, float age, float yaw, float pitch, float scale);

	@Shadow
	protected abstract boolean setupOverlayColor(LivingEntity entity, float tickDelta, boolean bl);

	@Shadow
	protected abstract void tearDownOverlayColor();

	@Shadow
	protected abstract int getOverlayColor(LivingEntity entity, float f, float timeDelta);

	@Shadow
	protected FloatBuffer tintBuffer;

	@Unique
	private static final ThreadLocal<Float> ornitheAnimations$h = ThreadLocal.withInitial(() -> 0.0f);

	@Inject(
		method = "render(Lnet/minecraft/entity/living/LivingEntity;DDDFF)V",
		at = @At("HEAD")
	)
	private void ornitheAnimations$capturePartialTicks(LivingEntity livingEntity, double d, double e, double f, float g, float h, CallbackInfo ci) {
		ornitheAnimations$h.set(h);
	}

	@Inject(
		method = "render(Lnet/minecraft/entity/living/LivingEntity;DDDFF)V",
		at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/blaze3d/platform/GlStateManager;translatef(FFF)V"
		)
    )
    private void ornitheAnimations$addSneakingTranslation(LivingEntity entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (entity instanceof PlayerEntity && entity.isSneaking()) {
			GlHelper.INSTANCE.translate(0.0F, -0.2F, 0.0F);
        }
    }

	@Redirect(
		method = "render(Lnet/minecraft/entity/living/LivingEntity;DDDFF)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;renderHand(Lnet/minecraft/entity/living/LivingEntity;FFFFFF)V",
			ordinal = 1
		)
	)
	private void ornitheAnimations$cancelDamageBrightness(LivingEntityRenderer<LivingEntity> instance, LivingEntity entity, float handSwing, float handSwingAmount, float age, float yaw, float pitch, float scale) {
		renderHand(entity, handSwing, handSwingAmount, age, yaw, pitch, scale);
		if (setupOverlayColor(entity, ornitheAnimations$h.get())) {
			renderHand(entity, handSwing, handSwingAmount, age, yaw, pitch, scale);
			DamageTint.unsetDamageTint();
		}
	}

	@Inject(
		method = "render(Lnet/minecraft/entity/living/LivingEntity;DDDFF)V",
		at = @At("TAIL")
	)
	private void ornitheAnimations$clearThreadLocalPartialTicks(CallbackInfo ci) {
		/* we need to clear the threadlocal */
		ornitheAnimations$h.remove();
	}

	@Redirect(
		method = "render(Lnet/minecraft/entity/living/LivingEntity;DDDFF)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;setupOverlayColor(Lnet/minecraft/entity/living/LivingEntity;F)Z"
		)
	)
	private boolean ornitheAnimations$cancelDamageBrightness(LivingEntityRenderer<LivingEntity> instance, LivingEntity entity, float tickDelta) {
		/* cancel model damage tint */
		return false;
	}

	@Redirect(
		method = "renderLayers",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;setupOverlayColor(Lnet/minecraft/entity/living/LivingEntity;FZ)Z"
		)
	)
	private boolean ornitheAnimations$cancelDamageBrightness2(LivingEntityRenderer<LivingEntity> instance, LivingEntity entity, float tickDelta, boolean bl) {
		/* cancel layer damage tint */
		return false;
	}

	@SuppressWarnings("AddedMixinMembersNamePattern")
	@Override
	public boolean setupOverlayColor(LivingEntity livingEntity, float partialTicks) {
		/* trick to ensure the brightnessBuffer is updated*/
		if (setupOverlayColor(livingEntity, partialTicks, true)) tearDownOverlayColor();
		/* if there are any performance issues, blame this */

		final float f = livingEntity.getBrightness(partialTicks);
		final int i = getOverlayColor(livingEntity, f, partialTicks);
		final boolean flag = (i >> 24 & 0xFF) > 0;
		final boolean flag1 = livingEntity.hurtTime > 0 || livingEntity.deathTime > 0;

		if (!flag && !flag1) {
			return false;
		} else {
			DamageTint.setDamageTint(tintBuffer);
			return true;
		}
	}
}
