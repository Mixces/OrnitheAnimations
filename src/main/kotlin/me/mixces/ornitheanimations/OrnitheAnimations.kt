package me.mixces.ornitheanimations

import net.ornithemc.osl.entrypoints.api.ModInitializer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object OrnitheAnimations : ModInitializer {

    private var LOGGER: Logger = LogManager.getLogger("Ornithe Animations")

    override fun init() {
        LOGGER.info("Initializing Ornithe Animations!")
    }
}
