package me.mixces.ornitheanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.mixces.ornitheanimations.config.Config;
import net.minecraft.client.entity.particle.EntityPickupParticle;
import net.minecraft.entity.Entity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityPickupParticle.class)
public abstract class EntityPickupParticleMixin {

	@Shadow
	private Entity collector;

	@ModifyExpressionValue(
		method = "render",
		at = @At(
			value = "FIELD",
			opcode = Opcodes.GETFIELD,
			target = "Lnet/minecraft/entity/Entity;prevTickY:D"
		)
	)
	private double ornitheAnimations$includeEyeHeight$PrevTickY(double original) {
		if (Config.INSTANCE.getOLD_ITEM_PICKUP().get()) {
			/* taken from 1.7 */
			original += collector.getEyeHeight();
		}
		return original;
	}

	@ModifyExpressionValue(
		method = "render",
		at = @At(
			value = "FIELD",
			opcode = Opcodes.GETFIELD,
			target = "Lnet/minecraft/entity/Entity;y:D",
			ordinal = 1
		)
	)
	private double ornitheAnimations$includeEyeHeight$Y(double original) {
		if (Config.INSTANCE.getOLD_ITEM_PICKUP().get()) {
			/* taken from 1.7 */
			original += collector.getEyeHeight();
		}
		return original;
	}
}
