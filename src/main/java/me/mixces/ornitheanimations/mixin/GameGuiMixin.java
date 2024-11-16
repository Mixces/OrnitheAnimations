package me.mixces.ornitheanimations.mixin;

import me.mixces.ornitheanimations.OrnitheAnimations;
import net.minecraft.client.gui.GameGui;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameGui.class)
public abstract class GameGuiMixin {

	//todo: find a better way to do this :p
	@Unique
	private static final ThreadLocal<Boolean> ornitheAnimations$bl = ThreadLocal.withInitial(() -> false);

	@ModifyVariable(
		method = "renderStatusBars",
		at = @At(
			value = "STORE",
			ordinal = 0
		),
		index = 4
	)
	private boolean ornitheAnimations$disableFlashingCheck(boolean bl) {
		ornitheAnimations$bl.set(bl);
		return false;
	}

	@ModifyArg(
		method = "renderStatusBars",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/GameGui;drawTexture(IIIIII)V",
			ordinal = 3
		),
		index = 2
	)
	private int ornitheAnimations$enableFlashingCheck(int par1) {
		return par1 + (OrnitheAnimations.INSTANCE.getConfig().getREMOVE_HEART_FLASHING().get() ? (ornitheAnimations$bl.get() ? 1 : 0) * 9 : 0);
	}
}
