@file:JvmName("DamageTint")

package me.mixces.ornitheanimations.hook

import com.mojang.blaze3d.platform.GlStateManager
import me.mixces.ornitheanimations.dsl.mc
import java.nio.FloatBuffer

fun setDamageTint(buffer: FloatBuffer) = with(mc.gameRenderer) {
    disableLightMap()
    GlStateManager.disableTexture()
    GlStateManager.disableAlphaTest()
    GlStateManager.enableBlend()
    GlStateManager.blendFunc(770, 771)
    GlStateManager.depthFunc(514)
    GlStateManager.color4f(buffer[0], buffer[1], buffer[2], buffer[3] + 0.1f /* matches 1.7 */)
}

fun unsetDamageTint() = with(mc.gameRenderer){
    GlStateManager.depthFunc(515)
    GlStateManager.disableBlend()
    GlStateManager.enableAlphaTest()
    GlStateManager.enableTexture()
    enableLightMap()
}

