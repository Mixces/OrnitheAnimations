package me.mixces.ornitheanimations.mixin;

import net.minecraft.client.entity.living.player.ClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.PlayerRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

	@Shadow
	private PlayerRenderer defaultPlayerRenderer;

	@Inject(
		method = "getRenderer(Lnet/minecraft/entity/Entity;)Lnet/minecraft/client/render/entity/EntityRenderer;",
		at = @At("HEAD"),
		cancellable = true
	)
	private void ornitheAnimations$defaultToSteve(Entity entity, CallbackInfoReturnable<PlayerRenderer> cir) {
		if (entity instanceof ClientPlayerEntity) {
			/* 1.7 doesn't have Alex skins! */
			/* thank you toggle */
			cir.setReturnValue(defaultPlayerRenderer);
		}
	}
}
