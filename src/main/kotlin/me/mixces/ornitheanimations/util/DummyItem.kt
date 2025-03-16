package me.mixces.ornitheanimations.util

import net.minecraft.item.Item
import net.minecraft.item.ItemStack

/* the goal of this class is to literally be a placeholder LOL */
object DummyItem: Item() {

    @JvmStatic
    fun getStack(): ItemStack {
        return ItemStack(this)
    }
}
