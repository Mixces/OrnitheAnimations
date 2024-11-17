package me.mixces.ornitheanimations.config

import net.minecraft.client.gui.widget.ButtonWidget
import net.ornithemc.osl.config.api.config.option.BooleanOption

/**
 * Based off of
 * @see net.minecraft.client.gui.widget.OptionButtonWidget
 */
class ConfigButtonWidget(
    id: Int, x: Int, y: Int, private var option: BooleanOption, message: String
) : ButtonWidget(id, x, y, 150, 20, message) {

    fun getOption(): BooleanOption {
        return option
    }
}
