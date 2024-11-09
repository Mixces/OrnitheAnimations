@file:JvmName("ItemBlacklist")

package me.mixces.ornitheanimations.hook

import net.minecraft.item.BannerItem
import net.minecraft.item.ItemStack
import net.minecraft.item.SkullItem

/* these items are special therefore im excluding them lol */
private val blacklistedItems = mapOf(
    SkullItem::class.java to true,
    BannerItem::class.java to true
)

fun isPresent(stack: ItemStack) = stack.item::class.java in blacklistedItems
