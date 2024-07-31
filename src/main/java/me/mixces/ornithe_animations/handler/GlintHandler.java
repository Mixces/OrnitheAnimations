package me.mixces.ornithe_animations.handler;

import net.minecraft.client.render.texture.TextureAtlasSprite;
import net.minecraft.client.resource.model.BakedModel;
import net.minecraft.client.resource.model.BasicBakedModel;

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
}
