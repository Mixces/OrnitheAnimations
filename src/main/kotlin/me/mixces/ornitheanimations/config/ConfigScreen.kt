package me.mixces.ornitheanimations.config

import me.mixces.ornitheanimations.OrnitheAnimations
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.EntryListWidget
import net.ornithemc.osl.config.api.config.option.group.OptionGroup

/**
 * Based off of
 * @see net.minecraft.client.gui.screen.VideoOptionsScreen
 */
class ConfigScreen(
    private val parentScreen: Screen?
) : Screen() {

    private var listWidget: EntryListWidget? = null

    override fun init() {
        super.init()
        val group: OptionGroup = OrnitheAnimations.config.getGroup(Config.GROUP_NAME)
        listWidget = ConfigListEntry(minecraft, width, height, 32, height - 32, 25, group)
        buttons.clear()
        buttons.add(ButtonWidget(0, width / 2 - 100, height - 27, "Done"))
    }

    override fun handleMouse() {
        super.handleMouse()
        listWidget?.handleMouse()
    }

    override fun render(mouseX: Int, mouseY: Int, tickDelta: Float) {
        listWidget?.render(mouseX, mouseY, tickDelta)
        drawCenteredString(textRenderer, Config.GROUP_NAME, width / 2, 15, 0xFFFFFF)
        super.render(mouseX, mouseY, tickDelta)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClicked(mouseX, mouseY, mouseButton)
        listWidget?.mouseClicked(mouseX, mouseY, mouseButton)
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseReleased(mouseX, mouseY, mouseButton)
        listWidget?.mouseReleased(mouseX, mouseY, mouseButton)
    }

    override fun buttonClicked(button: ButtonWidget) {
        super.buttonClicked(button)
        if (!button.active) {
            return
        }
        if (button.id == 0) {
            minecraft.options.save()
            minecraft.openScreen(parentScreen)
        }
    }

    override fun removed() {
        minecraft.options.save()
        super.removed()
    }
}
