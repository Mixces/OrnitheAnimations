package me.mixces.ornitheanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.mixces.ornitheanimations.OrnitheAnimations;
import net.minecraft.client.render.entity.FishingBobberRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(FishingBobberRenderer.class)
public class FishingBobberRendererMixin {

	@ModifyArgs(
		method = "render(Lnet/minecraft/entity/FishingBobberEntity;DDDFF)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/math/Vec3d;<init>(DDD)V"
		)
	)
	private void ornitheAnimations$modifyLinePosition(Args args) {
		if (OrnitheAnimations.config.getOLD_ITEM_POSITIONS().get()) {
		/* original values from 1.7 */
		args.set(0, -0.5D);
		args.set(2, 0.8D);
		}
	}

	@ModifyExpressionValue(
		method = "render(Lnet/minecraft/entity/FishingBobberEntity;DDDFF)V",
		at = @At(
			value = "CONSTANT",
			args = "doubleValue=0.8D"
		)
	)
	public double ornitheAnimations$moveLinePosition(double constant) {
		/* original values from 1.7 */
		return constant + (OrnitheAnimations.config.getOLD_ITEM_POSITIONS().get() ? 0.05D : 0.0D);
	}

	@ModifyExpressionValue(
		method = "render(Lnet/minecraft/entity/FishingBobberEntity;DDDFF)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/entity/living/player/PlayerEntity;isSneaking()Z"
		)
	)
	public boolean ornitheAnimations$removeSneakTranslation(boolean original) {
		return !OrnitheAnimations.config.getOLD_ITEM_POSITIONS().get() && original;
	}
}
