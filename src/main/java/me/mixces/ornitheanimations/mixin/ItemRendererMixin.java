package me.mixces.ornitheanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tessellator;
import kotlin.Unit;
import me.mixces.ornitheanimations.OrnitheAnimations;
import me.mixces.ornitheanimations.handler.GlintHandler;
import me.mixces.ornitheanimations.hook.GlintModel;
import me.mixces.ornitheanimations.hook.ModelUtil;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.block.ModelTransformations;
import net.minecraft.client.render.texture.TextureManager;
import net.minecraft.client.resource.model.BakedModel;
import net.minecraft.client.resource.model.BakedQuad;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.resource.Identifier;
import net.minecraft.util.math.Direction;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

	@Shadow
	@Final
	private static Identifier ENCHANTMENT_GLINT_LOCATION;

	@Shadow
	@Final
	private TextureManager textureManager;

	@Shadow
	protected abstract void prepareGuiItemRender(int x, int y, boolean gui3d);

	@Shadow
	protected abstract void renderQuad(BufferBuilder bufferBuilder, BakedQuad quad, int color);

	@Unique
	private boolean ornitheAnimations$isGui;

	@Unique
	private boolean ornitheAnimations$isHeld;

	@Unique
	private BakedModel ornitheAnimations$model = null;

	@Inject(
		method = "renderItem",
		at = @At("HEAD")
	)
	private void ornitheAnimations$captureModel(ItemStack stack, BakedModel model, CallbackInfo ci) {
		ornitheAnimations$model = model;
	}

	@Inject(
		method = "renderItem",
		at = @At("TAIL")
	)
	private void ornitheAnimations$clearModel(CallbackInfo ci) {
		/* we need to clear this field */
		ornitheAnimations$model = null;
	}

	@ModifyArgs(
		method = "applyNormal",
		at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/blaze3d/vertex/BufferBuilder;postNormal(FFF)V"
		)
	)
	private void ornitheAnimations$modifyNormals(Args args) {
		if (!OrnitheAnimations.INSTANCE.getConfig().getFAST_ITEMS().get()) {
			return;
		}
		if (!ornitheAnimations$isGui && !ornitheAnimations$isHeld && !ornitheAnimations$model.isGui3d()) {
			args.setAll(args.get(0), args.get(2), args.get(1));
		}
	}

	@ModifyExpressionValue(
		method = "render(Lnet/minecraft/client/resource/model/BakedModel;ILnet/minecraft/item/ItemStack;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/resource/model/BakedModel;getQuads()Ljava/util/List;"
		)
	)
	private List<BakedQuad> overflowAnimations$changeToSprite(List<BakedQuad> quads, @Local(argsOnly = true) BakedModel model) {
		List<BakedQuad> filteredQuads = quads.stream().filter(baked -> baked.getFace() == Direction.SOUTH).toList();
		if (!model.isGui3d() && (ornitheAnimations$isGui ||
			(OrnitheAnimations.INSTANCE.getConfig().getFAST_ITEMS().get() && !ornitheAnimations$isHeld))) {
			return filteredQuads;
		}
		return quads;
	}

	@ModifyArg(
		method = "renderItem",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/item/ItemRenderer;renderEnchantmentGlint(Lnet/minecraft/client/resource/model/BakedModel;)V"
		)
	)
	public BakedModel ornitheAnimations$replaceModel(BakedModel model) {
		return OrnitheAnimations.INSTANCE.getConfig().getOLD_GLINT().get() ? GlintModel.getModel(model) : model;
	}

	@ModifyArg(
		method = "render(Lnet/minecraft/client/resource/model/BakedModel;I)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/item/ItemRenderer;render(Lnet/minecraft/client/resource/model/BakedModel;ILnet/minecraft/item/ItemStack;)V"
		),
		index = 1
	)
	public int ornitheAnimations$replaceColor(int color) {
		return OrnitheAnimations.INSTANCE.getConfig().getOLD_GLINT().get() ? -10407781 : color;
	}

	@Inject(
		method = "renderEnchantmentGlint",
		at = @At("HEAD"),
		cancellable = true
	)
	public void ornitheAnimations$disableDefaultGlint(CallbackInfo ci) {
		if (OrnitheAnimations.INSTANCE.getConfig().getOLD_GLINT().get() && ornitheAnimations$isGui) ci.cancel();
		if (OrnitheAnimations.INSTANCE.getConfig().getFAST_ITEMS().get() && !ornitheAnimations$isGui && !ornitheAnimations$isHeld)
			ci.cancel();
	}

	@ModifyExpressionValue(
		method = "renderEnchantmentGlint",
		at = @At(
			value = "CONSTANT",
			args = "floatValue=8.0F")
	)
	public float ornitheAnimations$modifyScale(float original) {
		return OrnitheAnimations.INSTANCE.getConfig().getOLD_GLINT().get() ? 1.0F / original : original;
	}

	@Inject(
		method = "renderHeldItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/living/LivingEntity;Lnet/minecraft/client/render/model/block/ModelTransformations$Type;)V",
		at = @At("HEAD")
	)
	public void ornitheAnimations$captureHeldMode(ItemStack stack, LivingEntity entity, ModelTransformations.Type transformationType, CallbackInfo ci) {
		ornitheAnimations$isHeld = true;
	}

	@Inject(
		method = "renderHeldItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/living/LivingEntity;Lnet/minecraft/client/render/model/block/ModelTransformations$Type;)V",
		at = @At("TAIL")
	)
	public void ornitheAnimations$releaseHeldMode(ItemStack stack, LivingEntity entity, ModelTransformations.Type transformationType, CallbackInfo ci) {
		ornitheAnimations$isHeld = false;
	}

	@Inject(
		method = "renderGuiItemModel",
		at = @At("HEAD")
	)
	public void ornitheAnimations$captureGuiMode(ItemStack stack, int x, int y, CallbackInfo ci) {
		ornitheAnimations$isGui = true;
	}

	@Inject(
		method = "renderGuiItemModel",
		at = @At("TAIL")
	)
	public void ornitheAnimations$renderGuiGlint(ItemStack stack, int x, int y, CallbackInfo ci) {
		ornitheAnimations$isGui = false;
	}

	@Inject(
		method = "renderGuiItem",
		at = @At(
			value = "FIELD",
			opcode = Opcodes.PUTFIELD,
			target = "Lnet/minecraft/client/render/item/ItemRenderer;zOffset:F",
			ordinal = 1
		)
	)
	public void ornitheAnimations$useCustomGlint(ItemStack stack, int x, int y, CallbackInfo ci) {
		if (OrnitheAnimations.INSTANCE.getConfig().getOLD_GLINT().get() && stack.hasEnchantmentGlint()) {
			GlintHandler.renderEnchantmentGlint(textureManager, ENCHANTMENT_GLINT_LOCATION, () -> {
				prepareGuiItemRender(x, y, false); /* i love kotlin */
				return Unit.INSTANCE;
			});
		}
	}

	@Inject(
		method = "renderItem",
		at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/blaze3d/platform/GlStateManager;popMatrix()V"
		)
	)
	private void ornitheAnimations$useCustomModel(ItemStack stack, BakedModel model, CallbackInfo ci) {
		if (OrnitheAnimations.INSTANCE.getConfig().getOLD_LAYER_GLINT().get() && stack.getItem() instanceof PotionItem) {
			/* renders the splash/drinkable bottle AFTER the glint rendering like in 1.7 */
			String id = PotionItem.isSplashPotion(stack.getMetadata()) ? "bottle_splash_empty" : "bottle_drinkable_empty";
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferBuilder = tessellator.getBuilder();
			bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormat.BLOCK_NORMALS);
			ModelUtil.getModelFromIdentifier(id).getQuads().forEach(quad -> renderQuad(bufferBuilder, quad, -1));
			tessellator.end();
		}
	}
}
