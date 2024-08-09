package me.mixces.ornithe_animations.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.entity.living.player.LocalClientPlayerEntity;
import net.minecraft.client.player.input.PlayerInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
        if (input.sneaking && ornitheAnimations$ySize < 0.2F) {
            ornitheAnimations$ySize = 0.2F;
        }
    }

    @WrapMethod(method = "swingHand")
    private void ornitheAnimations$useFakeSwing(Operation<Void> original) {
        if (isUsingItem()) {
            ornitheAnimations$swingItem();
        } else {
			original.call();
		}
    }

    @Unique
    private void ornitheAnimations$swingItem() {
		if (!handSwinging || handSwingTicks >= ornitheAnimations$getMiningSpeedMultiplier() / 2 || handSwingTicks < 0) {
			handSwingTicks = -1;
			handSwinging = true;
		}
    }
}
