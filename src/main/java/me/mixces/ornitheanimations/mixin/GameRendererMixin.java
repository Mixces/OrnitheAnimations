package me.mixces.ornitheanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.mixces.ornitheanimations.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

	@Shadow
	private Minecraft minecraft;

	@ModifyArg(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/world/ClientWorld;getBrightness(Lnet/minecraft/util/math/BlockPos;)F"
		)
	)
	private BlockPos ornitheAnimations$oldEyeLocation(BlockPos par1) {
		if (Config.INSTANCE.getOLD_RENDER_TICK_DELAY().get()) {
			final Vec3d eyeVec = minecraft.getCamera().getEyePosition(1.0F);
			return par1.add(eyeVec.x - par1.getX(), eyeVec.y - par1.getY(), eyeVec.z - par1.getZ());
		}
		return par1;
	}

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
