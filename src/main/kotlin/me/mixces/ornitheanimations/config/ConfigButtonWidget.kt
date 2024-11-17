package me.mixces.ornitheanimations.config

import net.minecraft.client.gui.widget.ButtonWidget
import net.ornithemc.osl.config.api.config.option.BooleanOption

/**
 * Based off of
 * @see net.minecraft.client.gui.widget.OptionButtonWidget
 */
class ConfigButtonWidget(
    x: Int, private var option: BooleanOption, message: String
) : ButtonWidget(0, x, 0, 150, 20, message) {

    fun getOption(): BooleanOption {
        return option
    }
}
