package me.mixces.ornithe_animations.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.mixces.ornithe_animations.handler.DamageLayerHandler;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.entity.layer.*;
import net.minecraft.client.render.model.Model;
import net.minecraft.client.render.model.entity.PigModel;
import net.minecraft.client.render.model.entity.SheepFurModel;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.mob.SlimeEntity;
import net.minecraft.entity.living.mob.hostile.EndermanEntity;
import net.minecraft.entity.living.mob.hostile.SpiderEntity;
import net.minecraft.entity.living.mob.hostile.boss.EnderDragonEntity;
import net.minecraft.entity.living.mob.passive.animal.PigEntity;
import net.minecraft.entity.living.mob.passive.animal.SheepEntity;
import net.minecraft.entity.living.mob.passive.animal.tamable.WolfEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public abstract class DamageLayerMixin {

	@Mixin(LivingEntityRenderer.class)
	public abstract static class LivingEntityRendererMixin {
		@WrapOperation(
			method = "render(Lnet/minecraft/entity/living/LivingEntity;DDDFF)V",
			at = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;renderHand(Lnet/minecraft/entity/living/LivingEntity;FFFFFF)V",
				ordinal = 1
			)
		)
		private void ornitheAnimations$setupGlColor(LivingEntityRenderer<LivingEntity> instance, LivingEntity entity, float handSwing, float handSwingAmount, float age, float yaw, float pitch, float scale, Operation<Void> original, @Local(ordinal = 0, index = 5, argsOnly = true) float h) {
			original.call(instance, entity, handSwing, handSwingAmount, age, yaw, pitch, scale);
			if (entity.hurtTime > 0 || entity.deathTime > 0) {
				DamageLayerHandler.setupOverlayColor(entity, h);
				original.call(instance, entity, handSwing, handSwingAmount, age, yaw, pitch, scale);
				DamageLayerHandler.tearDownOverlayColor();
			}
		}

		@WrapMethod(method = "setupOverlayColor(Lnet/minecraft/entity/living/LivingEntity;FZ)Z")
		private boolean ornitheAnimations$disableTintBuffer(LivingEntity entity, float tickDelta, boolean bl, Operation<Boolean> original) {
			return false;
		}
	}

	@Mixin(AbstractArmorLayer.class)
	public abstract static class AbstractArmorLayerMixin {
		@Inject(
			method = "renderArmor",
			at = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/item/ItemStack;hasEnchantments()Z"
			)
		)
		private void ornitheAnimations$setupGlColor(LivingEntity entity, float handSwingAmount, float handSwing, float tickDelta, float age, float headYaw, float headPitch, float scale, int equipmentSlot, CallbackInfo ci, @Local Model model) {
			if (entity.hurtTime > 0 || entity.deathTime > 0) {
				DamageLayerHandler.setupOverlayColor(entity, tickDelta);
				model.render(entity, handSwingAmount, handSwing, age, headYaw, headPitch, scale);
				DamageLayerHandler.tearDownOverlayColor();
			}
		}
	}

	@Mixin(EnderDragonEyesLayer.class)
	public abstract static class EnderDragonEyesLayerMixin {
		@Shadow
		@Final
		private EnderDragonRenderer parent;

		@Inject(
			method = "render(Lnet/minecraft/entity/living/mob/hostile/boss/EnderDragonEntity;FFFFFFF)V",
			at = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/client/render/model/Model;render(Lnet/minecraft/entity/Entity;FFFFFF)V",
				shift = At.Shift.AFTER
			)
		)
		private void ornitheAnimations$setupGlColor(EnderDragonEntity enderDragonEntity, float f, float g, float h, float i, float j, float k, float l, CallbackInfo ci) {
			if (enderDragonEntity.hurtTime > 0 || enderDragonEntity.deathTime > 0) {
				DamageLayerHandler.setupOverlayColor(enderDragonEntity, h);
				parent.getModel().render(enderDragonEntity, f, g, i, j, k, l);
				DamageLayerHandler.tearDownOverlayColor();
			}
		}
	}

	@Mixin(EndermanEyesLayer.class)
	public abstract static class EndermanEyesLayerMixin {
		@Shadow
		@Final
		private EndermanRenderer parent;

		@Inject(
			method = "render(Lnet/minecraft/entity/living/mob/hostile/EndermanEntity;FFFFFFF)V",
			at = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/client/render/model/Model;render(Lnet/minecraft/entity/Entity;FFFFFF)V",
				shift = At.Shift.AFTER
			)
		)
		private void ornitheAnimations$setupGlColor(EndermanEntity endermanEntity, float f, float g, float h, float i, float j, float k, float l, CallbackInfo ci) {
			if (endermanEntity.hurtTime > 0 || endermanEntity.deathTime > 0) {
				DamageLayerHandler.setupOverlayColor(endermanEntity, h);
				parent.getModel().render(endermanEntity, f, g, i, j, k, l);
				DamageLayerHandler.tearDownOverlayColor();
			}
		}
	}

	@Mixin(PigSaddleLayer.class)
	public abstract static class PigSaddleLayerMixin {
		@Shadow
		@Final
		private PigModel model;

		@Inject(
			method = "render(Lnet/minecraft/entity/living/mob/passive/animal/PigEntity;FFFFFFF)V",
			at = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/client/render/model/entity/PigModel;render(Lnet/minecraft/entity/Entity;FFFFFF)V",
				shift = At.Shift.AFTER
			)
		)
		private void ornitheAnimations$setupGlColor(PigEntity pigEntity, float f, float g, float h, float i, float j, float k, float l, CallbackInfo ci) {
			if (pigEntity.hurtTime > 0 || pigEntity.deathTime > 0) {
				DamageLayerHandler.setupOverlayColor(pigEntity, h);
				model.render(pigEntity, f, g, i, j, k, l);
				DamageLayerHandler.tearDownOverlayColor();
			}
		}
	}

	@Mixin(SheepFurLayer.class)
	public abstract static class SheepFurLayerMixin {
		@Shadow
		@Final
		private SheepFurModel model;

		@Inject(
			method = "render(Lnet/minecraft/entity/living/mob/passive/animal/SheepEntity;FFFFFFF)V",
			at = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/client/render/model/entity/SheepFurModel;render(Lnet/minecraft/entity/Entity;FFFFFF)V",
				shift = At.Shift.AFTER
			)
		)
		private void ornitheAnimations$setupGlColor(SheepEntity sheepEntity, float f, float g, float h, float i, float j, float k, float l, CallbackInfo ci) {
			if (sheepEntity.hurtTime > 0 || sheepEntity.deathTime > 0) {
				DamageLayerHandler.setupOverlayColor(sheepEntity, h);
				model.render(sheepEntity, f, g, i, j, k, l);
				DamageLayerHandler.tearDownOverlayColor();
			}
		}
	}

	@Mixin(SlimeOuterLayer.class)
	public abstract static class SlimeOuterLayerMixin {
		@Shadow
		@Final
		private Model model;

		@Inject(
			method = "render(Lnet/minecraft/entity/living/mob/SlimeEntity;FFFFFFF)V",
			at = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/client/render/model/Model;render(Lnet/minecraft/entity/Entity;FFFFFF)V",
				shift = At.Shift.AFTER
			)
		)
		private void ornitheAnimations$setupGlColor(SlimeEntity slimeEntity, float f, float g, float h, float i, float j, float k, float l, CallbackInfo ci) {
			if (slimeEntity.hurtTime > 0 ||  slimeEntity.deathTime > 0) {
				DamageLayerHandler.setupOverlayColor(slimeEntity, h);
				model.render(slimeEntity, f, g, i, j, k, l);
				DamageLayerHandler.tearDownOverlayColor();
			}
		}
	}

	@Mixin(SpiderEyesLayer.class)
	public abstract static class SpiderEyesLayerMixin {
		@Shadow
		@Final
		private SpiderRenderer<SpiderEntity> parent;

		@Inject(
			method = "render(Lnet/minecraft/entity/living/mob/hostile/SpiderEntity;FFFFFFF)V",
			at = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/client/render/model/Model;render(Lnet/minecraft/entity/Entity;FFFFFF)V",
				shift = At.Shift.AFTER
			)
		)
		private void ornitheAnimations$setupGlColor(SpiderEntity spiderEntity, float f, float g, float h, float i, float j, float k, float l, CallbackInfo ci) {
			if (spiderEntity.hurtTime > 0 ||  spiderEntity.deathTime > 0) {
				DamageLayerHandler.setupOverlayColor(spiderEntity, h);
				parent.getModel().render(spiderEntity, f, g, i, j, k, l);
				DamageLayerHandler.tearDownOverlayColor();
			}
		}
	}

	@Mixin(WolfCollarLayer.class)
	public abstract static class WolfCollarLayerMixin {
		@Shadow
		@Final
		private WolfRenderer parent;

		@Inject(
			method = "render(Lnet/minecraft/entity/living/mob/passive/animal/tamable/WolfEntity;FFFFFFF)V",
			at = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/client/render/model/Model;render(Lnet/minecraft/entity/Entity;FFFFFF)V",
				shift = At.Shift.AFTER
			)
		)
		private void ornitheAnimations$setupGlColor(WolfEntity wolfEntity, float f, float g, float h, float i, float j, float k, float l, CallbackInfo ci) {
			if (wolfEntity.hurtTime > 0 ||  wolfEntity.deathTime > 0) {
				DamageLayerHandler.setupOverlayColor(wolfEntity, h);
				parent.getModel().render(wolfEntity, f, g, i, j, k, l);
				DamageLayerHandler.tearDownOverlayColor();
			}
		}
	}
}
