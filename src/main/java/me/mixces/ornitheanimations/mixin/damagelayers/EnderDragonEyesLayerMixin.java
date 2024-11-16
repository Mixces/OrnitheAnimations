package me.mixces.ornitheanimations.mixin.damagelayers;

import me.mixces.ornitheanimations.OrnitheAnimations;
import me.mixces.ornitheanimations.hook.DamageTint;
import me.mixces.ornitheanimations.shared.IDamageTint;
import net.minecraft.client.render.entity.EnderDragonRenderer;
import net.minecraft.client.render.entity.layer.EnderDragonEyesLayer;
import net.minecraft.entity.living.mob.hostile.boss.EnderDragonEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragonEyesLayer.class)
public abstract class EnderDragonEyesLayerMixin {

	@Shadow
	@Final
	private EnderDragonRenderer parent;

	@Inject(
            method = "render(Lnet/minecraft/entity/living/mob/hostile/boss/EnderDragonEntity;FFFFFFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/model/Model;render(Lnet/minecraft/entity/Entity;FFFFFF)V",
                    shift = At.Shift.AFTER
            )
    )
    public void ornitheAnimations$addDamageBrightness(EnderDragonEntity enderDragonEntity, float f, float g, float h, float i, float j, float k, float l, CallbackInfo ci) {
		/* colors the entity's layer red just like 1.7 */
		if (!OrnitheAnimations.INSTANCE.getConfig().getALTERNATIVE_DAMAGE_TINT().get()) {
			return;
		}
		if (((IDamageTint) parent).setupOverlayColor(enderDragonEntity, h)) {
			parent.getModel().render(enderDragonEntity, f, g, i, j, k, l);
			DamageTint.unsetDamageTint();
		}
    }
}
