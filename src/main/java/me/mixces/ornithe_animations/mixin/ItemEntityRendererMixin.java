package me.mixces.ornithe_animations.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.mixces.ornithe_animations.handler.SpriteHandler;
import me.mixces.ornithe_animations.util.GlHelper;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.resource.model.BakedModel;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemEntityRenderer.class)
public abstract class ItemEntityRendererMixin extends EntityRenderer<ItemEntity> {

	protected ItemEntityRendererMixin(EntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@ModifyArg(
		method = "applyItemBobbing",
		at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/blaze3d/platform/GlStateManager;rotatef(FFFF)V"
		),
		index = 0
	)
	private float ornitheAnimations$itemFacePlayer(float angle, @Local boolean bl) {
		if (!bl) {
			return 180.0F - dispatcher.cameraYaw;
		}
		return angle;
	}

	@WrapOperation(
		method = "render(Lnet/minecraft/entity/ItemEntity;DDDFF)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resource/model/BakedModel;)V",
			ordinal = 1
		)
	)
	private void ornitheAnimations$renderSprite(ItemRenderer instance, ItemStack stack, BakedModel model, Operation<Void> original) {
		GlHelper.getBuilder().scale(0.5f, 0.5f, 0.5f);
		SpriteHandler.renderSpriteWithLayer(instance.getModelShaper().getModel(stack), stack, stack.getItem());
	}
}
