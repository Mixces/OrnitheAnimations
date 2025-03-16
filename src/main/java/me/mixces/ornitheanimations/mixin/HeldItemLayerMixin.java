package me.mixces.ornitheanimations.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.GlStateManager;
import me.mixces.ornitheanimations.config.Config;
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
		return Config.INSTANCE.getREPLACE_CAST_ROD().get() ? Items.STICK : item;
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
		return Config.INSTANCE.getOLD_ITEM_POSITIONS().get() || original;
	}

	@WrapOperation(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/entity/living/LivingEntity;isSneaking()Z"
		)
	)
	private boolean ornitheAnimations$disableSneakTranslation(LivingEntity instance, Operation<Boolean> original) {
		return !Config.INSTANCE.getOLD_ITEM_POSITIONS().get() &&
			!Config.INSTANCE.getSMOOTH_SNEAKING().get() && original.call(instance);
	}

	@Inject(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/model/entity/HumanoidModel;translateRightArm(F)V"
		)
	)
	private void legarity$mc125204fix(LivingEntity entity, float handSwingAmount, float handSwing, float tickDelta, float age, float headYaw, float headPitch, float scale, CallbackInfo ci) {
		if (Config.INSTANCE.getOLD_ITEM_POSITIONS().get() &&
			!Config.INSTANCE.getSMOOTH_SNEAKING().get() && entity.isSneaking()) {
			GlHelper.translate(0.0F, 0.2F, 0.0F);
		}
	}

    @Inject(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/HeldItemRenderer;render(Lnet/minecraft/entity/living/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/block/ModelTransformations$Type;)V"
		)
    )
    private void ornitheAnimations$applyHeldItemLayerTransforms(LivingEntity entity, float handSwingAmount, float handSwing, float tickDelta, float age, float headYaw, float headPitch, float scale, CallbackInfo ci, @Local(ordinal = 0, index = 9) ItemStack stack, @Local(ordinal = 0, index = 10) Item item) {
		if (!Config.INSTANCE.getOLD_ITEM_POSITIONS().get()) {
			return;
		}
		if (Minecraft.getInstance().getItemRenderer().isGui3d(stack) || ItemBlacklist.isPresent(stack)) {
			return;
		}
		/* original transformations from 1.7 */
		float var7;
		if (item == Items.BOW) {
			GlStateManager.cullFace(1028);
			var7 = 0.625F;
			GlHelper.translate(0.0F, 0.125F, 0.3125F);
			GlHelper.yaw(-20.0F);
			GlHelper.scale(var7, -var7, var7);
			GlHelper.pitch(-100.0F);
			GlHelper.yaw(45.0F);
		} else if (item.isHandheld()) {
			GlStateManager.cullFace(1028);
			var7 = 0.625F;
			if (item.shouldRotate()) {
				GlHelper.roll(180.0F);
				GlHelper.translate(0.0F, -0.125F, 0.0F);
			}
			if (entity instanceof PlayerEntity && ((PlayerEntity) entity).getItemUseTimer() > 0 && ((PlayerEntity) entity).isSwordBlocking()) {
				GlHelper.translate(0.05F, 0.0F, -0.1F);
				GlHelper.yaw(-50.0F);
				GlHelper.pitch(-10.0F);
				GlHelper.roll(-60.0F);
			}
			GlHelper.translate(0.0F, 0.1875F, 0.0F);
			GlHelper.scale(var7, -var7, var7);
			GlHelper.pitch(-100.0F);
			GlHelper.yaw(45.0F);
		} else {
			GlStateManager.cullFace(1029);
			var7 = 0.375F;
			GlHelper.translate(0.25F, 0.1875F, -0.1875F);
			GlHelper.scale(var7, var7, var7);
			GlHelper.roll(60.0F);
			GlHelper.pitch(-90.0F);
			GlHelper.roll(20.0F);
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
		return Config.INSTANCE.getOLD_ITEM_POSITIONS().get() && !ItemBlacklist.isPresent(itemStack) ? ModelTransformations.Type.NONE : transform;
	}
}
