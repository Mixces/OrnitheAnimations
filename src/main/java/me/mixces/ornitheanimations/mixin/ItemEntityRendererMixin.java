package me.mixces.ornitheanimations.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.model.block.ModelTransformations;
import net.minecraft.entity.ItemEntity;
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

	@ModifyArg(
		method = "render(Lnet/minecraft/entity/ItemEntity;DDDFF)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/model/block/ModelTransformations;apply(Lnet/minecraft/client/render/model/block/ModelTransformations$Type;)V",
			ordinal = 1
		)
	)
	private ModelTransformations.Type ornitheAnimations$replaceTransform(ModelTransformations.Type type) {
		return ModelTransformations.Type.GUI;
	}
}
