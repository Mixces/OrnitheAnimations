package me.mixces.ornitheanimations.mixin.layers;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.mixces.ornitheanimations.OrnitheAnimations;
import net.minecraft.client.render.entity.layer.WornSkullLayer;
import net.minecraft.entity.living.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WornSkullLayer.class)
public class WornSkullLayerMixin {

	@WrapOperation(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/entity/living/LivingEntity;isSneaking()Z"
		)
	)
	private boolean ornitheAnimations$disableSneakTranslation(LivingEntity instance, Operation<Boolean> original) {
		return !OrnitheAnimations.INSTANCE.getConfig().getSMOOTH_SNEAKING().get() && original.call(instance);
	}
}
