package me.mixces.ornitheanimations.mixin;

import me.mixces.ornitheanimations.OrnitheAnimations;
import net.minecraft.client.render.model.ModelPart;
import net.minecraft.client.render.model.entity.HumanoidModel;
import net.minecraft.entity.Entity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin {

    @Shadow
	public ModelPart rightArm;

    @Inject(
		method = "setAngles",
		at = @At(
			value = "FIELD",
			opcode = Opcodes.PUTFIELD,
			target = "Lnet/minecraft/client/render/model/ModelPart;rotationY:F",
			shift = At.Shift.AFTER
		),
		slice = @Slice(
			from = @At(
				value = "FIELD",
				opcode = Opcodes.GETFIELD,
				target = "Lnet/minecraft/client/render/model/entity/HumanoidModel;rightHandItemId:I",
				ordinal = 0
			),
			to = @At(
				value = "FIELD",
				opcode = Opcodes.GETFIELD,
				target = "Lnet/minecraft/client/render/model/entity/HumanoidModel;rightHandItemId:I",
				ordinal = 2
			)
		)
    )
    private void ornitheAnimations$reAssignArmPosition(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, CallbackInfo ci) {
		if (OrnitheAnimations.config.getOLD_ITEM_POSITIONS().get()) {
			rightArm.rotationY = 0.0f;
		}
    }
}
