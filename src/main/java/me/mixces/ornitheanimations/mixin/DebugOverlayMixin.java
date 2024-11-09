package me.mixces.ornitheanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.mixces.ornitheanimations.hook.DebugComponents;
import net.minecraft.client.gui.overlay.DebugOverlay;
import net.minecraft.client.render.TextRenderer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DebugOverlay.class)
public class DebugOverlayMixin {

	@Shadow
	@Final
	private TextRenderer textRenderer;

	@Redirect(
		method = "drawGameInfo",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/overlay/DebugOverlay;getGameInfo()Ljava/util/List;"
		)
	)
	private List<String> ornitheAnimations$replaceGameInfo(DebugOverlay instance) {
		return DebugComponents.getLeft();
	}

	@Inject(
		method = "drawGameInfo",
		at = @At("TAIL"),
		remap = false
	)
	private void ornitheAnimations$addBottomLeftColumn(CallbackInfo ci) {
		/* renders the bottom left column of debug text, but in the greyish color just like 1.7 */
		final int fontHeight = 8;
		int top = DebugComponents.getLeft().size() * 10 + 4 /* should be 64, just like 1.7 */;
		for (String msg : DebugComponents.getLeftBottom()) {
			if (msg == null) continue;
			textRenderer.draw(msg, 2, top, 14737632, true);
			top += fontHeight;
		}
	}

	@ModifyExpressionValue(
		method = "drawSystemInfo",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/overlay/DebugOverlay;getSystemInfo()Ljava/util/List;"
		)
	)
	private List<String> ornitheAnimations$replaceSystemInfo(List<String> original) {
		return DebugComponents.getRight();
	}

	@WrapOperation(
		method = {"drawGameInfo", "drawSystemInfo"},
		at = @At(
			value = "FIELD",
			opcode = Opcodes.GETFIELD,
			target = "Lnet/minecraft/client/render/TextRenderer;fontHeight:I"
		)
	)
	private int ornitheAnimations$changeFontHeight(TextRenderer instance, Operation<Integer> original) {
		return 10;
	}

	@WrapWithCondition(
		method = {"drawGameInfo", "drawSystemInfo"},
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/overlay/DebugOverlay;fill(IIIII)V"
		)
	)
	private boolean ornitheAnimations$removeBackgroundRectangle(int left, int top, int right, int bottom, int color) {
		/* disable rendering the rectangular background, just like 1.7 */
		return false;
	}

	@WrapOperation(
		method = "drawSystemInfo",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/TextRenderer;draw(Ljava/lang/String;III)I"
		)
	)
	private int ornitheAnimations$addTextShadow2(TextRenderer instance, String text, int x, int y, int color, Operation<Integer> original) {
		/* uses the alternative drawString method which allows text shadows, just like in 1.7 */
		return instance.draw(text, x, y, color, true);
	}

	@WrapOperation(
		method = "drawGameInfo",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/TextRenderer;draw(Ljava/lang/String;III)I"
		)
	)
	private int ornitheAnimations$addTextShadow(TextRenderer instance, String text, int x, int y, int color, Operation<Integer> original) {
		/* same as above redirect, but the text is white, just like in 1.7 */
		return instance.draw(text, x, y, 16777215, true);
	}
}