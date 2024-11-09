package me.mixces.ornitheanimations.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.mixces.ornitheanimations.OrnitheAnimations;
import me.mixces.ornitheanimations.hook.ItemBlacklist;
import me.mixces.ornitheanimations.util.GlHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.entity.layer.HeldItemLayer;
import net.minecraft.client.render.model.block.ModelTransformations;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemLayer.class)
public abstract class HeldItemLayerMixin {

	@ModifyArg(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/item/ItemStack;<init>(Lnet/minecraft/item/Item;I)V"
		),
		index = 0
	)
	private Item ornitheAnimations$changeToStick(Item item) {
		return OrnitheAnimations.config.getREPLACE_CAST_ROD().get() ? Items.STICK : item;
	}

	@Definition(
		id = "getRenderType",
		method = "Lnet/minecraft/block/Block;getRenderType()I"
	)
	@Expression("?.getRenderType() == 2")
	@ModifyExpressionValue(
		method = "render",
		at = @At("MIXINEXTRAS:EXPRESSION")
	)
	private boolean ornitheAnimations$allowBlocksTransforms(boolean original) {
		return true;
	}

    @Inject(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/HeldItemRenderer;render(Lnet/minecraft/entity/living/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/block/ModelTransformations$Type;)V"
		)
    )
    private void ornitheAnimations$applyHeldItemLayerTransforms(LivingEntity entity, float handSwingAmount, float handSwing, float tickDelta, float age, float headYaw, float headPitch, float scale, CallbackInfo ci, @Local(ordinal = 0, index = 9) ItemStack stack, @Local(ordinal = 0, index = 10) Item item) {
		if (!OrnitheAnimations.config.getOLD_ITEM_POSITIONS().get()) {
			return;
		}
		if (Minecraft.getInstance().getItemRenderer().isGui3d(stack) || ItemBlacklist.isPresent(stack)) {
			return;
		}
		GlHelper builder = GlHelper.INSTANCE;
		float var7;
		/* original transformations from 1.7 */
		if (item == Items.BOW) {
			var7 = 0.625F;
			builder.translate(0.0F, 0.125F, 0.3125F).yaw(-20.0F).scale(var7, -var7, var7).pitch(-100.0F).yaw(45.0F);
		} else if (item.isHandheld()) {
			var7 = 0.625F;
			if (item.shouldRotate()) {
				builder.roll(180.0F).translate(0.0F, -0.125F, 0.0F);
			}
			if (entity instanceof PlayerEntity && ((PlayerEntity) entity).getItemUseTimer() > 0 && ((PlayerEntity) entity).isSwordBlocking()) {
				builder.translate(0.05F, 0.0F, -0.1F).yaw(-50.0F).pitch(-10.0F).roll(-60.0F);
			}
			builder.translate(0.0F, 0.1875F, 0.0F).scale(var7, -var7, var7).pitch(-100.0F).yaw(45.0F);
		} else {
			var7 = 0.375F;
			builder.translate(0.25F, 0.1875F, -0.1875F).scale(var7, var7, var7).roll(60.0F).pitch(-90.0F).roll(20.0F);
		}
    }

	@ModifyArg(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/HeldItemRenderer;render(Lnet/minecraft/entity/living/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/block/ModelTransformations$Type;)V"
		),
		index = 2
	)
	private ModelTransformations.Type ornitheAnimations$changeTransformType(ModelTransformations.Type transform, @Local ItemStack itemStack) {
		return OrnitheAnimations.config.getOLD_ITEM_POSITIONS().get() && !ItemBlacklist.isPresent(itemStack) ? ModelTransformations.Type.NONE : transform;
	}
}
