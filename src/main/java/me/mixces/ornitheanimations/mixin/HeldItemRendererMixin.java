package me.mixces.ornitheanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.mixces.ornitheanimations.OrnitheAnimations;
import me.mixces.ornitheanimations.hook.ItemBlacklist;
import me.mixces.ornitheanimations.util.GlHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.living.player.ClientPlayerEntity;
import net.minecraft.client.render.HeldItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.block.ModelTransformations;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

	@Shadow
	private int selectedSlot;

	@Shadow
	@Final
	private Minecraft minecraft;

	@Shadow
	@Final
	private ItemRenderer renderer;

	@Shadow
	private ItemStack item;

	@Unique
	private static final ThreadLocal<Float> ornitheAnimations$g = ThreadLocal.withInitial(() -> 0.0F);

	@ModifyVariable(
		method = "renderInFirstPerson",
		at = @At("STORE"),
		index = 4
	)
	private float ornitheAnimations$captureF1(float g) {
		ornitheAnimations$g.set(g);
		return g;
	}

	@ModifyArg(
		method = "renderInFirstPerson",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/HeldItemRenderer;applyFirstPersonTransform(FF)V"
		),
		slice = @Slice(
			from = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/client/render/HeldItemRenderer;applyConsuming(Lnet/minecraft/client/entity/living/player/ClientPlayerEntity;F)V"
			),
			to = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/client/render/HeldItemRenderer;applyBowNocking(FLnet/minecraft/client/entity/living/player/ClientPlayerEntity;)V"
			)
		),
		index = 1
	)
	private float ornitheAnimations$useCapture(float swingProgress) {
		return OrnitheAnimations.config.getBLOCK_HITTING().get() ? ornitheAnimations$g.get() : swingProgress;
	}

	@Inject(
		method = "applyBowNocking",
		at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/blaze3d/platform/GlStateManager;scalef(FFF)V"
		)
	)
	private void orintheAnimations$preBowTransform(float tickDelta, ClientPlayerEntity player, CallbackInfo ci) {
		if (OrnitheAnimations.config.getOLD_ITEM_POSITIONS().get()) {
			/* original transformations from 1.7 */
			GlHelper.INSTANCE.roll(-335.0F).yaw(-50.0F);
		}
	}

	@Inject(
		method = "applyBowNocking",
		at = @At("TAIL")
	)
	private void orintheAnimations$postBowTransform(float tickDelta, ClientPlayerEntity player, CallbackInfo ci) {
		if (OrnitheAnimations.config.getOLD_ITEM_POSITIONS().get()) {
			/* original transformations from 1.7 */
			GlHelper.INSTANCE.yaw(50.0F).roll(335.0F);
		}
	}

	@Inject(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/item/ItemRenderer;renderHeldItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/living/LivingEntity;Lnet/minecraft/client/render/model/block/ModelTransformations$Type;)V"
		)
	)
	private void ornitheAnimations$applyHeldItemTransforms(LivingEntity entity, ItemStack item, ModelTransformations.Type transform, CallbackInfo ci) {
		if (!OrnitheAnimations.config.getOLD_ITEM_POSITIONS().get()) {
			return;
		}
		if (renderer.isGui3d(item) || ItemBlacklist.isPresent(item)) {
			return;
		}
		GlHelper builder = GlHelper.INSTANCE;
		/* original transformations from 1.7 */
		builder.translate(0.0F, -0.3F, 0.0F).scale(1.5F, 1.5F, 1.5F).yaw(50.0F).roll(335.0F).translate(-0.9375F, -0.0625F, 0.0F);
		/* idk */
		builder.yaw(180.0F).translate(-0.5F, 0.5F, 0.03F);
	}

	@Inject(
		method = "renderInFirstPerson",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/HeldItemRenderer;render(Lnet/minecraft/entity/living/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/block/ModelTransformations$Type;)V"
		)
	)
	private void ornitheAnimations$applyRodRotation(float tickDelta, CallbackInfo ci) {
		if (OrnitheAnimations.config.getOLD_ITEM_POSITIONS().get() && item.getItem().shouldRotate()) {
			GlHelper.INSTANCE.yaw(180.0F);
		}
	}

	@ModifyArg(
		method = "renderInFirstPerson",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/HeldItemRenderer;render(Lnet/minecraft/entity/living/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/block/ModelTransformations$Type;)V"
		),
		index = 2
	)
	private ModelTransformations.Type ornitheAnimations$changeTransformType(ModelTransformations.Type transform) {
		return OrnitheAnimations.config.getOLD_ITEM_POSITIONS().get() && !ItemBlacklist.isPresent(item) ? ModelTransformations.Type.NONE : transform;
	}

	@ModifyExpressionValue(
		method = "updateHeldItem",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/item/ItemStack;isEqualForHoldAnimation(Lnet/minecraft/item/ItemStack;)Z"
		)
	)
	private boolean ornitheAnimations$modifyReEquipBehavior(boolean original) {
		return original && (!OrnitheAnimations.config.getFULL_REEQUIP_LOGIC().get() || selectedSlot == minecraft.player.inventory.selectedSlot);
	}
}
