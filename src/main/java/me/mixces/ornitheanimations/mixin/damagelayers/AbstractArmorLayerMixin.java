package me.mixces.ornitheanimations.mixin.damagelayers;

import me.mixces.ornitheanimations.hook.DamageTint;
import me.mixces.ornitheanimations.shared.IDamageTint;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.layer.AbstractArmorLayer;
import net.minecraft.client.render.model.Model;
import net.minecraft.entity.living.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArmorLayer.class)
public abstract class AbstractArmorLayerMixin {

	@Shadow
	public abstract Model getModel(int equipmentSlot);

	@Shadow
	@Final
	private LivingEntityRenderer<?> parent;

	@Inject(
            method = "renderArmor",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/model/Model;render(Lnet/minecraft/entity/Entity;FFFFFF)V",
                    shift = At.Shift.AFTER
            )
    )
    private void ornitheAnimations$addDamageBrightness(LivingEntity entity, float handSwingAmount, float handSwing, float tickDelta, float age, float headYaw, float headPitch, float scale, int equipmentSlot, CallbackInfo ci) {
		/* colors the armor pieces red just like 1.7 */
		if (((IDamageTint) parent).setupOverlayColor(entity, tickDelta)) {
			getModel(equipmentSlot).render(entity, handSwingAmount, handSwing, age, headYaw, headPitch, scale);
			DamageTint.unsetDamageTint();
		}
    }
}
