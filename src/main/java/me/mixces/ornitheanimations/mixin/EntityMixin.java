package me.mixces.ornitheanimations.mixin;

import me.mixces.ornitheanimations.OrnitheAnimations;
import net.minecraft.entity.Entity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

	@Shadow
	public double y;

    @Unique
	public float ornitheAnimations$ySize;

    @Inject(
		method = "move",
		at = @At(
			value = "INVOKE_STRING",
			target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V",
			args = "ldc=move",
			shift = At.Shift.AFTER
		)
    )
    private void ornitheAnimations$smoothenYSize(double x, double y, double z, CallbackInfo ci) {
		if (OrnitheAnimations.INSTANCE.getConfig().getSMOOTH_SNEAKING().get()) {
			ornitheAnimations$ySize *= 0.4F;
		}
    }

    @Inject(
		method = "setPositionFromShape",
		at = @At(
			value = "FIELD",
			opcode = Opcodes.PUTFIELD,
			target = "Lnet/minecraft/entity/Entity;y:D",
			shift = At.Shift.AFTER
		)
    )
    private void ornitheAnimations$reAssignY(CallbackInfo ci) {
		if (OrnitheAnimations.INSTANCE.getConfig().getSMOOTH_SNEAKING().get()) {
			y -= ornitheAnimations$ySize;
		}
    }
}
