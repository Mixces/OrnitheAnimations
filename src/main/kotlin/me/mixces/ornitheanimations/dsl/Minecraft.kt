@file:JvmName("Minecraft")

package me.mixces.ornitheanimations.dsl

import net.minecraft.client.Minecraft

val mc: Minecraft get() = Minecraft.getInstance()
