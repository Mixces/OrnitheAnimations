package me.mixces.ornithe_animations.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.mixces.ornithe_animations.handler.SpriteHandler;
import net.minecraft.client.render.entity.ProjectileRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.block.ModelTransformations;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ProjectileRenderer.class)
public abstract class ProjectileRendererMixin {

    @ModifyArg(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/blaze3d/platform/GlStateManager;rotatef(FFFF)V",
			ordinal = 0
		),
		index = 0
    )
    private float ornitheAnimations$rotateProjectile(float angle) {
        return angle + 180.0F;
    }

    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/GlStateManager;rotatef(FFFF)V",
                    ordinal = 1
            ),
            index = 0
    )
    private float ornitheAnimations$useProperCameraView(float angle) {
        return -angle;
    }

	@WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/ItemRenderer;renderHeldItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/block/ModelTransformations$Type;)V"
            )
    )
    private void ornitheAnimations$renderSprite(ItemRenderer instance, ItemStack stack, ModelTransformations.Type transformationType, Operation<Void> original) {
		SpriteHandler.renderSpriteWithLayer(instance.getModelShaper().getModel(stack), stack, stack.getItem());
    }
}
