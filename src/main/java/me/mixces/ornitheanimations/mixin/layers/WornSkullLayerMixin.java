package me.mixces.ornitheanimations.mixin.layers;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.mixces.ornitheanimations.config.Config;
import net.minecraft.client.render.entity.layer.WornSkullLayer;
import net.minecraft.entity.living.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WornSkullLayer.class)
public abstract class WornSkullLayerMixin {

	@WrapOperation(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/entity/living/LivingEntity;isSneaking()Z"
		)
	)
	private boolean ornitheAnimations$disableSneakTranslation(LivingEntity instance, Operation<Boolean> original) {
		return !Config.INSTANCE.getSMOOTH_SNEAKING().get() && original.call(instance);
	}
}
