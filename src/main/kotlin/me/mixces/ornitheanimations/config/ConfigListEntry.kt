package me.mixces.ornitheanimations.config

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.EntryListWidget
import net.ornithemc.osl.config.api.config.option.BooleanOption
import net.ornithemc.osl.config.api.config.option.group.OptionGroup

/**
 * Based off of
 * @see net.minecraft.client.gui.widget.OptionListWidget
 */
class ConfigListEntry(
    minecraft: Minecraft, width: Int, height: Int, minY: Int, maxY: Int, itemHeight: Int, group: OptionGroup
) : EntryListWidget(
    minecraft, width, height, minY, maxY, itemHeight
) {

    private val entries = mutableListOf<Entry>()

    init {
        centerAlongY = false

        group.options.filterIsInstance<BooleanOption>().chunked(2).forEach { options ->
            val buttonWidget = options.getOrNull(0)?.let { createWidget(width / 2 - 155, it) }
            val buttonWidget2 = options.getOrNull(1)?.let { createWidget(width / 2 - 155 + 160, it) }
            entries.add(Entry(buttonWidget, buttonWidget2))
        }
    }

    private fun createWidget(x: Int, option: BooleanOption): ConfigButtonWidget {
        return ConfigButtonWidget(x, option, option.toReadableName())
    }

    override fun getEntry(index: Int): Entry = entries[index]

    override fun size(): Int = entries.size

    override fun getRowWidth(): Int = 400

    override fun getScrollbarPosition(): Int = super.getScrollbarPosition() + 32

    inner class Entry(
        private val left: ButtonWidget?,
        private val right: ButtonWidget?
    ) : EntryListWidget.Entry {

        override fun render(
            index: Int, x: Int, y: Int, width: Int, height: Int, mouseX: Int, mouseY: Int, hovered: Boolean
        ) {
            listOfNotNull(left, right).forEach { button ->
                button.y = y
                button.render(minecraft, mouseX, mouseY)
            }
        }

        override fun mouseClicked(
            index: Int, mouseX: Int, mouseY: Int, button: Int, entryMouseX: Int, entryMouseY: Int
        ): Boolean {
            return listOfNotNull(left, right).any { option ->
                if (option.isMouseOver(minecraft, mouseX, mouseY) && option is ConfigButtonWidget) {
                    option.toggleOption()
                    true
                } else false
            }
        }

        override fun mouseReleased(index: Int, mouseX: Int, mouseY: Int, button: Int, entryMouseX: Int, entryMouseY: Int) {
            listOfNotNull(left, right).forEach { it.mouseReleased(mouseX, mouseY) }
        }

        override fun renderOutOfBounds(index: Int, x: Int, y: Int) {
            /* no-op */
        }
    }

    private fun BooleanOption.toReadableName(): String {
        val readableName = name.replace(Regex("([a-z])([A-Z])"), "$1 $2").replaceFirstChar { it.uppercase() }
        val state = if (get()) "ON" else "OFF"
        return "$readableName: $state"
    }

    private fun ConfigButtonWidget.toggleOption() {
        val option = getOption()
        option.set(!option.get())
        message = option.toReadableName()
    }
}
