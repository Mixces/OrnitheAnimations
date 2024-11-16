package me.mixces.ornitheanimations.mixin;

import me.mixces.ornitheanimations.OrnitheAnimations;
import net.minecraft.client.ClientPlayerInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {

	@Shadow
	@Final
	private Minecraft minecraft;

	@Shadow
	private float miningProgress;

	@Shadow
	public abstract void stopMiningBlock();

	@Inject(
		method = "updateBlockMining",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/ClientPlayerInteractionManager;updateSelectedHotbarSlot()V",
			shift = At.Shift.AFTER
		),
		cancellable = true
	)
    private void ornitheAnimations$resetDestroyProgress(BlockPos pos, Direction face, CallbackInfoReturnable<Boolean> cir) {
		if (!OrnitheAnimations.INSTANCE.getConfig().getBLOCK_HITTING().get()) {
			return;
		}
        if (minecraft.player.isUsingItem() && minecraft.player.canModifyWorld()) {
            if (miningProgress > 0.0F) {
                stopMiningBlock();
            }
			cir.setReturnValue(true);
        }
    }
}
