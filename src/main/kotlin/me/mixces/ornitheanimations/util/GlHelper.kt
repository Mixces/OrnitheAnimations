@file:JvmName("GlHelper")

package me.mixces.ornitheanimations.util

import com.mojang.blaze3d.platform.GlStateManager

/**
 * I wrote this for fun :) It's impractical which is why I love it.
 */

fun pitch(pitch: Float) {
    GlStateManager.rotatef(pitch, 1.0f, 0.0f, 0.0f)
}

fun yaw(yaw: Float) {
    GlStateManager.rotatef(yaw, 0.0f, 1.0f, 0.0f)
}

fun roll(roll: Float) {
    GlStateManager.rotatef(roll, 0.0f, 0.0f, 1.0f)
}

fun translate(x: Float, y: Float, z: Float) {
    GlStateManager.translatef(x, y, z)
}

fun scale(x: Float, y: Float, z: Float) {
    GlStateManager.scalef(x, y, z)
}
