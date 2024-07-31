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
public abstract class LocalClientPlayerEntityMixin extends LivingEntityMixin {

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
        final LocalClientPlayerEntity player = ((LocalClientPlayerEntity) (Object) this);
        if (player.isUsingItem()) {
            ornitheAnimations$swingItem(player);
        } else {
			original.call();
		}
    }

    @Unique
    private void ornitheAnimations$swingItem(LocalClientPlayerEntity player) {
		if (!player.handSwinging || player.handSwingTicks >= ornitheAnimations$getMiningSpeedMultiplier() / 2 || player.handSwingTicks < 0) {
			player.handSwingTicks = -1;
			player.handSwinging = true;
		}
    }
}
