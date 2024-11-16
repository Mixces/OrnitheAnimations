package me.mixces.ornitheanimations.mixin;

import me.mixces.ornitheanimations.OrnitheAnimations;
import net.minecraft.client.entity.living.player.ClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerRenderer;
import net.minecraft.client.render.model.ModelPart;
import net.minecraft.client.render.model.entity.PlayerModel;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {

	@Shadow
	public abstract PlayerModel getModel();

	@Inject(
		method = "setModelStatus",
		at = @At(
			value = "FIELD",
			opcode = Opcodes.PUTFIELD,
			target = "Lnet/minecraft/client/render/model/entity/PlayerModel;leftHandItemId:I"
		)
	)
	private void ornitheAnimations$reAssignShownLayer(ClientPlayerEntity entity, CallbackInfo ci) {
		if (OrnitheAnimations.INSTANCE.getConfig().getSIMPLE_SKIN_RENDERING().get()) {
			/* 1.7 doesn't have any skin layers except for the headwear */
			PlayerModel playerModel = getModel();
			ModelPart[] wearLayers = {
				playerModel.jacket,
				playerModel.leftPants,
				playerModel.rightPants,
				playerModel.leftSleeve,
				playerModel.rightSleeve
			};
			for (ModelPart layer : wearLayers) {
				layer.visible = false;
			}
		}
	}
}
