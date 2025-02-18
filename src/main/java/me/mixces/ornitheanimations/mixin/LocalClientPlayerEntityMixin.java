package me.mixces.ornitheanimations.mixin;

import me.mixces.ornitheanimations.OrnitheAnimations;
import net.minecraft.client.entity.living.player.LocalClientPlayerEntity;
import net.minecraft.client.player.input.PlayerInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalClientPlayerEntity.class)
public abstract class LocalClientPlayerEntityMixin extends PlayerEntityMixin {

    @Shadow
	public PlayerInput input;

    @Inject(
		method = "tickAi",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/entity/living/player/LocalClientPlayerEntity;pushAwayFrom(DDD)Z",
			ordinal = 0
		)
    )
    private void ornitheAnimations$sneakYSize(CallbackInfo ci) {
		if (!OrnitheAnimations.INSTANCE.getConfig().getSMOOTH_SNEAKING().get()) {
			return;
		}
        if (input.sneaking && ornitheAnimations$ySize < 0.2F) {
            ornitheAnimations$ySize = 0.2F;
        }
    }
}
