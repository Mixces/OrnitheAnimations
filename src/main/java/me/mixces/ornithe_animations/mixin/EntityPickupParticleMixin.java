package me.mixces.ornithe_animations.mixin;

import com.mojang.blaze3d.vertex.BufferBuilder;
import me.mixces.ornithe_animations.util.GlHelper;
import net.minecraft.client.entity.particle.EntityPickupParticle;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPickupParticle.class)
public abstract class EntityPickupParticleMixin {

	@Inject(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;render(Lnet/minecraft/entity/Entity;DDDFF)Z"
		)
	)
	private void ornitheAnimations$pickupPosition(BufferBuilder bufferBuilder, Entity camera, float tickDelta, float dx, float dy, float dz, float forwards, float sideways, CallbackInfo ci) {
		GlHelper.getBuilder().translate(0.0F, 0.5F, 0.0F);
	}
}
