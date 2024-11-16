package me.mixces.ornitheanimations.config

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.widget.*
import net.ornithemc.osl.config.api.config.option.BooleanOption
import net.ornithemc.osl.config.api.config.option.group.OptionGroup

/**
 * Based off of
 * @see net.minecraft.client.gui.widget.OptionListWidget
 */
class ConfigListEntry(
    minecraft: Minecraft,
    width: Int,
    height: Int,
    minY: Int,
    maxY: Int,
    itemHeight: Int,
    group: OptionGroup
) : EntryListWidget(
    minecraft, width, height, minY, maxY, itemHeight
) {

    private val entries = mutableListOf<Entry>()

    init {
        centerAlongY = false

        val options = group.options.filterIsInstance<BooleanOption>().toMutableList()

        for (i in options.indices step 2) {
            val option = options[i]
            val option2 = if (i < options.size - 1) options[i + 1] else null
            val buttonWidget = createWidget(i, width / 2 - 155, 0, option)
            val buttonWidget2 = createWidget(i, width / 2 - 155 + 160, 0, option2)
            entries.add(Entry(buttonWidget, buttonWidget2))
        }
    }

    private fun createWidget(id: Int, x: Int, y: Int, option: BooleanOption?): ButtonWidget {
        return ButtonWidget(id, x, y, option?.name)
    }

    override fun getEntry(index: Int): Entry = entries[index]

    override fun size(): Int = entries.size

    override fun getRowWidth(): Int = 400

    override fun getScrollbarPosition(): Int = super.getScrollbarPosition() + 32

    @Environment(EnvType.CLIENT)
    class Entry(
        private val left: ButtonWidget?,
        private val right: ButtonWidget?
    ) : EntryListWidget.Entry {

        private val minecraft: Minecraft = Minecraft.getInstance()

        override fun render(
            index: Int,
            x: Int,
            y: Int,
            width: Int,
            height: Int,
            mouseX: Int,
            mouseY: Int,
            hovered: Boolean
        ) {
            left?.let {
                it.y = y
                it.render(minecraft, mouseX, mouseY)
            }
            right?.let {
                it.y = y
                it.render(minecraft, mouseX, mouseY)
            }
        }

        override fun mouseClicked(
            index: Int,
            mouseX: Int,
            mouseY: Int,
            button: Int,
            entryMouseX: Int,
            entryMouseY: Int
        ): Boolean {
            left?.let {
                if (it.isMouseOver(minecraft, mouseX, mouseY)) {
                    if (it is OptionButtonWidget) {
                        minecraft.options.setValue(it.option, 1)
                        it.message = "Hey"
                    }
                    return true
                }
            }
            right?.let {
                if (it.isMouseOver(minecraft, mouseX, mouseY)) {
                    if (it is OptionButtonWidget) {
                        minecraft.options.setValue(it.option, 1)
                        it.message = "Hey"
                    }
                    return true
                }
            }
            return false
        }

        override fun mouseReleased(
            index: Int,
            mouseX: Int,
            mouseY: Int,
            button: Int,
            entryMouseX: Int,
            entryMouseY: Int
        ) {
            left?.mouseReleased(mouseX, mouseY)
            right?.mouseReleased(mouseX, mouseY)
        }

        override fun renderOutOfBounds(index: Int, x: Int, y: Int) {
            /* no-op */
        }
    }
}
