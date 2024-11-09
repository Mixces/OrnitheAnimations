@file:JvmName("GlintModel")

package me.mixces.ornitheanimations.hook

import net.minecraft.client.render.texture.TextureAtlasSprite
import net.minecraft.client.resource.model.BakedModel
import net.minecraft.client.resource.model.BasicBakedModel
import net.minecraft.util.math.Direction

private val glintMap = hashMapOf<HashedModel, BakedModel>()

fun getModel(model: BakedModel): BakedModel = with(model) {
    return glintMap.computeIfAbsent(HashedModel(this)) {
        BasicBakedModel.Builder(this, CustomTextureAtlasSprite).build()
    }
}

data class HashedModel(val data: List<Int>) {
    constructor(model: BakedModel) : this((Direction.entries.flatMap { face -> model.getQuads(face) } + model.quads).flatMap { it.vertices.slice(0..2) })
}

object CustomTextureAtlasSprite : TextureAtlasSprite(null) {
    override fun getU(u: Double) = (-u / 16).toFloat()
    override fun getV(v: Double) = (v / 16).toFloat()
}
