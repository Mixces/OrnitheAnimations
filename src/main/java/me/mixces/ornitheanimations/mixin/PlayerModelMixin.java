package me.mixces.ornitheanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import me.mixces.ornitheanimations.config.Config;
import net.minecraft.client.render.model.ModelPart;
import net.minecraft.client.render.model.entity.PlayerModel;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public abstract class PlayerModelMixin {

	@Shadow
	private ModelPart cape;

	@WrapOperation(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/entity/Entity;isSneaking()Z"
		)
	)
	private boolean ornitheAnimations$disableSneakTranslation(Entity instance, Operation<Boolean> original) {
		return !Config.INSTANCE.getSMOOTH_SNEAKING().get() && original.call(instance);
	}

	@Inject(
		method = "setAngles",
		at = @At("HEAD")
	)
	private void ornitheAnimations$copyCapePivot(float handSwing, float handSwingAmount, float age, float yaw, float pitch, float scale, Entity entity, CallbackInfo ci, @Share("pivotY") LocalFloatRef pivotY) {
		if (Config.INSTANCE.getSMOOTH_SNEAKING().get()) {
			pivotY.set(cape.pivotY);
		}
	}

	@Inject(
		method = "setAngles",
		at = @At("TAIL")
	)
	private void ornitheAnimations$disableSneakCapeTranslations(float handSwing, float handSwingAmount, float age, float yaw, float pitch, float scale, Entity entity, CallbackInfo ci, @Share("pivotY") LocalFloatRef pivotY) {
		if (Config.INSTANCE.getSMOOTH_SNEAKING().get()) {
			cape.pivotY = pivotY.get();
		}
	}

	@ModifyExpressionValue(
		method = "translateRightArm",
		at = @At(
			value = "CONSTANT",
			args = "floatValue=1.0F"
		)
	)
	private float ornitheAnimations$alexArmFix(float original) {
		return Config.INSTANCE.getOLD_ITEM_POSITIONS().get() ? original / 2.0F : original;
	}
}
