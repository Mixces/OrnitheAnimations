package me.mixces.ornitheanimations.mixin;

import me.mixces.ornitheanimations.config.Config;
import me.mixces.ornitheanimations.util.GlHelper;
import net.minecraft.client.render.entity.ProjectileRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProjectileRenderer.class)
public abstract class ProjectileRendererMixin {

	@Inject(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/blaze3d/platform/GlStateManager;translatef(FFF)V"
		)
	)
	private void ornitheAnimations$includeEyeHeight(Entity entity, double dx, double dy, double dz, float yaw, float tickDelta, CallbackInfo ci) {
		if (Config.INSTANCE.getMIRRORED_PROJECTILES().get()) {
			/* 1.7's projectile position is suspiciously raised by the player's eyeheight minus the projectile y */
			GlHelper.translate(0.0F, 0.12F, 0.0F);
		}
	}

	@ModifyArg(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/blaze3d/platform/GlStateManager;rotatef(FFFF)V",
			ordinal = 0
		),
		index = 0
    )
    private float ornitheAnimations$rotateProjectile(float angle) {
        return angle + (Config.INSTANCE.getMIRRORED_PROJECTILES().get() ? 180.0F : 0.0F);
    }

    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/GlStateManager;rotatef(FFFF)V",
                    ordinal = 1
            ),
            index = 0
    )
    private float ornitheAnimations$useProperCameraView(float angle) {
        return angle * (Config.INSTANCE.getMIRRORED_PROJECTILES().get() ? -1 : 1);
    }

	@Inject(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/item/ItemRenderer;renderHeldItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/block/ModelTransformations$Type;)V"
		)
	)
	private void ornitheAnimations$applyProjectilePosition(Entity entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
		if (Config.INSTANCE.getMIRRORED_PROJECTILES().get()) {
			/* item entities already have this translation which matches item rendering to 1.7 */
			GlHelper.translate(0.0F, 0.25F, 0.0F);
		}

		if (Config.INSTANCE.getFAST_ITEMS().get()) {
			/* half of a pixel, matches 1.7's sprite rendering */
			GlHelper.translate(0.0F, 0.0F, 0.03125F);
		}
	}
}
