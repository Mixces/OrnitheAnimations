package me.mixces.ornitheanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.mixces.ornitheanimations.config.Config;
import net.minecraft.client.network.handler.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.TitlesS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

	@ModifyExpressionValue(
		method = "handleAddXpOrb",
		at = @At(
			value = "CONSTANT",
			args = "doubleValue=32"
		)
	)
	private double ornitheAnimations$oldOrbRendering(double original) {
		return original / (Config.INSTANCE.getOLD_XP_ORB_RENDERING().get() ?
			32.0D : 1.0D /* renders the xp orbs similar to 1.7 by oddly offsetting them */
		);
	}

	@ModifyExpressionValue(
		method = "handleEntityPickup",
		at = @At(
			value = "CONSTANT",
			args = "floatValue=0.5"
		)
	)
	private float ornitheAnimations$oldItemPickup(float original) {
		/* taken from 1.7 */
		return Config.INSTANCE.getOLD_ITEM_PICKUP().get() ? -0.5F : original;
	}

	@Inject(
		method = "handleTitles",
		at = @At("HEAD"),
		cancellable = true
	)
	private void ornitheAnimations$disableTitlesPacket(TitlesS2CPacket packet, CallbackInfo ci) {
		if (Config.INSTANCE.getREMOVE_TITLES().get()) {
			/* 1.7 doesn't have titles */
			ci.cancel();
		}
	}
}
