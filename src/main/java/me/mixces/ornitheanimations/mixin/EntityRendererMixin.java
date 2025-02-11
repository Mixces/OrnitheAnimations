package me.mixces.ornitheanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.GlStateManager;
import me.mixces.ornitheanimations.OrnitheAnimations;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {

	@ModifyExpressionValue(
		method = "renderOnFire",
		at = @At(
			value = "FIELD",
			opcode = Opcodes.GETFIELD,
			target = "Lnet/minecraft/entity/Entity;y:D"
		)
	)
	private double ornitheAnimations$includeEyeHeight$Y(double original, @Local(argsOnly = true) Entity entity) {
		if (OrnitheAnimations.INSTANCE.getConfig().getOLD_FLAME_OFFSET().get()) {
			original += entity.getEyeHeight();
		}
		return original;
	}

	@WrapOperation(
		method = "postRender",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/entity/EntityRenderer;renderOnFire(Lnet/minecraft/entity/Entity;DDDF)V"
		)
	)
	private void ornitheAnimations$includeEyeHeight$renderFire(EntityRenderer<?> instance, Entity entity, double dx, double dy, double dz, float tickDelta, Operation<Void> original) {
		boolean oldFlameHeight = OrnitheAnimations.INSTANCE.getConfig().getOLD_FLAME_OFFSET().get();
		if (oldFlameHeight) {
			GlStateManager.pushMatrix();
			GlStateManager.translatef(0.0F, entity.getEyeHeight(), 0.0F);
		}

		original.call(instance, entity, dx, dy, dz, tickDelta);

		if (oldFlameHeight) {
			GlStateManager.popMatrix();
		}
	}
}
