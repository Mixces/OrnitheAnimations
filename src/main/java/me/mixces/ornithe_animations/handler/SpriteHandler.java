package me.mixces.ornithe_animations.handler;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tessellator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.texture.TextureAtlas;
import net.minecraft.client.render.texture.TextureAtlasSprite;
import net.minecraft.client.resource.model.BakedModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SpriteHandler {

	//todo: work on it!
	private static final Map<String, TextureAtlasSprite> spriteCache = new HashMap<>();
	private static final Map<String, String> layerIdentifierMap = new HashMap<>();

	public static void renderSpriteWithLayer(BakedModel model, ItemStack stack, Item item) {
		final TextureAtlas atlas = Minecraft.getInstance().getBlocksAtlas();
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder builder = tessellator.getBuilder();
		final TextureAtlasSprite layer0 = model.getParticleIcon();
		final String layerIdentifier = getLayerIdentifier(model, stack);
		final TextureAtlasSprite layer1 = (layerIdentifier != null) ? getCachedSprite(atlas, layerIdentifier) : null;
		builder.begin(7, DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL);
		drawSprite(builder, layer0, item, stack, 0);
		drawSprite(builder, layer1, item, stack, 1);
		tessellator.end();
	}

	private static TextureAtlasSprite getCachedSprite(TextureAtlas atlas, String spriteName) {
		return spriteCache.computeIfAbsent(spriteName, atlas::getSprite);
	}

	private static void drawSprite(BufferBuilder builder, TextureAtlasSprite sprite, Item item, ItemStack stack, int pass) {
		if (sprite == null) return;
		double uMin = sprite.getUMin();
		double uMax = sprite.getUMax();
		double vMin = sprite.getVMin();
		double vMax = sprite.getVMax();
		Color color = new Color(item.getDisplayColor(stack, pass));
		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();
		int alpha = color.getAlpha();
		builder.vertex(-0.5, -0.25, 0.0).texture(uMin, vMax).color(red, green, blue, alpha).normal(0.0f, 1.0f, 0.0f).nextVertex();
		builder.vertex(0.5, -0.25, 0.0).texture(uMax, vMax).color(red, green, blue, alpha).normal(0.0f, 1.0f, 0.0f).nextVertex();
		builder.vertex(0.5, 0.75, 0.0).texture(uMax, vMin).color(red, green, blue, alpha).normal(0.0f, 1.0f, 0.0f).nextVertex();
		builder.vertex(-0.5, 0.75, 0.0).texture(uMin, vMin).color(red, green, blue, alpha).normal(0.0f, 1.0f, 0.0f).nextVertex();
	}

	private static String getLayerIdentifier(BakedModel model, ItemStack stack) {
		String spriteName = model.getParticleIcon().getName();
		String baseIdentifier = layerIdentifierMap.get(spriteName);
		if (baseIdentifier == null) return null;
		if ("minecraft:items/potion_bottle".equals(baseIdentifier)) {
			boolean isSplash = PotionItem.isSplashPotion(stack.getMetadata());
			return baseIdentifier + (isSplash ? "_splash" : "_drinkable");
		}
		return baseIdentifier;
	}

	static {
		layerIdentifierMap.put("minecraft:items/potion_overlay", "minecraft:items/potion_bottle");
		layerIdentifierMap.put("minecraft:items/leather_helmet", "minecraft:items/leather_helmet_overlay");
		layerIdentifierMap.put("minecraft:items/leather_chestplate", "minecraft:items/leather_chestplate_overlay");
		layerIdentifierMap.put("minecraft:items/leather_leggings", "minecraft:items/leather_leggings_overlay");
		layerIdentifierMap.put("minecraft:items/leather_boots", "minecraft:items/leather_boots_overlay");
		layerIdentifierMap.put("minecraft:items/spawn_egg", "minecraft:items/spawn_egg_overlay");
		layerIdentifierMap.put("minecraft:items/fireworks_charge", "minecraft:items/fireworks_charge_overlay");
	}
}
