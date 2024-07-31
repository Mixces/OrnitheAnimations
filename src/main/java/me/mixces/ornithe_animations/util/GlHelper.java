package me.mixces.ornithe_animations.util;

import com.mojang.blaze3d.platform.GlStateManager;

/**
 * I wrote this purely for fun lol. There is almost nothing practical about it.
 */
public class GlHelper {
	public static GlHelper getBuilder() {
		return new GlHelper();
	}

    public GlHelper pitch(float pitch) {
        GlStateManager.rotatef(pitch, 1.0f, 0.0f, 0.0f);
        return this;
    }

    public GlHelper yaw(float yaw) {
        GlStateManager.rotatef(yaw, 0.0f, 1.0f, 0.0f);
        return this;
    }

    public GlHelper roll(float roll) {
        GlStateManager.rotatef(roll, 0.0f, 0.0f, 1.0f);
        return this;
    }

    public GlHelper translate(float x, float y, float z) {
        GlStateManager.translatef(x, y, z);
        return this;
    }

	public GlHelper scale(float x, float y, float z) {
		GlStateManager.scalef(x, y, z);
		return this;
	}
}
