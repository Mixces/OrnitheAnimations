package me.mixces.ornitheanimations.mixin;

import me.mixces.ornitheanimations.OrnitheAnimations;
import net.minecraft.client.gui.widget.ListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ListWidget.class)
public abstract class ListWidgetMixin {

	@Shadow
	public abstract int getMaxScroll();

	@Shadow
	protected float scrollAmount;

	@Shadow
	protected boolean centerAlongY;

	//todo: can this be written better?
	@Inject(
		method = "capScrolling",
		at = @At("HEAD"),
		cancellable = true
	)
	private void ornitheAnimations$allowNonNegativeScrolling(CallbackInfo ci) {
		if (!OrnitheAnimations.INSTANCE.getConfig().getCENTER_GUI_SELECTION().get()) {
			return;
		}

		ci.cancel();
		int var1 = getMaxScroll();

		if (var1 < 0) {
			var1 /= 2;
		}

		if (!centerAlongY && var1 < 0) {
			var1 = 0;
		}

		if (scrollAmount < 0.0F) {
			scrollAmount = 0.0F;
		}

		if (scrollAmount > (float) var1) {
			scrollAmount = (float) var1;
		}
	}

	@ModifyArgs(
		method = "getMaxScroll",
		at = @At(
			value = "INVOKE",
			target = "Ljava/lang/Math;max(II)I"
		)
	)
	private void ornitheAnimations$removeNonNegativeRestriction(Args args) {
		if (OrnitheAnimations.INSTANCE.getConfig().getCENTER_GUI_SELECTION().get()) {
			args.set(0, args.get(1));
		}
	}
}
