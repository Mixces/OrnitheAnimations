package me.mixces.ornitheanimations.mixin;

import me.mixces.ornitheanimations.OrnitheAnimations;
import net.minecraft.client.entity.living.player.ClientPlayerEntity;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.PlayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {

	@Shadow
	private PlayerRenderer defaultPlayerRenderer;

	@Shadow
	public float cameraPitch;

	@Inject(
		method = "prepare",
		at = @At(
			value = "FIELD",
			opcode = Opcodes.PUTFIELD,
			target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;cameraYaw:F",
			ordinal = 0,
			shift = At.Shift.AFTER
		),
		slice = @Slice(
			from = @At(
				value = "FIELD",
				opcode = Opcodes.GETFIELD,
				target = "Lnet/minecraft/client/options/GameOptions;perspective:I"
			)
		)
	)
	private void ornitheAnimations$fixCameraRotation(World world, TextRenderer textRenderer, Entity camera, Entity targetEntity, GameOptions options, float tickDelta, CallbackInfo ci) {
		if (OrnitheAnimations.INSTANCE.getConfig().getMIRRORED_PROJECTILES().get()) {
			cameraPitch *= -1;
		}
	}

	@Inject(
		method = "getRenderer(Lnet/minecraft/entity/Entity;)Lnet/minecraft/client/render/entity/EntityRenderer;",
		at = @At("HEAD"),
		cancellable = true
	)
	private void ornitheAnimations$defaultToSteve(Entity entity, CallbackInfoReturnable<PlayerRenderer> cir) {
		if (OrnitheAnimations.INSTANCE.getConfig().getSIMPLE_SKIN_RENDERING().get() && entity instanceof ClientPlayerEntity) {
			/* 1.7 doesn't have Alex skins! */
			/* thank you toggle */
			cir.setReturnValue(defaultPlayerRenderer);
		}
	}
}
