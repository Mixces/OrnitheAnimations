package me.mixces.ornitheanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.network.handler.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

	@ModifyExpressionValue(
		method = "handleAddXpOrb",
		at = @At(
			value = "CONSTANT",
			args = "doubleValue=32"
		)
	)
	private double ornitheAnimations$oldOrbRendering(double original) {
		return original / 32.0D; /* renders the xp orbs similar to 1.7 by oddly offsetting them */
	}
}
