package me.mixces.ornitheanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.mixces.ornitheanimations.OrnitheAnimations;
import net.minecraft.client.network.handler.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.TitlesS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
		return original / (OrnitheAnimations.INSTANCE.getConfig().getOLD_XP_ORB_RENDERING().get() ?
			32.0D : 1.0D /* renders the xp orbs similar to 1.7 by oddly offsetting them */
		);
	}

	@Inject(
		method = "handleTitles",
		at = @At("HEAD"),
		cancellable = true
	)
	private void ornitheAnimations$disableTitlesPacket(TitlesS2CPacket packet, CallbackInfo ci) {
		if (OrnitheAnimations.INSTANCE.getConfig().getREMOVE_TITLES().get()) {
			/* 1.7 doesn't have titles */
			ci.cancel();
		}
	}
}
