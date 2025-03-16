package me.mixces.ornitheanimations

import me.mixces.ornitheanimations.config.Config
import net.ornithemc.osl.config.api.ConfigManager
import net.ornithemc.osl.entrypoints.api.ModInitializer

object OrnitheAnimations : ModInitializer {

    override fun init() {
        ConfigManager.register(Config)
    }
}
