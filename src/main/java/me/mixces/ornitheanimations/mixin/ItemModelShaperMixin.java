package me.mixces.ornitheanimations.mixin;

import me.mixces.ornitheanimations.config.Config;
import me.mixces.ornitheanimations.util.ModelUtil;
import net.minecraft.client.render.item.ItemModelShaper;
import net.minecraft.client.resource.model.BakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SkullItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemModelShaper.class)
public abstract class ItemModelShaperMixin {

	@Inject(
 		method = "getModel(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/client/resource/model/BakedModel;",
 		at = @At("HEAD"),
 		cancellable = true
 	)
 	private void ornitheAnimations$useCustomModel$skull(ItemStack stack, CallbackInfoReturnable<BakedModel> cir) {
 		if (Config.INSTANCE.getOLD_SKULL_MODEL().get() && stack.getItem() instanceof SkullItem) {
 			cir.setReturnValue(ModelUtil.getSkullModel(stack));
 		}
 	}
}
