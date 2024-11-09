package me.mixces.ornitheanimations.mixin;

import net.minecraft.client.gui.widget.ListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
		ci.cancel();
		int var1 = getMaxScroll();

		if (var1 < 0)
			var1 /= 2;

		if (!centerAlongY && var1 < 0)
			var1 = 0;

		if (scrollAmount < 0.0F)
			scrollAmount = 0.0F;

		if (scrollAmount > (float)var1)
			scrollAmount = (float)var1;
	}

	@Redirect(
		method = "getMaxScroll",
		at = @At(
			value = "INVOKE",
			target = "Ljava/lang/Math;max(II)I"
		)
	)
	private int ornitheAnimations$removeNonNegativeRestriction(int a, int b) {
		return b;
	}
}
