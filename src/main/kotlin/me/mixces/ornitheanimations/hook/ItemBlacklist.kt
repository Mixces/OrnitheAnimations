@file:JvmName("ItemBlacklist")

package me.mixces.ornitheanimations.hook

import me.mixces.ornitheanimations.config.Config
import net.minecraft.item.BannerItem
import net.minecraft.item.ItemStack
import net.minecraft.item.SkullItem

/* these items cause issues :sob: */
private val blacklistedItems = mutableMapOf(
    BannerItem::class.java to true,
    SkullItem::class.java to true
)

fun isPresent(stack: ItemStack): Boolean {
    /* exclude SkullItem from blacklist based on config condition */
    if (stack.item is SkullItem && Config.OLD_SKULL_MODEL.get()) {
        return false
    }

    return stack.item::class.java in blacklistedItems
}
