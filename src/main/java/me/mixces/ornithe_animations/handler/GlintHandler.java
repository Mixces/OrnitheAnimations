package me.mixces.ornithe_animations.handler;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tessellator;
import me.mixces.ornithe_animations.util.GlHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.texture.TextureAtlas;
import net.minecraft.client.render.texture.TextureAtlasSprite;
import net.minecraft.client.resource.model.BakedModel;
import net.minecraft.client.resource.model.BasicBakedModel;
import net.minecraft.resource.Identifier;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

public class GlintHandler {
	private static final Map<String, CustomTextureAtlasSprite> uvCache = new HashMap<>();
	private static final Map<CacheKey, BakedModel> modelCache = new HashMap<>();

	public static BakedModel getGlint(BakedModel model) {
		final CustomTextureAtlasSprite sprite = uvCache.computeIfAbsent(null, CustomTextureAtlasSprite::new);
		return modelCache.computeIfAbsent(new CacheKey(model, sprite), key -> new BasicBakedModel.Builder(key.model, key.sprite).build());
	}

	private static class CustomTextureAtlasSprite extends TextureAtlasSprite {
		private CustomTextureAtlasSprite(String name) {
			super(name);
		}

		@Override
		public float getU(double u) {
			return (float) (-u / 16);
		}

		@Override
		public float getV(double v) {
			return (float) (v / 16);
		}
	}

	private static class CacheKey {
		private final BakedModel model;
		private final CustomTextureAtlasSprite sprite;

		CacheKey(BakedModel model, CustomTextureAtlasSprite sprite) {
			this.model = model;
			this.sprite = sprite;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			CacheKey cacheKey = (CacheKey) o;
			return model.equals(cacheKey.model) && sprite.equals(cacheKey.sprite);
		}

		@Override
		public int hashCode() {
			int result = model.hashCode();
			result = 31 * result + sprite.hashCode();
			return result;
		}
	}

	//todo: fix this mess lol
	public static void renderGlintGui(int x, int y, float zOffset, Identifier glintTexture) {
		float red = 128 / 255f;
		float green = 64 / 255f;
		float blue = 204 / 255f;
		float alpha = 255 / 255f;

		float currentTime = Minecraft.getTime();
		float twentyPixels = 20.0F / 256.0F;
		float a = (float) ((currentTime % 3000L) / 3000.0);
		float b = (float) ((currentTime % 4873L) / 4873.0);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuilder();

		GlStateManager.enableRescaleNormal();
		GlStateManager.depthFunc(GL11.GL_GEQUAL);
		GlStateManager.disableLighting();
		GlStateManager.depthMask(false);
		Minecraft.getInstance().getTextureManager().bind(glintTexture);
		GlStateManager.enableAlphaTest();
		GlStateManager.alphaFunc(516, 0.1f);
		GlStateManager.enableBlend();
		GlStateManager.blendFuncSeparate(772, 1, 0, 0);
		GlStateManager.color4f(red, green, blue, alpha);

		GlStateManager.pushMatrix();
		GlHelper glBuilder = GlHelper.getBuilder();
		glBuilder
			.translate(x, y, 100.0f + zOffset).translate(8.0f, 8.0f, 0.0f)
			.scale(1.0f, 1.0f, -1.0f).scale(0.5f, 0.5f, 0.5f).scale(64.0f, 64.0f, 64.0f)
			.pitch(180.0f);
		GlStateManager.disableLighting();
		glBuilder.scale(0.5f, 0.5f, 0.5f).translate(-0.5f, -0.5f, -0.5f);
		builder.begin(7, DefaultVertexFormat.POSITION_TEX);
		drawVertices(builder, a, twentyPixels);
		drawVertices(builder, b - twentyPixels, twentyPixels);
		tessellator.end();
		GlStateManager.popMatrix();

		GlStateManager.blendFuncSeparate(770, 771, 1, 0);
		GlStateManager.depthMask(true);
		GlStateManager.enableLighting();
		GlStateManager.depthFunc(GL11.GL_LEQUAL);
		GlStateManager.disableAlphaTest();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableLighting();
		Minecraft.getInstance().getTextureManager().bind(TextureAtlas.BLOCKS_LOCATION);
	}

	private static void drawVertices(BufferBuilder builder, double uOffset, double twentyPixels) {
		builder.vertex(0.0, 0.0, 0.0).texture(uOffset + twentyPixels * 4.0, twentyPixels).nextVertex();
		builder.vertex(1.0, 0.0, 0.0).texture(uOffset + twentyPixels * 5.0, twentyPixels).nextVertex();
		builder.vertex(1.0, 1.0, 0.0).texture(uOffset + twentyPixels, 0.0).nextVertex();
		builder.vertex(0.0, 1.0, 0.0).texture(uOffset, 0.0).nextVertex();
	}
}
