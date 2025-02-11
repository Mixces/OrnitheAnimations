package me.mixces.ornitheanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.mixces.ornitheanimations.config.Config;
import net.minecraft.client.render.GameRenderer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

	@ModifyExpressionValue(
		method = "applyHurtCam",
		at = @At(
			value = "FIELD",
			opcode = Opcodes.GETFIELD,
			target = "Lnet/minecraft/entity/living/LivingEntity;hurtTime:I"
		)
	)
	private int ornitheAnimations$oldTickDelay(int original) {
		if (Config.INSTANCE.getOLD_RENDER_TICK_DELAY().get()) {
			return Math.max(original - 1, 0);
		}
		return original;
	}
}
