package me.mixces.ornithe_animations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.mixces.ornithe_animations.handler.GlintHandler;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.resource.model.BakedModel;
import net.minecraft.client.resource.model.BakedQuad;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.Identifier;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

	@Shadow
	public float zOffset;

	@Shadow
	@Final
	private static Identifier ENCHANTMENT_GLINT_LOCATION;

	@Unique
	private boolean ornitheAnimations$isGui;

	@ModifyExpressionValue(
		method = "render(Lnet/minecraft/client/resource/model/BakedModel;ILnet/minecraft/item/ItemStack;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/resource/model/BakedModel;getQuads()Ljava/util/List;"
		)
	)
	private List<BakedQuad> overflowAnimations$changeToSprite(List<BakedQuad> quads, @Local(argsOnly = true) BakedModel model) {
		if (ornitheAnimations$isGui && !model.isGui3d()) {
			return quads.stream().filter(baked -> baked.getFace() == Direction.SOUTH).collect(Collectors.toList());
		}
		return quads;
	}

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

	@Inject(
		method = "renderEnchantmentGlint",
		at = @At("HEAD"),
		cancellable = true
	)
	public void abc(CallbackInfo ci) {
		if (ornitheAnimations$isGui) ci.cancel();
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

	@Inject(
		method = "renderGuiItemModel",
		at = @At("HEAD")
	)
	public void ornitheAnimations$captureGuiMode(ItemStack stack, int x, int y, CallbackInfo ci) {
		ornitheAnimations$isGui = true;
	}

	@Inject(
		method = "renderGuiItemModel",
		at = @At("TAIL")
	)
	public void ornitheAnimations$(ItemStack stack, int x, int y, CallbackInfo ci) {
		ornitheAnimations$isGui = false;
		if (stack.hasEnchantmentGlint()) {
			GlintHandler.renderGlintGui(x, y, zOffset, ENCHANTMENT_GLINT_LOCATION);
		}
	}
}
