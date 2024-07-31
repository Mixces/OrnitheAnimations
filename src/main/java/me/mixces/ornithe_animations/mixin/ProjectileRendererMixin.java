package me.mixces.ornithe_animations.mixin;

import me.mixces.ornithe_animations.util.GlHelper;
import net.minecraft.client.render.entity.ProjectileRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProjectileRenderer.class)
public abstract class ProjectileRendererMixin {

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
        return angle + 180.0F;
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
        return -angle;
    }

	@Inject(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/item/ItemRenderer;renderHeldItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/block/ModelTransformations$Type;)V"
		)
	)
	private void ornitheAnimations$projectilePosition(Entity entity, double dx, double dy, double dz, float yaw, float tickDelta, CallbackInfo ci) {
		GlHelper.getBuilder().translate(0.0F, 0.25F, 0.0F);
	}
}
