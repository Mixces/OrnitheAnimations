package me.mixces.ornitheanimations.config

import me.mixces.ornitheanimations.OrnitheAnimations
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.EntryListWidget
import net.minecraft.client.gui.widget.OptionListWidget
import net.ornithemc.osl.config.api.config.option.BooleanOption


class ConfigScreen(
    private val parentScreen: Screen
) : Screen() {

    private val options = OrnitheAnimations.config.getGroup(Config.GROUP_NAME).options.filterIsInstance<BooleanOption>().toMutableList()
    private lateinit var listWidget: EntryListWidget

    private lateinit var list: EntryListWidget

    override fun init() {
        super.init()
        buttons.clear()
//        options.forEachIndexed { index, option ->
//            buttons.add(ButtonWidget(index, getRowID(index), getRowPos(index), 150, 20, formatOption(option.name.toReadableFormat(), option.get())))
//        }
        list = ConfigListEntry(minecraft, width, height, 32, height - 32, 25, OrnitheAnimations.config.getGroup(Config.GROUP_NAME));
        buttons.add(ButtonWidget(2024, getCenter - 100, height - 27, "Done"))
    }

    override fun render(mouseX: Int, mouseY: Int, tickDelta: Float) {
        renderBackground()
        list.render(mouseX, mouseY, tickDelta)
        drawCenteredString(textRenderer, Config.GROUP_NAME, width / 2, 15, 0xFFFFFF);
        super.render(mouseX, mouseY, tickDelta)
    }

    override fun buttonClicked(button: ButtonWidget) {
        super.buttonClicked(button)
        if (!button.active) {
            return
        }

//        options.forEachIndexed { index, option ->
//            if (button.id == index) {
//                option.set(!option.get())
//                button.message = formatOption(option.name.toReadableFormat(), option.get())
//            }
//        }

        if (button.id == 2024) {
            minecraft.options.save()
            minecraft.openScreen(parentScreen);
        }
    }

    override fun removed() {
        minecraft.options.save()
        super.removed()
    }

    private val getCenter get() = width / 2
    private val getHeight get() = height / 6

    private fun getRowPos(rowNumber: Int): Int {
        val adjustedRow = rowNumber / 2
        return getHeight + 24 * adjustedRow - 6
    }

    private fun getRowID(buttonId: Int): Int {
        return getCenter + (80 * (if (buttonId % 2 == 1) 1 else -1) - 75)
    }

    private fun getSuffix(enabled: Boolean): String {
        return ": " + (if (enabled) "ON" else "OFF")
    }

    private fun formatOption(name: String, setting: Boolean): String {
        return name + getSuffix(setting)
    }

    private fun String.toReadableFormat(): String {
        return replace(Regex("([a-z])([A-Z])"), "$1 $2").replaceFirstChar { it.uppercase() }
    }
}
