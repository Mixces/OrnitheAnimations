package me.mixces.ornitheanimations.mixin;

import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.mixces.ornitheanimations.config.Config;
import me.mixces.ornitheanimations.hook.ItemBlacklist;
import me.mixces.ornitheanimations.util.GlHelper;
import net.minecraft.client.entity.living.player.ClientPlayerEntity;
import net.minecraft.client.render.HeldItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.block.ModelTransformations;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

	@Shadow
	private int selectedSlot;

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
		return Config.INSTANCE.getBLOCK_HITTING().get() ? ornitheAnimations$g.get() : swingProgress;
	}

	@Inject(
		method = "applyBowNocking",
		at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/blaze3d/platform/GlStateManager;scalef(FFF)V"
		)
	)
	private void orintheAnimations$preBowTransform(float tickDelta, ClientPlayerEntity player, CallbackInfo ci) {
		if (Config.INSTANCE.getOLD_ITEM_POSITIONS().get()) {
			/* original transformations from 1.7 */
			GlHelper.roll(-335.0F);
			GlHelper.yaw(-50.0F);
		}
	}

	@Inject(
		method = "applyBowNocking",
		at = @At("TAIL")
	)
	private void orintheAnimations$postBowTransform(float tickDelta, ClientPlayerEntity player, CallbackInfo ci) {
		if (Config.INSTANCE.getOLD_ITEM_POSITIONS().get()) {
			/* original transformations from 1.7 */
			GlHelper.yaw(50.0F);
			GlHelper.roll(335.0F);
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
		if (!Config.INSTANCE.getOLD_ITEM_POSITIONS().get()) {
			return;
		}
		if (renderer.isGui3d(item) || ItemBlacklist.isPresent(item)) {
			return;
		}
		/* original transformations from 1.7 */
		GlHelper.translate(0.0F, -0.3F, 0.0F);
		GlHelper.scale(1.5F, 1.5F, 1.5F);
		GlHelper.yaw(50.0F);
		GlHelper.roll(335.0F);
		GlHelper.translate(-0.9375F, -0.0625F, 0.0F);
		/* idk */
		GlHelper.yaw(180.0F);
		GlHelper.translate(-0.5F, 0.5F, 0.03125F);
	}

	@Inject(
		method = "renderInFirstPerson",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/HeldItemRenderer;render(Lnet/minecraft/entity/living/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/block/ModelTransformations$Type;)V"
		)
	)
	private void ornitheAnimations$applyRodRotation(float tickDelta, CallbackInfo ci) {
		if (Config.INSTANCE.getOLD_ITEM_POSITIONS().get() && item.getItem().shouldRotate()) {
			GlHelper.yaw(180.0F);
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
		return Config.INSTANCE.getOLD_ITEM_POSITIONS().get() && !ItemBlacklist.isPresent(item) ? ModelTransformations.Type.NONE : transform;
	}

	@Expression("? != null")
	@ModifyExpressionValue(
		method = "updateHeldItem",
		at = @At(
			value = "MIXINEXTRAS:EXPRESSION",
			ordinal = 1
		)
	)
	private boolean ornitheAnimations$compareDamage(boolean original, @Local ItemStack itemStack) {
		if (Config.INSTANCE.getOLD_EQUIP_LOGIC().get()) {
			/* adapted from 1.7 */
			return original && itemStack != item && itemStack.getItem() == item.getItem() && itemStack.getDamage() == item.getDamage();
		}
		return original;
	}

	@ModifyExpressionValue(
		method = "updateHeldItem",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/item/ItemStack;isEqualForHoldAnimation(Lnet/minecraft/item/ItemStack;)Z"
		)
	)
	private boolean ornitheAnimations$disableStackEquality(boolean original, @Local ItemStack itemStack) {
		/* adapted from 1.7 */
		return !Config.INSTANCE.getOLD_EQUIP_LOGIC().get() && original;
	}

	@ModifyVariable(
		method = "updateHeldItem",
		at = @At(
			value = "STORE",
			ordinal = 1
		),
		index = 3
	)
	private boolean ornitheAnimations$updateItemStack(boolean original, @Local ItemStack itemStack) {
		if (Config.INSTANCE.getOLD_EQUIP_LOGIC().get()) {
			/* adapted from 1.7 */
			item = itemStack;
			return false;
		}
		return original;
	}

	@ModifyVariable(
		method = "updateHeldItem",
		at = @At(
			value = "STORE",
			ordinal = 3
		),
		index = 3
	)
	private boolean ornitheAnimations$makeAssignmentRedundant(boolean original, @Local PlayerEntity playerEntity, @Local ItemStack itemStack) {
		if (Config.INSTANCE.getOLD_EQUIP_LOGIC().get()) {
			/* adapted from 1.7 */
			return selectedSlot != playerEntity.inventory.selectedSlot || itemStack != item;
		}
		return original;
	}
}
