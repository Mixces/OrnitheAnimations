package me.mixces.ornithe_animations.handler;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.living.LivingEntity;

public class DamageLayerHandler {
    public static void setupOverlayColor(LivingEntity entitylivingbaseIn, float tickDelta) {
        float f12 = entitylivingbaseIn.getBrightness(tickDelta);
		Minecraft.getInstance().gameRenderer.disableLightMap();
		GlStateManager.disableTexture();
		GlStateManager.disableAlphaTest();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		GlStateManager.depthFunc(514);
		GlStateManager.color4f(f12, 0.0F, 0.0F, 0.4F);
    }

    public static void tearDownOverlayColor() {
		GlStateManager.depthFunc(515);
		GlStateManager.disableBlend();
		GlStateManager.enableAlphaTest();
		GlStateManager.enableTexture();
		Minecraft.getInstance().gameRenderer.enableLightMap();
    }
}
