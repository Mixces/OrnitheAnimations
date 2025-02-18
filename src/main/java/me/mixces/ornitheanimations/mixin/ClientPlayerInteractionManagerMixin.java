package me.mixces.ornitheanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.mixces.ornitheanimations.OrnitheAnimations;
import net.minecraft.client.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {

	@Shadow
	public abstract boolean isMiningBlock();

	@ModifyExpressionValue(
		method = "updateBlockMining",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/ClientPlayerInteractionManager;isMiningBlock(Lnet/minecraft/util/math/BlockPos;)Z"
		)
	)
	private boolean ornitheAnimations$resetDestroyProgress(boolean original) {
		return OrnitheAnimations.INSTANCE.getConfig().getBLOCK_HITTING().get() ? original && isMiningBlock() : original;
	}
}
