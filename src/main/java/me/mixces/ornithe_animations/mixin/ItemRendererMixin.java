package me.mixces.ornithe_animations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.mixces.ornithe_animations.handler.GlintHandler;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.resource.model.BakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @ModifyArg(
		method = "renderItem",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/item/ItemRenderer;renderEnchantmentGlint(Lnet/minecraft/client/resource/model/BakedModel;)V"
		)
    )
    public BakedModel ornitheAnimations$replaceModel(BakedModel model) {
		return GlintHandler.getGlint(model);
    }

	@ModifyArg(
		method = "render(Lnet/minecraft/client/resource/model/BakedModel;I)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/item/ItemRenderer;render(Lnet/minecraft/client/resource/model/BakedModel;ILnet/minecraft/item/ItemStack;)V"
		),
		index = 1
	)
	public int ornitheAnimations$replaceColor(int color) {
		return -10407781;
	}

	@ModifyExpressionValue(
		method = "renderEnchantmentGlint",
		at = @At(
			value = "CONSTANT",
			args = "floatValue=8.0F")
	)
	public float ornitheAnimations$modifyScale(float original) {
		return 1.0F / original;
	}
}
