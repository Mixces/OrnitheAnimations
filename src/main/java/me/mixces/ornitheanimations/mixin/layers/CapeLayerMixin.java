package me.mixces.ornitheanimations.mixin.layers;

import me.mixces.ornitheanimations.config.Config;
import me.mixces.ornitheanimations.util.GlHelper;
import net.minecraft.client.entity.living.player.ClientPlayerEntity;
import net.minecraft.client.render.entity.layer.CapeLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CapeLayer.class)
public abstract class CapeLayerMixin {

	@Inject(method = "render(Lnet/minecraft/client/entity/living/player/ClientPlayerEntity;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;translatef(FFF)V"))
	private void ornitheAnimations$addSneakingTranslation(ClientPlayerEntity clientPlayerEntity, float f, float g, float h, float i, float j, float k, float l, CallbackInfo ci) {
		if (!Config.INSTANCE.getSMOOTH_SNEAKING().get()) {
			return;
		}
		if (clientPlayerEntity.isSneaking()) {
			GlHelper.translate(0.0F, -0.125F, 0.0F);
		}
	}
}
