package me.mixces.ornithe_animations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ornithemc.osl.entrypoints.api.ModInitializer;

public class OrnitheAnimations implements ModInitializer {

	public static final Logger LOGGER = LogManager.getLogger("Ornithe Animations");

	@Override
	public void init() {
		LOGGER.info("initializing example mod!");
	}
}
