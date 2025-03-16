@file:JvmName("ModelUtil")

package me.mixces.ornitheanimations.util

import me.mixces.ornitheanimations.dsl.mc
import net.minecraft.client.resource.ModelIdentifier
import net.minecraft.client.resource.model.BakedModel
import net.minecraft.item.ItemStack

fun getModel(model: String): BakedModel {
    return mc.blockRenderDispatcher.modelShaper.manager.getModel(ModelIdentifier(model, "inventory"))
}

fun getSkullModel(stack: ItemStack): BakedModel {
    val id = when (stack.metadata) {
        0 -> "old_skull_skeleton"
        1 -> "old_skull_wither"
        2 -> "old_skull_zombie"
        4 -> "old_skull_creeper"
        else -> "old_skull_char"
    }
    return getModel(id)
}
