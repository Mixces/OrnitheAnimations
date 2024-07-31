package me.mixces.ornithe_animations.mixin;

import me.mixces.ornithe_animations.util.GlHelper;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {

	@Inject(
		method = "render(Lnet/minecraft/entity/living/LivingEntity;DDDFF)V",
		at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/blaze3d/platform/GlStateManager;translatef(FFF)V"
		)
    )
    private void ornitheAnimations$addSneakingTranslation(LivingEntity entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (entity instanceof PlayerEntity && entity.isSneaking()) {
			GlHelper.getBuilder().translate(0.0F, -0.2F, 0.0F);
        }
    }
}
