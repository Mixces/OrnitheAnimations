package me.mixces.ornitheanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.mixces.ornitheanimations.OrnitheAnimations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.overlay.PlayerTabOverlay;
import net.minecraft.client.network.PlayerInfo;
import net.minecraft.text.Text;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

import java.util.List;

@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {

	@Shadow
	@Final
	private Minecraft minecraft;

	@WrapOperation(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/List;size()I",
			ordinal = 1
		)
	)
	public int ornitheAnimations$replace(List<PlayerInfo> instance, Operation<Integer> original) {
		/* renders a fixed amount of player slots just like 1.7 */
		return OrnitheAnimations.INSTANCE.getConfig().getSIMPLE_PLAYER_LIST().get() ? minecraft.getNetworkHandler().maxPlayerCount : original.call(instance);
	}

	@WrapOperation(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Ljava/lang/Math;min(II)I",
			ordinal = 1
		)
	)
	public int ornitheAnimations$staticSlotWidth(int a, int b, Operation<Integer> original) {
		/* makes the slot width static just like 1.7 */
		return OrnitheAnimations.INSTANCE.getConfig().getSIMPLE_PLAYER_LIST().get() ? 300 : original.call(a, b);
	}

	@ModifyVariable(
		method = "render",
		at = @At("STORE"),
		index = 13
	)
	private int ornitheAnimations$capSlotWidth(int value) {
		/* caps the slot width just like 1.7 */
		if (OrnitheAnimations.INSTANCE.getConfig().getSIMPLE_PLAYER_LIST().get() && value > 150) {
			value = 150;
		}
		return value;
	}

	@ModifyExpressionValue(
		method = "render",
		at = @At(
			value = "CONSTANT",
			args = "intValue=5"
		),
		slice = @Slice(
			from = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/scoreboard/ScoreboardObjective;getRenderType()Lnet/minecraft/scoreboard/criterion/ScoreboardCriterion$RenderType;",
				ordinal = 1
			),
			to = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/client/gui/overlay/PlayerTabOverlay;fill(IIIII)V",
				ordinal = 2
			)
		)
	)
	private int ornitheAnimations$removeBackgroundSpace(int constant) {
		/* cancels spacing */
		return OrnitheAnimations.INSTANCE.getConfig().getSIMPLE_PLAYER_LIST().get() ? 0 : constant;
	}

	@ModifyExpressionValue(
		method = "render",
		at = @At(
			value = "FIELD",
			opcode = Opcodes.GETFIELD,
			target = "Lnet/minecraft/client/gui/overlay/PlayerTabOverlay;header:Lnet/minecraft/text/Text;",
			ordinal = 0
		)
	)
	public Text ornitheAnimations$disableHeaderElement(Text original) {
		/* disables the tab header */
		return OrnitheAnimations.INSTANCE.getConfig().getSIMPLE_PLAYER_LIST().get() ? null : original;
	}

	@ModifyExpressionValue(
		method = "render",
		at = @At(
			value = "FIELD",
			opcode = Opcodes.GETFIELD,
			target = "Lnet/minecraft/client/gui/overlay/PlayerTabOverlay;footer:Lnet/minecraft/text/Text;",
			ordinal = 0
		)
	)
	public Text ornitheAnimations$disableFooterElement(Text original) {
		/* disables the tab footer */
		return OrnitheAnimations.INSTANCE.getConfig().getSIMPLE_PLAYER_LIST().get() ? null : original;
	}

	@ModifyVariable(
		method = "render",
		at = @At("STORE"),
		index = 11
	)
	private boolean ornitheAnimations$disablePlayerHeads(boolean original) {
		/* disables the rendering of player heads */
		return !OrnitheAnimations.INSTANCE.getConfig().getSIMPLE_PLAYER_LIST().get() && original;
	}

	@ModifyArg(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/overlay/PlayerTabOverlay;fill(IIIII)V",
			ordinal = 2
		),
		index = 2
	)
	private int ornitheAnimations$removeExtraPixels(int par1) {
		/* corrects for an extra pixel added in 1.8+ */
		return par1 - (OrnitheAnimations.INSTANCE.getConfig().getSIMPLE_PLAYER_LIST().get() ? 1 : 0);
	}
}
