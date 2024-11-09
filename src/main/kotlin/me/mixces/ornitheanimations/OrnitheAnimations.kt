package me.mixces.ornitheanimations

import me.mixces.ornitheanimations.config.Config
import net.ornithemc.osl.config.api.ConfigManager
import net.ornithemc.osl.entrypoints.api.ModInitializer

object OrnitheAnimations : ModInitializer {

    lateinit var config: Config

    override fun init() {
        config = Config
        ConfigManager.register(Config)
    }
}
