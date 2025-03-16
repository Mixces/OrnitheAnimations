package me.mixces.ornitheanimations.mixin;

import net.minecraft.client.resource.model.ModelBakery;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {

    @Shadow
    private Map<Item, List<String>> itemVariants;

    @Inject(
        method = "registerItemVariants",
        at = @At("TAIL")
    )
    private void ornitheAnimations$registerCustomModels(CallbackInfo ci) {
		/* register our custom models */
		// TODO: how can i get the option to load :sob:

		/* potions */
		List<String> originalPotions = itemVariants.get(Items.POTION);
		List<String> potionComponents = Arrays.asList("bottle_drinkable_empty", "bottle_overlay", "bottle_splash_empty");
		originalPotions.addAll(potionComponents);
		itemVariants.put(Items.POTION, originalPotions);

		/* skulls */
		List<String> originalSkulls = itemVariants.get(Items.SKULL);
		List<String> oldSkulls = Arrays.asList("old_skull_skeleton", "old_skull_wither", "old_skull_zombie", "old_skull_char", "old_skull_creeper");
		originalSkulls.addAll(oldSkulls);
		itemVariants.put(Items.SKULL, originalSkulls);
    }
}
