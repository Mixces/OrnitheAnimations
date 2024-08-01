package me.mixces.ornithe_animations.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.platform.GlStateManager;
import me.mixces.ornithe_animations.util.GlHelper;
import net.minecraft.client.entity.particle.EntityPickupParticle;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityPickupParticle.class)
public abstract class EntityPickupParticleMixin {

	@WrapOperation(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;render(Lnet/minecraft/entity/Entity;DDDFF)Z"
		)
	)
	private boolean ornitheAnimations$pickupPosition(EntityRenderDispatcher instance, Entity entity, double dx, double dy, double dz, float yaw, float tickDelta, Operation<Boolean> original) {
		GlStateManager.pushMatrix();
		GlHelper.getBuilder().translate(0.0F, 0.5F, 0.0F);
		original.call(instance, entity, dx, dy, dz, yaw, tickDelta);
		GlStateManager.popMatrix();
		return true;
	}
}
