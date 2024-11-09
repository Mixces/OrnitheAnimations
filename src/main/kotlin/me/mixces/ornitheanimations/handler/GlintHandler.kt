@file:JvmName("GlintHandler")

package me.mixces.ornitheanimations.handler

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.Tessellator
import me.mixces.ornitheanimations.util.GlHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.render.texture.TextureAtlas
import net.minecraft.client.render.texture.TextureManager
import net.minecraft.resource.Identifier
import org.lwjgl.opengl.GL11

fun renderEnchantmentGlint(textureManager: TextureManager, glintTexture: Identifier, setupGuiTransform: () -> Unit) = with(textureManager) {
    /* renders the gui glint. values taken from 1.7 */
    GlStateManager.enableRescaleNormal()
    GlStateManager.depthFunc(GL11.GL_GEQUAL)
    GlStateManager.disableLighting()
    GlStateManager.depthMask(false)
    this.bind(glintTexture)
    GlStateManager.enableAlphaTest()
    GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f)
    GlStateManager.enableBlend()
    GlStateManager.blendFuncSeparate(GL11.GL_DST_ALPHA, GL11.GL_ONE, GL11.GL_ZERO, GL11.GL_ZERO)
    GlStateManager.color4f(0.5f, 0.25f, 0.8f, 1.0f)

    GlStateManager.pushMatrix()
    setupGuiTransform()
    /* this is needed to adapt the glint to the gui */
    GlHelper.translate(-0.25f, -0.25f, -0.25f).scale(0.5f, 0.5f, 0.5f)
    renderFace()
    GlStateManager.popMatrix()

    GlStateManager.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)
    GlStateManager.depthMask(true)
    GlStateManager.enableLighting()
    GlStateManager.depthFunc(GL11.GL_LEQUAL)
    GlStateManager.disableAlphaTest()
    GlStateManager.disableRescaleNormal()
    GlStateManager.disableLighting()
    this.bind(TextureAtlas.BLOCKS_LOCATION)
}

private fun renderFace() = with(Tessellator.getInstance()) {
    /* drawing the glint in one batch */
    this.builder.apply {
        begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_TEX)
        drawGlint(this, ((Minecraft.getTime() % 3000L) / 3000.0f).toDouble())
        drawGlint(this, (((Minecraft.getTime() % 4873L) / 4873.0f) - 0.0625f).toDouble())
    }.let {
        this.end()
    }
    /* kotlin magic */
}

private fun drawGlint(builder: BufferBuilder, width: Double) = with(builder) {
    /* draws the glint. taken from 1.7 */
    val height = 0.0625
    vertex(0.0, 0.0, 0.0).texture(width + height * 4.0, height).nextVertex()
    vertex(1.0, 0.0, 0.0).texture(width + height * 5.0, height).nextVertex()
    vertex(1.0, 1.0, 0.0).texture(width + height, 0.0).nextVertex()
    vertex(0.0, 1.0, 0.0).texture(width, 0.0).nextVertex()
}
