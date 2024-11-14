package me.mixces.ornitheanimations.mixin.damagelayers;

import me.mixces.ornitheanimations.OrnitheAnimations;
import me.mixces.ornitheanimations.hook.DamageTint;
import me.mixces.ornitheanimations.shared.IDamageTint;
import net.minecraft.client.render.entity.PigRenderer;
import net.minecraft.client.render.entity.layer.PigSaddleLayer;
import net.minecraft.client.render.model.entity.PigModel;
import net.minecraft.entity.living.mob.passive.animal.PigEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PigSaddleLayer.class)
public abstract class PigSaddleLayerMixin {

	@Shadow
	@Final
	private PigModel model;

	@Shadow
	@Final
	private PigRenderer parent;

	@Inject(
            method = "render(Lnet/minecraft/entity/living/mob/passive/animal/PigEntity;FFFFFFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/model/entity/PigModel;render(Lnet/minecraft/entity/Entity;FFFFFF)V",
                    shift = At.Shift.AFTER
            )
    )
    public void ornitheAnimations$addDamageBrightness(PigEntity pigEntity, float f, float g, float h, float i, float j, float k, float l, CallbackInfo ci) {
		/* colors the entity's layer red just like 1.7 */
		if (!OrnitheAnimations.config.getALTERNATIVE_DAMAGE_TINT().get()) {
			return;
		}
		if (((IDamageTint) parent).setupOverlayColor(pigEntity, h)) {
			model.render(pigEntity, f, g, i, j, k, l);
			DamageTint.unsetDamageTint();
		}
    }
}
