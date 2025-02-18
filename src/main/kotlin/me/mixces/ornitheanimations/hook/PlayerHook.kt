@file:JvmName("PlayerHook")

package me.mixces.ornitheanimations.hook

import me.mixces.ornitheanimations.dsl.mc
import net.minecraft.client.entity.living.player.LocalClientPlayerEntity
import net.minecraft.entity.Entity

fun isSelf(entity: Entity): Boolean {
    return entity is LocalClientPlayerEntity && mc.player.networkId == entity.networkId
}
